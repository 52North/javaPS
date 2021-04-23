/*
 * Copyright 2016-2021 52°North Initiative for Geospatial Open Source
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
import java.util.ArrayList;
import java.util.List;
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
    private static final String COULD_NOT_CREATE_URI_FROM_STRING = "Could not create URI from String: ";

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
            LOG.error(COULD_NOT_CREATE_URI_FROM_STRING + annotation.defaultCRSString());
            defaultCRSURI = URI.create("http://www.opengis.net/def/crs/EPSG/0/4326");
        }

        List<OwsCRS> supportedCRSList = new ArrayList<>();

        String [] supportedCRSArray = annotation.supportedCRSStringArray();

        for (String crsString : supportedCRSArray) {
            try {
                supportedCRSList.add(new OwsCRS(new URI(crsString)));
            } catch (URISyntaxException e) {
                LOG.error(COULD_NOT_CREATE_URI_FROM_STRING + crsString);
            }
        }
        // TODO add supported CRSs

        return new TypedProcessDescriptionFactory().boundingBoxInput().withIdentifier(annotation.identifier())
                .withAbstract(annotation.abstrakt()).withTitle(annotation.title()).withMinimalOccurence(annotation
                        .minOccurs()).withMaximalOccurence(annotation.maxOccurs()).withDefaultCRS(new OwsCRS(
                                defaultCRSURI)).withSupportedCRS(supportedCRSList).build();
    }

    @Override
    public Class<? extends BoundingBoxInput> getSupportedAnnotation() {
        return BoundingBoxInput.class;
    }
}
