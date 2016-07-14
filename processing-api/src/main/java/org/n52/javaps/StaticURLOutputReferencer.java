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

import java.net.URI;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.ogc.wps.JobId;
import org.n52.iceland.util.Chain;

public class StaticURLOutputReferencer implements OutputReferencer {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private URI baseURI;

    @Override
    public URI reference(OutputReference identifier) {
        UriComponentsBuilder builder;
        this.lock.readLock().lock();
        try {
            builder = UriComponentsBuilder.fromUri(baseURI);
        } finally {
            this.lock.readLock().unlock();
        }

        builder.pathSegment(identifier.getJobId().getValue());
        identifier.getOutputId().stream().map(OwsCode::getValue)
                .forEach(builder::pathSegment);
        return builder.build().toUri();
    }

    @Override
    public OutputReference dereference(URI uri) throws IllegalArgumentException {
        UriComponents build;
        this.lock.readLock().lock();
        try {
            build = UriComponentsBuilder.fromUri(this.baseURI.relativize(uri)).build();
        } finally {
            this.lock.readLock().unlock();
        }

        Iterator<String> iterator = build.getPathSegments().iterator();
        if (!iterator.hasNext()) {
            throw new IllegalArgumentException();
        }
        JobId jobId = new JobId(iterator.next());
        if (!iterator.hasNext()) {
            throw new IllegalArgumentException();
        }
        List<OwsCode> chain = new LinkedList<>();
        iterator.forEachRemaining(x -> chain.add(new OwsCode(x)));
        return new OutputReference(jobId, new Chain<>(chain));
    }

    public URI getBaseURI() {
        return baseURI;
    }

    public void setBaseURI(URI baseURI) {
        this.lock.writeLock().lock();
        try {
            this.baseURI = Objects.requireNonNull(baseURI);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

}
