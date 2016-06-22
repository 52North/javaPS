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


import javax.xml.stream.XMLStreamException;

import org.n52.javaps.coding.stream.xml.AbstractXmlElementStreamWriter;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.OWS;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.WPS;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.XLink;
import org.n52.javaps.ogc.wps.StatusInfo;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class StatusInfoWriter extends AbstractXmlElementStreamWriter<StatusInfo> {

    public StatusInfoWriter() {
        super(StatusInfo.class);
    }

    @Override
    protected void write(StatusInfo object)
            throws XMLStreamException {
        element(WPS.Elem.QN_STATUS_INFO, object, x -> {
            namespace(WPS.NS_WPS_PREFIX, WPS.NS_WPS);
            namespace(OWS.NS_OWS_PREFIX, OWS.NS_OWS);
            namespace(XLink.NS_XLINK_PREFIX, XLink.NS_XLINK);
            element(WPS.Elem.QN_JOB_ID, object.getJobId().getValue());
            element(WPS.Elem.QN_STATUS, object.getStatus().getValue());
            element(WPS.Elem.QN_EXPIRATION_DATE, object.getExpirationDate().map(this::format));
            element(WPS.Elem.QN_ESTIMATED_COMPLETION, object.getEstimatedCompletion().map(this::format));
            element(WPS.Elem.QN_NEXT_POLL, object.getNextPoll().map(this::format));
            element(WPS.Elem.QN_PERCENT_COMPLETED, object.getPercentCompleted().map(String::valueOf));
        });
    }
}
