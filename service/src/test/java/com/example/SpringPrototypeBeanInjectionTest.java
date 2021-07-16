/*
 * Copyright 2016-2021 52°North Spatial Information Research GmbH
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
package com.example;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import org.n52.iceland.config.spring.ProviderAwareListableBeanFactory;

public class SpringPrototypeBeanInjectionTest {
    @Component
    @Scope("prototype")
    public static class Prototype {
    }

    public static interface PrototypeInterface {
    }

    public static interface ListSingleton {
        List<PrototypeInterface> get();
    }

    @Component
    @Scope("prototype")
    public static class PrototypeImplementation1 implements PrototypeInterface {
    }

    @Component
    @Scope("prototype")
    public static class PrototypeImplementation2 implements PrototypeInterface {
    }

    @Service
    @Scope("singleton")
    public static class ListSingleton1 implements ListSingleton {
        @Inject
        Provider<Collection<PrototypeInterface>> provider;

        public List<PrototypeInterface> get() {
            return new LinkedList<>(provider.get());
        }
    }

    @Service
    @Scope("singleton")
    public static class ListSingleton2 implements ListSingleton {
        @Inject
        Collection<Provider<PrototypeInterface>> provider;

        public List<PrototypeInterface> get() {
            return provider.stream().map(Provider::get).collect(toList());
        }
    }

    @Service
    @Scope("singleton")
    public static class Singleton {
        @Inject
        Provider<Prototype> provider;

        Prototype get() {
            return provider.get();
        }
    }

    @Configuration
    @ComponentScan
    public static class Config {
    }

    private ApplicationContext ctx;

    @Before
    public void setup() {
        ProviderAwareListableBeanFactory beanFactory = new ProviderAwareListableBeanFactory();
        AnnotationConfigApplicationContext annotationConfigCtx
                = new AnnotationConfigApplicationContext(beanFactory);
        annotationConfigCtx.register(Config.class);
        annotationConfigCtx.refresh();
        this.ctx = annotationConfigCtx;
    }

    @Test
    public void shouldReturnANewPrototypeInstance()
            throws Exception {
        Prototype req1 = ctx.getBean(Prototype.class);
        Prototype req2 = ctx.getBean(Prototype.class);
        assertTrue("New instance expected", req1 != req2);
    }

    @Test
    public void shouldReturnANewPrototypeInstanceFromAnInjectedProvider()
            throws Exception {
        Singleton singleton = ctx.getBean(Singleton.class);
        Prototype req1 = singleton.get();
        Prototype req2 = singleton.get();
        assertTrue("New instance expected", req1 != req2);
    }

    @Test
    public void shouldReturnANewPrototypeInstanceFromAnInjectedListProvider()
            throws Exception {
        ListSingleton bean = ctx.getBean(ListSingleton1.class);

        List<PrototypeInterface> get1 = bean.get();
        List<PrototypeInterface> get2 = bean.get();

        assertTrue("New instance expected", get1.get(0) != get2.get(0));
        assertTrue("New instance expected", get1.get(1) != get2.get(1));
    }

}
