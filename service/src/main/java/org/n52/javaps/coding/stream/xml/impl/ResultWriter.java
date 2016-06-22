/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.coding.stream.xml.impl;

import java.net.URI;

import javax.xml.stream.XMLStreamException;

import org.n52.javaps.coding.stream.xml.AbstractXmlElementStreamWriter;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.OWS;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.WPS;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.XLink;
import org.n52.javaps.ogc.wps.JobId;
import org.n52.javaps.ogc.wps.Result;
import org.n52.javaps.ogc.wps.data.Data;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ResultWriter extends AbstractXmlElementStreamWriter<Result> {

    public ResultWriter() {
        super(Result.class);
    }

    @Override
    protected void write(Result result)
            throws XMLStreamException {
        element(WPS.Elem.QN_RESULT, result, x -> {
            namespace(WPS.NS_WPS_PREFIX, WPS.NS_WPS);
            namespace(OWS.NS_OWS_PREFIX, OWS.NS_OWS);
            namespace(XLink.NS_XLINK_PREFIX, XLink.NS_XLINK);

            element(WPS.Elem.QN_JOB_ID, x.getJobId().map(JobId::getValue));
            element(WPS.Elem.QN_EXPIRATION_DATE, x.getExpirationDate().map(this::format));

            for (Data data : x.getOutputs()) {
                writeOutput(data);
            }
        });

    }

    private void writeOutput(Data data)
            throws XMLStreamException {
        element(WPS.Elem.QN_OUTPUT, data, x -> {
            attr(WPS.Attr.AN_ID, x.getId().getValue());

            // FIXME codeSpace is not allowed here...
            attr(OWS.Attr.AN_CODE_SPACE, x.getId().getCodeSpace().map(URI::toString));

            if (x.isGroup()) {
                for (Data output : x.asGroup().getElements()) {
                    writeOutput(output);
                }
            } else {
                delegate(x);
            }
        });
    }
}
