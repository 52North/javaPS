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
package org.n52.javaps;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class UnsupportedOutputFormatException extends EngineException {

    private static final long serialVersionUID = 3746355400310805878L;

    public UnsupportedOutputFormatException() {
    }

    public UnsupportedOutputFormatException(String message) {
        super(message);
    }

    public UnsupportedOutputFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedOutputFormatException(Throwable cause) {
        super(cause);
    }

}
