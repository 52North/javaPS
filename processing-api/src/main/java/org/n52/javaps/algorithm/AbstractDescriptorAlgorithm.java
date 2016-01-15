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
package org.n52.javaps.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.n52.javaps.algorithm.descriptor.AlgorithmDescriptor;
import org.n52.javaps.commons.observerpattern.IObserver;
import org.n52.javaps.commons.observerpattern.ISubject;
import org.n52.javaps.io.GeneratorFactory;
import org.n52.javaps.io.ParserFactory;
import org.n52.javaps.io.data.IData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDescriptorAlgorithm implements IAlgorithm, ISubject {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractDescriptorAlgorithm.class);

    private AlgorithmDescriptor descriptor;

    private ProcessDescription description;

    public AbstractDescriptorAlgorithm() {
        super();
    }

    @Override
    public synchronized ProcessDescription getDescription() {
        if (description == null) {
            description = createProcessDescription();
        }
        return description;
    }

    @Override
    public String getWellKnownName() {
        return getAlgorithmDescriptor().getIdentifier();
    }

    private ProcessDescription createProcessDescription() {

        AlgorithmDescriptor algorithmDescriptor = getAlgorithmDescriptor();

        ProcessDescription processDescription = new ProcessDescription();

        processDescription.setAlgorithmDescriptor(algorithmDescriptor);

        return processDescription;
    }

    @Override
    public boolean processDescriptionIsValid(String version) {
        return true;// TODO
    }

    protected final synchronized AlgorithmDescriptor getAlgorithmDescriptor() {
        if (descriptor == null) {
            descriptor = createAlgorithmDescriptor();
        }
        return descriptor;
    }

    protected abstract AlgorithmDescriptor createAlgorithmDescriptor();

    @Override
    public Class<? extends IData> getInputDataType(String identifier) {
        AlgorithmDescriptor algorithmDescriptor = getAlgorithmDescriptor();
        if (algorithmDescriptor != null) {
            return getAlgorithmDescriptor().getInputDescriptor(identifier).getBinding();
        } else {
            throw new IllegalStateException("Instance must have an algorithm descriptor");
        }
    }

    @Override
    public Class<? extends IData> getOutputDataType(String identifier) {
        AlgorithmDescriptor algorithmDescriptor = getAlgorithmDescriptor();
        if (algorithmDescriptor != null) {
            return getAlgorithmDescriptor().getOutputDescriptor(identifier).getBinding();
        } else {
            throw new IllegalStateException("Instance must have an algorithm descriptor");
        }
    }

    private List<IObserver> observers = new ArrayList<>();

    private Object state = null;

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
        Iterator<IObserver> i = observers.iterator();
        while (i.hasNext()) {
            IObserver o = (IObserver) i.next();
            o.update(this);
        }
    }

    List<String> errorList = new ArrayList<String>();

    protected List<String> addError(String error) {
        errorList.add(error);
        return errorList;
    }

    @Override
    public List<String> getErrors() {
        return errorList;
    }
}
