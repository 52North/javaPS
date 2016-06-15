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
package org.n52.javaps.description.annotation.binding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class ExecuteMethodBinding extends AnnotationBinding<Method> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteMethodBinding.class);

    public ExecuteMethodBinding(Method method) {
        super(method);
    }

    @Override
    public boolean validate() {
        if (!checkModifier()) {
            LOGGER.error("Method {} with Execute annotation can't be used, not public.", getMember());
            return false;
        }
        // eh, do we really need to care about this?
        if (!getMember().getReturnType().equals(void.class)) {
            LOGGER.error("Method {} with Execute annotation can't be used, return type not void", getMember());
            return false;
        }
        if (getMember().getParameterTypes().length != 0) {
            LOGGER.error("Method {} with Execute annotation can't be used, method parameter count is > 0.", getMember());
            return false;
        }
        return true;
    }

    public void execute(Object annotatedInstance) {
        try {
            getMember().invoke(annotatedInstance);
        } catch (IllegalAccessException | IllegalArgumentException ex) {
            throw new RuntimeException("Internal error executing process", ex);
        } catch (InvocationTargetException ex) {
            Throwable cause = ex.getCause() == null ? ex : ex.getCause();
            throw new RuntimeException(cause.getMessage(), cause);
        }
    }

}
