/**
 * ﻿Copyright (C) 2007 - 2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *       • Apache License, version 2.0
 *       • Apache Software License, version 1.0
 *       • GNU Lesser General Public License, version 3
 *       • Mozilla Public License, versions 1.0, 1.1 and 2.0
 *       • Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.javaps.coding.decode.kvp;

import java.util.Collections;
import java.util.Set;

import org.n52.iceland.coding.decode.DecoderKey;
import org.n52.iceland.exception.ows.InvalidParameterValueException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.util.KvpHelper;
import org.n52.javaps.ogc.wps.WPS100Constants;
import org.n52.javaps.ogc.wps.WPSConstants;
import org.n52.javaps.request.DescribeProcessRequest;

public class DescribeProcessKvpDecoder extends AbstractKvpDecoder<DescribeProcessRequest> {
    private static final DecoderKey KEY
            = createKey(WPSConstants.SERVICE,
                        WPS100Constants.SERVICEVERSION,
                        WPSConstants.Operations.DescribeProcess.toString());

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.singleton(KEY);
    }

    @Override
    protected DescribeProcessRequest createRequest() {
        return new DescribeProcessRequest();
    }

    @Override
    protected void decodeParameter(DescribeProcessRequest request, String name,
                                   String value)
            throws OwsExceptionReport {
        switch (name.toLowerCase()) {
            case "service":
                request.setService(KvpHelper
                        .checkParameterSingleValue(value, name));
                break;
            case "version":
                request.setVersion(KvpHelper
                        .checkParameterSingleValue(value, name));
                break;
            case "identifier":
                KvpHelper.checkParameterMultipleValues(value, name)
                        .stream().forEach(request::addProcessIdentifier);
                break;
            default:
                throw new InvalidParameterValueException(name, value)
                        .withMessage("The parameter '%s' is not supported.", name);
        }
    }

}
