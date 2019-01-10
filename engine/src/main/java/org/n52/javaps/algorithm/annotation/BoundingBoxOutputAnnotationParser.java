/*
 * Copyright 2016-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.algorithm.annotation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Function;

import org.n52.javaps.description.TypedBoundingBoxOutputDescription;
import org.n52.javaps.description.impl.TypedProcessDescriptionFactory;
import org.n52.shetland.ogc.ows.OwsCRS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BoundingBoxOutputAnnotationParser<M extends AccessibleObject & Member, B extends AbstractOutputBinding<M>> extends
        AbstractOutputAnnotationParser<BoundingBoxOutput, M, B> {

    private static final Logger LOG = LoggerFactory.getLogger(BoundingBoxOutputAnnotationParser.class);

    BoundingBoxOutputAnnotationParser(Function<M, B> bindingFunction) {
        super(bindingFunction);
    }

    @Override
    public Class<? extends BoundingBoxOutput> getSupportedAnnotation() {
        return BoundingBoxOutput.class;
    }

    @Override
    public TypedBoundingBoxOutputDescription createDescription(BoundingBoxOutput annotation,
            B binding) {
        URI defaultCRSURI = null;

        try {
            defaultCRSURI = new URI(annotation.defaultCRSString());
        } catch (URISyntaxException e) {
            LOG.error("Could not create URI from String: " + annotation.defaultCRSString());
            defaultCRSURI = URI.create("http://www.opengis.net/def/crs/EPSG/0/4326");
        }

        TypedProcessDescriptionFactory descriptionFactory = new TypedProcessDescriptionFactory();
        return descriptionFactory.boundingBoxOutput().withTitle(annotation.title()).withAbstract(annotation.abstrakt())
                .withIdentifier(annotation.identifier()).withDefaultCRS(new OwsCRS(defaultCRSURI)).build();
    }
}
