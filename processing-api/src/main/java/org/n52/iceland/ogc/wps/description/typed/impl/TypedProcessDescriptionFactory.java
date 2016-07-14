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
package org.n52.iceland.ogc.wps.description.typed.impl;

import org.n52.iceland.ogc.wps.description.LiteralDataDomain;
import org.n52.iceland.ogc.wps.description.ProcessDescriptionBuilderFactory;
import org.n52.iceland.ogc.wps.description.impl.LiteralDataDomainImpl;
import org.n52.iceland.ogc.wps.description.typed.TypedBoundingBoxInputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedBoundingBoxOutputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedComplexInputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedComplexOutputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedGroupInputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedGroupOutputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedLiteralInputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedLiteralOutputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedProcessDescription;

public class TypedProcessDescriptionFactory implements ProcessDescriptionBuilderFactory
        <
            TypedProcessDescription,
            TypedGroupInputDescription,
            TypedGroupOutputDescription,
            TypedLiteralInputDescription,
            TypedLiteralOutputDescription,
            TypedComplexInputDescription,
            TypedComplexOutputDescription,
            TypedBoundingBoxInputDescription,
            TypedBoundingBoxOutputDescription,
            LiteralDataDomain
        > {

    @Override
    public TypedProcessDescriptionImpl.Builder process() {
        return new TypedProcessDescriptionImpl.Builder();
    }

    @Override
    public TypedGroupOutputDescriptionImpl.Builder groupOutput() {
        return new TypedGroupOutputDescriptionImpl.Builder();
    }

    @Override
    public TypedGroupInputDescriptionImpl.Builder groupInput() {
        return new TypedGroupInputDescriptionImpl.Builder();
    }

    @Override
    public TypedLiteralInputDescriptionImpl.Builder literalInput() {
        return new TypedLiteralInputDescriptionImpl.Builder();
    }

    @Override
    public TypedLiteralOutputDescriptionImpl.Builder literalOutput() {
        return new TypedLiteralOutputDescriptionImpl.Builder();
    }

    @Override
    public TypedComplexInputDescriptionImpl.Builder complexInput() {
        return new TypedComplexInputDescriptionImpl.Builder();
    }

    @Override
    public TypedComplexOutputDescriptionImpl.Builder complexOutput() {
        return new TypedComplexOutputDescriptionImpl.Builder();
    }

    @Override
    public  TypedBoundingBoxInputDescriptionImpl.Builder boundingBoxInput() {
        return new TypedBoundingBoxInputDescriptionImpl.Builder();
    }

    @Override
    public TypedBoundingBoxOutputDescriptionImpl.Builder boundingBoxOutput() {
        return new TypedBoundingBoxOutputDescriptionImpl.Builder();
    }

    @Override
    public LiteralDataDomainImpl.Builder literalDataDomain() {
        return new LiteralDataDomainImpl.Builder();
    }

}
