/*
 * Copyright 2016-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.description.impl;

import org.n52.javaps.description.TypedBoundingBoxInputDescription;
import org.n52.javaps.description.TypedBoundingBoxOutputDescription;
import org.n52.javaps.description.TypedComplexInputDescription;
import org.n52.javaps.description.TypedComplexOutputDescription;
import org.n52.javaps.description.TypedGroupInputDescription;
import org.n52.javaps.description.TypedGroupOutputDescription;
import org.n52.javaps.description.TypedLiteralInputDescription;
import org.n52.javaps.description.TypedLiteralOutputDescription;
import org.n52.javaps.description.TypedProcessDescription;
import org.n52.shetland.ogc.wps.description.BoundingBoxInputDescription;
import org.n52.shetland.ogc.wps.description.BoundingBoxOutputDescription;
import org.n52.shetland.ogc.wps.description.ComplexInputDescription;
import org.n52.shetland.ogc.wps.description.ComplexOutputDescription;
import org.n52.shetland.ogc.wps.description.GroupInputDescription;
import org.n52.shetland.ogc.wps.description.GroupOutputDescription;
import org.n52.shetland.ogc.wps.description.LiteralDataDomain;
import org.n52.shetland.ogc.wps.description.LiteralInputDescription;
import org.n52.shetland.ogc.wps.description.LiteralOutputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;
import org.n52.shetland.ogc.wps.description.impl.LiteralDataDomainImpl;
import org.n52.shetland.ogc.wps.description.impl.ProcessDescriptionFactory;

public class TypedProcessDescriptionFactory
        implements ProcessDescriptionBuilderFactory<TypedProcessDescription,
                                                           TypedGroupInputDescription,
                                                           TypedGroupOutputDescription,
                                                           TypedLiteralInputDescription,
                                                           TypedLiteralOutputDescription,
                                                           TypedComplexInputDescription,
                                                           TypedComplexOutputDescription,
                                                           TypedBoundingBoxInputDescription,
                                                           TypedBoundingBoxOutputDescription,
                                                           LiteralDataDomain> {

    private static final TypedProcessDescriptionFactory INSTANCE = new TypedProcessDescriptionFactory();

    @Override
    public TypedProcessDescriptionImpl.Builder process() {
        return new TypedProcessDescriptionImpl.Builder(this);
    }

    @Override
    public TypedProcessDescriptionImpl.Builder process(ProcessDescription entity) {
        return new TypedProcessDescriptionImpl.Builder(this, entity);
    }

    @Override
    public TypedGroupOutputDescriptionImpl.Builder groupOutput() {
        return new TypedGroupOutputDescriptionImpl.Builder(this);
    }

    @Override
    public TypedGroupOutputDescriptionImpl.Builder groupOutput(GroupOutputDescription entity) {
        return new TypedGroupOutputDescriptionImpl.Builder(this, entity);
    }

    @Override
    public TypedGroupInputDescriptionImpl.Builder groupInput() {
        return new TypedGroupInputDescriptionImpl.Builder(this);
    }

    @Override
    public TypedGroupInputDescriptionImpl.Builder groupInput(GroupInputDescription entity) {
        return new TypedGroupInputDescriptionImpl.Builder(this, entity);
    }

    @Override
    public TypedLiteralInputDescriptionImpl.Builder literalInput() {
        return new TypedLiteralInputDescriptionImpl.Builder(this);
    }

    @Override
    public TypedLiteralInputDescriptionImpl.Builder literalInput(LiteralInputDescription entity) {
        return new TypedLiteralInputDescriptionImpl.Builder(this, entity);
    }

    @Override
    public TypedLiteralOutputDescriptionImpl.Builder literalOutput() {
        return new TypedLiteralOutputDescriptionImpl.Builder(this);
    }

    @Override
    public TypedLiteralOutputDescriptionImpl.Builder literalOutput(LiteralOutputDescription entity) {
        return new TypedLiteralOutputDescriptionImpl.Builder(this, entity);
    }

    @Override
    public TypedComplexInputDescriptionImpl.Builder complexInput() {
        return new TypedComplexInputDescriptionImpl.Builder(this);
    }

    @Override
    public TypedComplexInputDescriptionImpl.Builder complexInput(ComplexInputDescription entity) {
        return new TypedComplexInputDescriptionImpl.Builder(this, entity);
    }

    @Override
    public TypedComplexOutputDescriptionImpl.Builder complexOutput() {
        return new TypedComplexOutputDescriptionImpl.Builder(this);
    }

    @Override
    public TypedComplexOutputDescriptionImpl.Builder complexOutput(ComplexOutputDescription entity) {
        return new TypedComplexOutputDescriptionImpl.Builder(this, entity);
    }

    @Override
    public TypedBoundingBoxInputDescriptionImpl.Builder boundingBoxInput() {
        return new TypedBoundingBoxInputDescriptionImpl.Builder(this);
    }

    @Override
    public TypedBoundingBoxInputDescriptionImpl.Builder boundingBoxInput(BoundingBoxInputDescription entity) {
        return new TypedBoundingBoxInputDescriptionImpl.Builder(this, entity);
    }

    @Override
    public TypedBoundingBoxOutputDescriptionImpl.Builder boundingBoxOutput() {
        return new TypedBoundingBoxOutputDescriptionImpl.Builder(this);
    }

    @Override
    public TypedBoundingBoxOutputDescriptionImpl.Builder boundingBoxOutput(BoundingBoxOutputDescription entity) {
        return new TypedBoundingBoxOutputDescriptionImpl.Builder(this, entity);
    }

    @Override
    public LiteralDataDomainImpl.Builder literalDataDomain() {
        return ProcessDescriptionFactory.instance().literalDataDomain();
    }

    @Override
    public LiteralDataDomainImpl.Builder literalDataDomain(LiteralDataDomain entity) {
        return ProcessDescriptionFactory.instance().literalDataDomain(entity);
    }

    public static TypedProcessDescriptionFactory instance() {
        return INSTANCE;
    }
}
