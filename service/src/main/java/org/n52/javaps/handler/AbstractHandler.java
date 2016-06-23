package org.n52.javaps.handler;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.n52.iceland.binding.Binding;
import org.n52.iceland.binding.BindingRepository;
import org.n52.iceland.coding.OperationKey;
import org.n52.iceland.config.annotation.Configurable;
import org.n52.iceland.config.annotation.Setting;
import org.n52.iceland.ds.OperationHandler;
import org.n52.iceland.exception.ConfigurationError;
import org.n52.iceland.exception.HTTPException;
import org.n52.iceland.exception.ows.NoApplicableCodeException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OwsAllowedValues;
import org.n52.iceland.ogc.ows.OwsDCP;
import org.n52.iceland.ogc.ows.OwsDomain;
import org.n52.iceland.ogc.ows.OwsHttp;
import org.n52.iceland.ogc.ows.OwsMetadata;
import org.n52.iceland.ogc.ows.OwsOperation;
import org.n52.iceland.ogc.ows.OwsRequestMethod;
import org.n52.iceland.ogc.ows.OwsValue;
import org.n52.iceland.service.ServiceSettings;
import org.n52.iceland.util.Validation;
import org.n52.iceland.util.http.HTTPHeaders;
import org.n52.iceland.util.http.HTTPMethods;
import org.n52.iceland.util.http.MediaType;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
@Configurable
public abstract class AbstractHandler implements OperationHandler {
    private URI serviceURL;
    private BindingRepository bindingRepository;

    @Inject
    public void setBindingRepository(BindingRepository bindingRepository) {
        this.bindingRepository = Objects.requireNonNull(bindingRepository);
    }

    @Setting(ServiceSettings.SERVICE_URL)
    public void setServiceURL(final URI serviceURL)
            throws ConfigurationError {
        this.serviceURL = Validation.notNull("Service URL", serviceURL);
    }

    private Set<OwsDCP> getDCP (String service, String version)
            throws OwsExceptionReport {
        return Collections.singleton(getDCP(new OperationKey(service, version, getOperationName())));
    }

    private OwsDCP getDCP(OperationKey decoderKey)
            throws OwsExceptionReport {
        try {
            Set<OwsRequestMethod> methods
                    = bindingRepository.getBindings().values().stream()
                            .flatMap(binding -> getRequestMethods(binding, decoderKey))
                            .collect(Collectors.toSet());

            return new OwsHttp(methods);
        } catch (Exception e) {
            // FIXME valid exception
            throw new NoApplicableCodeException().causedBy(e);
        }
    }

    private Stream<OwsRequestMethod> getRequestMethods(Binding binding, OperationKey decoderKey) {
        URI uri = URI.create(this.serviceURL + binding.getUrlPattern());
        Set<OwsDomain> constraints = getConstraints(binding);

        return Stream.of(HTTPMethods.GET,
                         HTTPMethods.POST,
                         HTTPMethods.PUT,
                         HTTPMethods.DELETE,
                         HTTPMethods.TRACE,
                         HTTPMethods.HEAD,
                         HTTPMethods.OPTIONS)
                .filter(isMethodSupported(binding, decoderKey))
                .map(method -> new OwsRequestMethod(uri, method, constraints));
    }

    private Predicate<String> isMethodSupported(Binding binding, OperationKey decoderKey) {
        return method -> {
            try {
                switch(method) {
                    case HTTPMethods.GET:
                        return binding.checkOperationHttpGetSupported(decoderKey);
                    case HTTPMethods.POST:
                        return binding.checkOperationHttpPostSupported(decoderKey);
                    case HTTPMethods.PUT:
                        return binding.checkOperationHttpPutSupported(decoderKey);
                    case HTTPMethods.DELETE:
                        return binding.checkOperationHttpDeleteSupported(decoderKey);
                    default:
                        return false;
                }
            } catch (HTTPException ex) {
                return false;
            }
        };
    }

    private Set<OwsDomain> getConstraints(Binding binding) {
        Set<OwsDomain> constraints = Optional
                .ofNullable(binding.getSupportedEncodings())
                .filter(x -> !x.isEmpty())
                .map(Collection::stream)
                .map(x -> x.map(MediaType::toString).map(OwsValue::new))
                .map(OwsAllowedValues::new)
                .map(x -> new OwsDomain(HTTPHeaders.CONTENT_TYPE, x))
                .map(Collections::singleton)
                .orElse(null);
        return constraints;
    }

    @Override
    public OwsOperation getOperationsMetadata(String service, String version)
            throws OwsExceptionReport {
         return new OwsOperation(
                 getOperationName(),
                 getOperationParameters(),
                 getOperationConstraints(),
                 getOperationMetadata(),
                 getDCP(service, version));
    }

    protected  Set<OwsDomain> getOperationParameters() {
        return null;
    }

    protected Set<OwsDomain> getOperationConstraints() {
        return null;
    }

    protected Set<OwsMetadata> getOperationMetadata() {
        return null;
    }

}
