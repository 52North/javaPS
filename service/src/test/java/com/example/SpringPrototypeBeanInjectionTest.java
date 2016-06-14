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
package com.example;

import static junit.framework.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class SpringPrototypeBeanInjectionTest {


	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
	@Component
	public static class SomeRequest {}

	@Service
	public static class SomeService {

		@Autowired
		javax.inject.Provider<SomeRequest> someRequestProvider;

		SomeRequest doSomething() {
			return someRequestProvider.get();
		}
	}

	@ComponentScan("com.example")
	@Configuration
	public static class MyModuleConfig {}

	@Test
	public void shouldReturnANewPrototypeInstance() throws Exception {
		//given
		final ApplicationContext ctx = new AnnotationConfigApplicationContext(MyModuleConfig.class);

		//when
		final SomeRequest req1 = ctx.getBean(SomeRequest.class);
		final SomeRequest req2 = ctx.getBean(SomeRequest.class);

		//then
		assertTrue("New instance expected", req1 != req2);
	}

	@Test
	public void shouldReturnANewPrototypeInstanceFromAnInjectedProvider() throws Exception {
		//given
		final ApplicationContext ctx = new AnnotationConfigApplicationContext(MyModuleConfig.class);

		//when
		final SomeService someService = ctx.getBean(SomeService.class);

		final SomeRequest req1 = someService.doSomething();
		final SomeRequest req2 = someService.doSomething();

		//then
		assertTrue("New instance expected", req1 != req2);
	}
}
