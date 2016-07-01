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

import org.n52.iceland.ogc.wps.description.LiteralInputDescription;
import org.n52.javaps.io.literal.LiteralType;

/**
 *
 * @author Christian Autermann
 */
public interface TypedLiteralInputDescription extends LiteralInputDescription, TypedLiteralDescription, TypedProcessInputDescription<LiteralType<?>> {

    interface Builder<T extends TypedLiteralInputDescription, B extends Builder<T, B>>
            extends LiteralInputDescription.Builder<T, B>,
                    TypedLiteralDescription.Builder<T, B> {

    }

}
