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
