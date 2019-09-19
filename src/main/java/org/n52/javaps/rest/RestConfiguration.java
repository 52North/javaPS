package org.n52.javaps.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.util.Properties;

@Configuration
public class RestConfiguration {

    @Bean
    public UrlBasedViewResolver viewResolver() {
        UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public ContentNegotiationManagerFactoryBean cnManager() {
        ContentNegotiationManagerFactoryBean cnManager = new ContentNegotiationManagerFactoryBean();
        cnManager.setFavorPathExtension(true);
        cnManager.setDefaultContentType(MediaType.TEXT_HTML);
        Properties mapping = new Properties();
        mapping.setProperty("html", MediaTypes.TEXT_HTML);
        mapping.setProperty("json", MediaTypes.APPLICATION_JSON);
        mapping.setProperty("xml", MediaTypes.APPLICATION_XML);
        mapping.setProperty("js", MediaTypes.APPLICATION_JAVASCRIPT);
        cnManager.setMediaTypes(mapping);
        return cnManager;
    }

}
