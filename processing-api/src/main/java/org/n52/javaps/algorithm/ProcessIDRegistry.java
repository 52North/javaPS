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

import java.util.ArrayList;

/**
 * @author Matthias Mueller, TU Dresden
 *
 */
public class ProcessIDRegistry {

    private static ProcessIDRegistry instance = new ProcessIDRegistry();

    private volatile boolean lock = false;

    private static ArrayList<String> idList = new ArrayList<String>();

    private ProcessIDRegistry() {
        // empty private constructor
    }

    public static ProcessIDRegistry getInstance() {
        return instance;
    }

    public boolean addID(String id) {
        while (lock) {
            // spin
        }
        try {
            lock = true;
            boolean retval = idList.add(id);
            lock = false;
            return retval;
        } finally {
            lock = false;
        }
    }

    public synchronized boolean removeID(String id) {
        while (lock) {
            // spin
        }
        try {
            lock = true;
            boolean retval = idList.remove(id);
            lock = false;
            return retval;
        } finally {
            lock = false;
        }
    }

    public boolean containsID(String id) {
        return idList.contains(id);
    }

    public String[] getIDs() {
        return idList.toArray(new String[idList.size()]);
    }

    protected void clearRegistry() {
        while (lock) {
            // spin
        }
        try {
            lock = true;
            idList.clear();
            lock = false;
        } finally {
            lock = false;
        }
    }
}
