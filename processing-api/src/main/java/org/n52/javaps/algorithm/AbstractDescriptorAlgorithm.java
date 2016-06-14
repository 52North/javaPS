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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.javaps.algorithm.descriptor.ProcessDescription;
import org.n52.javaps.commons.observerpattern.IObserver;
import org.n52.javaps.commons.observerpattern.ISubject;
import org.n52.javaps.io.data.IData;

public abstract class AbstractDescriptorAlgorithm implements IAlgorithm, ISubject {
    private ProcessDescription description;
    private final List<IObserver> observers = new LinkedList<>();
    private final List<String> errorList = new LinkedList<>();
    private Object state = null;

    @Override
    public synchronized ProcessDescription getDescription() {
        if (description == null) {
            description = createAlgorithmDescriptor();
        }
        return description;
    }

    @Override
    public boolean processDescriptionIsValid(String version) {
        return true;// TODO
    }

    protected abstract ProcessDescription createAlgorithmDescriptor();

    @Override
    public Class<? extends IData> getInputDataType(String identifier) {
        ProcessDescription algorithmDescriptor = getDescription();
        if (algorithmDescriptor != null) {
            return getDescription().getInputDescriptor(identifier).getBinding();
        } else {
            throw new IllegalStateException("Instance must have an algorithm descriptor");
        }
    }

    @Override
    public Class<? extends IData> getOutputDataType(String identifier) {
        ProcessDescription algorithmDescriptor = getDescription();
        if (algorithmDescriptor != null) {
            return getDescription().getOutputDescriptor(identifier).getBinding();
        } else {
            throw new IllegalStateException("Instance must have an algorithm descriptor");
        }
    }

    @Override
    public Object getState() {
        return state;
    }

    @Override
    public void update(Object state) {
        this.state = state;
        notifyObservers();
    }

    @Override
    public void addObserver(IObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IObserver o) {
        observers.remove(o);
    }

    public void notifyObservers() {
        this.observers.stream().forEach(o -> o.update(this));
    }

    protected void addError(String error) {
        this.errorList.add(error);
    }

    @Override
    public List<String> getErrors() {
        return Collections.unmodifiableList(this.errorList);
    }
}
