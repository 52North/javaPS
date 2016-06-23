package org.n52.javaps.handler;

import java.util.Collections;
import java.util.Set;

import org.n52.iceland.ogc.ows.OwsAllowedValues;
import org.n52.iceland.ogc.ows.OwsAnyValue;
import org.n52.iceland.ogc.ows.OwsDomain;
import org.n52.iceland.ogc.ows.OwsPossibleValues;
import org.n52.iceland.ogc.ows.OwsValue;
import org.n52.javaps.Engine;
import org.n52.javaps.ogc.wps.JobId;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractJobHandler extends AbstractEngineHandler {
    private static final String JOB_ID = "JobId";

    private final boolean discloseJobIds;

    public AbstractJobHandler(Engine engine, boolean discloseJobIds) {
        super(engine);
        this.discloseJobIds = discloseJobIds;
    }

    @Override
    protected Set<OwsDomain> getOperationParameters() {
        OwsPossibleValues allowedValues;
        if (discloseJobIds) {
            allowedValues = new OwsAllowedValues(getEngine().getJobIdentifiers().stream().map(JobId::getValue).map(OwsValue::new));
        } else {
            allowedValues = OwsAnyValue.instance();
        }
        return Collections.singleton(new OwsDomain(JOB_ID, allowedValues));
    }
}
