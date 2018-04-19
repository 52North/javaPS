/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.io;

import java.io.IOException;
import java.io.InputStream;

import org.n52.shetland.ogc.wps.Format;
import org.n52.javaps.description.TypedProcessOutputDescription;

/**
 * Basic interface for all Generators.
 *
 * @author Matthias Mueller, TU Dresden
 *
 */
public interface OutputHandler extends InputOutputHandler {

    InputStream generate(TypedProcessOutputDescription<?> description, Data<?> data, Format format)
            throws IOException, EncodingException;

}
