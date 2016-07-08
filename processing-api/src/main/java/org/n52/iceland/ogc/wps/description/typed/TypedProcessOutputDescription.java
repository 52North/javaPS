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
package org.n52.iceland.ogc.wps.description.typed;

import org.n52.iceland.ogc.wps.description.ProcessOutputDescription;

/**
 *
 * @author Christian Autermann
 */
public interface TypedProcessOutputDescription<X> extends ProcessOutputDescription, TypedDataDescription<X> {
    @Override
    default TypedLiteralOutputDescription asLiteral() {
        throw new UnsupportedOperationException();
    }

    @Override
    default TypedBoundingBoxOutputDescription asBoundingBox() {
        throw new UnsupportedOperationException();
    }

    @Override
    default TypedComplexOutputDescription asComplex() {
        throw new UnsupportedOperationException();
    }

    @Override
    default TypedGroupOutputDescription asGroup() {
        throw new UnsupportedOperationException();
    }

}
