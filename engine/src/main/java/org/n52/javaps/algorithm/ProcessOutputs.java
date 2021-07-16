/*
 * Copyright 2016-2021 52°North Spatial Information Research GmbH
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

import org.n52.javaps.io.Data;
import org.n52.shetland.ogc.ows.OwsCode;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ProcessOutputs extends HashMap<OwsCode, Data<?>> {
    private static final long serialVersionUID = -3966745236341552277L;

    public ProcessOutputs() {
    }

    public ProcessOutputs(Map<? extends OwsCode, ? extends Data<?>> m) {
        super(m);
    }

    public ProcessOutputs(int initialCapacity) {
        super(initialCapacity);
    }

}
