/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.javaps.service.operator.validation;

import java.util.List;
import java.util.Optional;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.exception.CompositeOwsException;
import org.n52.shetland.ogc.ows.exception.InvalidParameterValueException;
import org.n52.shetland.ogc.ows.exception.MissingParameterValueException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.wps.request.DescribeProcessRequest;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class DescribeProcessParameterValidator extends EngineParameterValidator<DescribeProcessRequest> {
    private static final String IDENTIFIER = "Identifier";

    @Override
    public void validate(DescribeProcessRequest request) throws OwsExceptionReport {

        List<OwsCode> identifiers = request.getProcessIdentifier();
        if (identifiers == null || identifiers.isEmpty()) {
            throw new MissingParameterValueException(IDENTIFIER);
        }
        checkIdentifiers(identifiers);
    }

    private void checkIdentifiers(List<OwsCode> identifiers) throws OwsExceptionReport {
        CompositeOwsException exception = new CompositeOwsException();
        identifiers.stream().map(this::checkIdentifier).filter(Optional::isPresent).map(Optional::get).forEach(
                exception::add);
        exception.throwIfNotEmpty();
    }

    private Optional<OwsExceptionReport> checkIdentifier(OwsCode id) {
        if (id == null) {
            return Optional.of(new MissingParameterValueException(IDENTIFIER));
        }
        if (!isAll(id) && !getEngine().hasProcessDescription(id)) {
            return Optional.of(new InvalidParameterValueException(IDENTIFIER, id.getValue()));
        }
        return Optional.empty();
    }

    private boolean isAll(OwsCode id) {
        return !id.getCodeSpace().isPresent() && id.getValue().equals(DescribeProcessRequest.ALL_KEYWORD);
    }

}
