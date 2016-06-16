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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.io.data.IData;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class ProcessInputs extends HashMap<OwsCodeType, List<IData>>{

    private static final long serialVersionUID = 921032882074907318L;

    public ProcessInputs() {
    }

    public ProcessInputs(Map<? extends OwsCodeType, ? extends List<IData>> m) {
        super(m);
    }

}
