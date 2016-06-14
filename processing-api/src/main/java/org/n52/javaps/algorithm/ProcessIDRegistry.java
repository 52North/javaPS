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
package org.n52.javaps.algorithm;

import java.util.ArrayList;

import org.n52.iceland.ogc.ows.OwsCodeType;

/**
 * @author Matthias Mueller, TU Dresden
 *
 */
public class ProcessIDRegistry {

    private static ProcessIDRegistry instance = new ProcessIDRegistry();
    private static ArrayList<OwsCodeType> idList = new ArrayList<>();
    private volatile boolean lock = false;

    private ProcessIDRegistry() {
        // empty private constructor
    }


    public boolean addID(OwsCodeType id) {
        while (lock) {
            // spin
        }
        try {
            lock = true;
            boolean retval = idList.add(id);
            lock = false;
            return retval;
        } finally {
            lock = false;
        }
    }

    public synchronized boolean removeID(OwsCodeType id) {
        while (lock) {
            // spin
        }
        try {
            lock = true;
            boolean retval = idList.remove(id);
            lock = false;
            return retval;
        } finally {
            lock = false;
        }
    }

    public boolean containsID(OwsCodeType id) {
        return idList.contains(id);
    }

    public OwsCodeType[] getIDs() {
        return idList.toArray(new OwsCodeType[idList.size()]);
    }

    protected void clearRegistry() {
        while (lock) {
            // spin
        }
        try {
            lock = true;
            idList.clear();
            lock = false;
        } finally {
            lock = false;
        }
    }
    public static ProcessIDRegistry getInstance() {
        return instance;
    }
}
