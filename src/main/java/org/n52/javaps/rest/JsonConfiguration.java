package org.n52.javaps.rest;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JsonNodeFactory jsonNodeFactory() {
        return JsonNodeFactory.withExactBigDecimals(false);
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectReader objectReader(ObjectMapper objectMapper) {
        return objectMapper.reader();
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectWriter objectWriter(ObjectMapper objectMapper) {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        return objectMapper.writer(pp);
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper(JsonNodeFactory jsonNodeFactory) {
        return new ObjectMapper().setNodeFactory(jsonNodeFactory)
                                 .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }

}
