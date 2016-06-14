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
package org.n52.javaps.description.annotation.parser;

import java.lang.reflect.Method;

import org.n52.javaps.description.annotation.Execute;
import org.n52.javaps.description.annotation.binding.ExecuteMethodBinding;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ExecuteAnnotationParser implements AnnotationParser<Execute, Method, ExecuteMethodBinding> {
    @Override
    public ExecuteMethodBinding parse(Execute annotation, Method member) {
        ExecuteMethodBinding annotationBinding = new ExecuteMethodBinding(member);
        return annotationBinding.validate() ? annotationBinding : null;
    }

    @Override
    public Class<? extends Execute> getSupportedAnnotation() {
        return Execute.class;
    }

}
