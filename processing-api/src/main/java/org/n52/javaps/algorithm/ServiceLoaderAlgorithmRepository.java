/**
 * ﻿Copyright (C) 2007 - 2014 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.algorithm.descriptor.ProcessDescription;

public class ServiceLoaderAlgorithmRepository implements IAlgorithmRepository {

    private static final Logger LOG = LoggerFactory
            .getLogger(ServiceLoaderAlgorithmRepository.class);

    private Map<OwsCodeType, Class<? extends IAlgorithm>> currentAlgorithms;

    public ServiceLoaderAlgorithmRepository() {
        this.currentAlgorithms = loadAlgorithms();
    }

    private Map<OwsCodeType, Class<? extends IAlgorithm>> loadAlgorithms() {
        Map<OwsCodeType, Class<? extends IAlgorithm>> result = new HashMap<>();
        ServiceLoader<IAlgorithm> loader = ServiceLoader.load(IAlgorithm.class);

        for (IAlgorithm ia : loader) {
            LOG.debug("Adding algorithm with identifier {} and class {}", ia
                      .getDescription().getIdentifier(), ia.getClass()
                      .getCanonicalName());
            result.put(ia.getDescription().getIdentifier(), ia.getClass());
        }

        return result;
    }

    @Override
    public Set<OwsCodeType> getAlgorithmNames() {
        return this.currentAlgorithms.keySet();
    }

    @Override
    public IAlgorithm getAlgorithm(OwsCodeType processID) {
        Class<? extends IAlgorithm> clazz = this.currentAlgorithms
                .get(processID);
        if (clazz != null) {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                LOG.warn(e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public ProcessDescription getProcessDescription(OwsCodeType processID) {
        IAlgorithm algo = getAlgorithm(processID);
        if (algo != null) {
            return algo.getDescription();
        }
        return null;
    }

    @Override
    public boolean containsAlgorithm(OwsCodeType processID) {
        return this.currentAlgorithms.containsKey(processID);
    }

}
