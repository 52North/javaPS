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
package org.n52.restfulwpsproxy.serializer.util;

import java.util.Iterator;
import java.util.List;

/**
 * Utility class.
 *
 * @author bpr
 */
public class Utils {

    /**
     * Creates a String from an Object List. Concatenates the objects with an empty string as separator.
     *
     * @param objectList The List containing the Objects.
     * @return Concatenated Objects as String with an empty string as separator.
     */
    public static String getStringFromObjectList(List<?> objectList) {

        String result = "";

        Iterator<?> objectIterator = objectList.iterator();

        while (objectIterator.hasNext()) {
            result = result.concat(objectIterator.next().toString());
            if (objectIterator.hasNext()) {
                result = result.concat(" ");
            }
        }

        return result;
    }

}
