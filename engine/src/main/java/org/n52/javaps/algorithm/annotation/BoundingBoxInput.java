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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author tkunicki
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Inherited
@Documented
public @interface BoundingBoxInput {
    String identifier(); // identifier
    String title() default "";
    String abstrakt() default ""; // 'abstract' is java reserved keyword
    long minOccurs() default 1;
    long maxOccurs() default 1;
    String boundingBoxString() default "";
    String defaultCRSString() default "http://www.opengis.net/def/crs/EPSG/0/4326";
    String[] supportedCRSStringArray() default {"http://www.opengis.net/def/crs/EPSG/0/4326"};
}