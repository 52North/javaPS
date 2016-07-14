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
package org.n52.javaps.coding.stream.xml.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.n52.javaps.coding.stream.StreamWriter;
import org.n52.javaps.coding.stream.StreamWriterFactory;
import org.n52.javaps.coding.stream.StreamWriterKey;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableSet;

/**
 * TODO
 *
 * @author Benjamin Pross
 *
 */
public class WPS20ResponseStreamWriterFactory implements StreamWriterFactory {

    private static final ImmutableSet<StreamWriterKey> KEYS = ImmutableSet.<StreamWriterKey> builder().add(DescribeProcessResponseWriter.KEY).build();

    @Autowired
    private DescribeProcessResponseWriter describeProcessResponseWriter;

    private Map<StreamWriterKey, StreamWriter<?>> streamWriterKeyMap;

    public void init(){
        streamWriterKeyMap = new HashMap<>();
        streamWriterKeyMap.put(DescribeProcessResponseWriter.KEY, describeProcessResponseWriter);
    }

    @Override
    public Set<StreamWriterKey> getKeys() {
        return Collections.unmodifiableSet(KEYS);
    }

    @Override
    public StreamWriter<?> create(StreamWriterKey key) {
        if (streamWriterKeyMap.keySet().contains(key)) {
            return streamWriterKeyMap.get(key);
        }
        throw new IllegalArgumentException();
    }

}
