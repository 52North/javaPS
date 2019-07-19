/*
 * Copyright (C) 2016 by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.restfulwpsproxy.serializer.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.List;

/**
 * @author adewa
 */
public abstract class AbstractWPSJsonModule extends SimpleModule {

    /**
     *
     */
    private static final long serialVersionUID = 1294800523104618214L;

    protected String getFirstArrayElementAsStringIfExist(List<?> arrayList) {
        if (arrayList != null && arrayList.size() > 0) {
            return arrayList.get(0).toString();
        }
        return null;
    }

    protected static final void writeArrayOfObjects(String fieldName, Object[] objects, JsonGenerator jg)
            throws IOException {
        jg.writeArrayFieldStart(fieldName);
        for (Object o : objects) {
            jg.writeObject(o);
        }
        jg.writeEndArray();
    }

    protected static final void writeArrayOfStrings(String fieldName, String[] strings, JsonGenerator jg)
            throws IOException {
        jg.writeArrayFieldStart(fieldName);
        for (String s : strings) {
            jg.writeString(s);
        }
        jg.writeEndArray();
    }

    protected static final String toStringOrEmpty(Object object) {
        return object == null ? "" : object.toString();
    }

    protected static final String toStringOrNull(Object object) {
        return object == null ? null : object.toString();
    }

    protected static final void writeStringFieldIfNotNull(JsonGenerator jg, String field, Object object)
            throws JsonGenerationException, IOException {
        if (object != null) {
            jg.writeStringField(field, object.toString());
        }
    }

    protected static final void writeObjectFieldIfNotNull(JsonGenerator jg, String field, Object object)
            throws JsonGenerationException, IOException {
        if (object != null) {
            jg.writeObjectField(field, object);
        }
    }
}
