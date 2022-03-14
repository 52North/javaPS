/*
 * Copyright 2016-2022 52°North Spatial Information Research GmbH
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
package org.n52.javaps.utils;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class SpringContext implements Context, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public <T> T require(Class<T> type) {
        return applicationContext.getBean(type);
    }

    @Override
    public <T> Optional<? extends T> getInstance(Class<T> type) {
        try {
            return Optional.of(applicationContext.getBean(type));
        } catch (NoSuchBeanDefinitionException ex) {
            return Optional.empty();
        }
    }

    @Override
    public <T> Collection<T> getInstances(Class<T> type) {
        return applicationContext.getBeansOfType(type).values();
    }

    @Override
    public <T> Collection<Object> getAnnotatedInstances(Class<? extends Annotation> annotation) {
        return applicationContext.getBeansWithAnnotation(annotation).values();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = Objects.requireNonNull(applicationContext);
    }

}
