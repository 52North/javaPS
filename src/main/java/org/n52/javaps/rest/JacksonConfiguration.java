/*
 * Copyright (C) 2016 by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
public class JacksonConfiguration {

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
