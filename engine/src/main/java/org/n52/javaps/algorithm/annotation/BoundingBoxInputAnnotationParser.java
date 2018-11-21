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
package org.n52.javaps.algorithm.annotation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Function;

import org.n52.javaps.description.TypedBoundingBoxInputDescription;
import org.n52.javaps.description.impl.TypedProcessDescriptionFactory;
import org.n52.shetland.ogc.ows.OwsCRS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <M>
 *            the accessible member type
 * @param <B>
 *            the binding type
 */
class BoundingBoxInputAnnotationParser<M extends AccessibleObject & Member, B extends AbstractInputBinding<M>> extends
        AbstractInputAnnotationParser<BoundingBoxInput, M, B> {

    private static final Logger LOG = LoggerFactory.getLogger(BoundingBoxInputAnnotationParser.class);

    BoundingBoxInputAnnotationParser(Function<M, B> bindingFunction) {
        super(bindingFunction);
    }

    @Override
    protected TypedBoundingBoxInputDescription createDescription(BoundingBoxInput annotation,
            B binding) {

        URI defaultCRSURI = null;

        try {
            defaultCRSURI = new URI(annotation.defaultCRSString());
        } catch (URISyntaxException e) {
            LOG.error("Could not create URI from String: " + annotation.defaultCRSString());
            defaultCRSURI = URI.create("http://www.opengis.net/def/crs/EPSG/0/4326");
        }

        // TODO add supported CRSs

        return new TypedProcessDescriptionFactory().boundingBoxInput().withIdentifier(annotation.identifier())
                .withAbstract(annotation.abstrakt()).withTitle(annotation.title()).withMinimalOccurence(annotation
                        .minOccurs()).withMaximalOccurence(annotation.maxOccurs()).withDefaultCRS(new OwsCRS(
                                defaultCRSURI)).build();
    }

    @Override
    public Class<? extends BoundingBoxInput> getSupportedAnnotation() {
        return BoundingBoxInput.class;
    }
}
