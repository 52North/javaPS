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
package org.n52.javaps.io.literal;

import org.n52.javaps.io.literal.xsd.Day;
import org.n52.javaps.io.literal.xsd.LiteralAnyURIType;
import org.n52.javaps.io.literal.xsd.LiteralBase64BinaryType;
import org.n52.javaps.io.literal.xsd.LiteralBooleanType;
import org.n52.javaps.io.literal.xsd.LiteralByteType;
import org.n52.javaps.io.literal.xsd.LiteralDateTimeType;
import org.n52.javaps.io.literal.xsd.LiteralDateType;
import org.n52.javaps.io.literal.xsd.LiteralDayType;
import org.n52.javaps.io.literal.xsd.LiteralDecimalType;
import org.n52.javaps.io.literal.xsd.LiteralDoubleType;
import org.n52.javaps.io.literal.xsd.LiteralDurationType;
import org.n52.javaps.io.literal.xsd.LiteralFloatType;
import org.n52.javaps.io.literal.xsd.LiteralIntType;
import org.n52.javaps.io.literal.xsd.LiteralIntegerType;
import org.n52.javaps.io.literal.xsd.LiteralLanguageType;
import org.n52.javaps.io.literal.xsd.LiteralLongType;
import org.n52.javaps.io.literal.xsd.LiteralMonthDayType;
import org.n52.javaps.io.literal.xsd.LiteralMonthType;
import org.n52.javaps.io.literal.xsd.LiteralShortType;
import org.n52.javaps.io.literal.xsd.LiteralStringType;
import org.n52.javaps.io.literal.xsd.LiteralTimeType;
import org.n52.javaps.io.literal.xsd.LiteralYearMonthType;
import org.n52.javaps.io.literal.xsd.LiteralYearType;
import org.n52.javaps.utils.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings({"rawtypes", "unchecked"})
public class LiteralTypeRepositoryImpl implements LiteralTypeRepository {

    private static final Logger LOG = LoggerFactory.getLogger(LiteralTypeRepositoryImpl.class);

    private final Context context;

    private final Map<Class<?>, Class<? extends LiteralType<?>>> mappings = new HashMap<>();

    @Inject
    public LiteralTypeRepositoryImpl(Context context) {
        this.context = context;

        // register the default types to avoid ambiguity (and to speed things
        // up...)
        registerMapping(BigDecimal.class, LiteralDecimalType.class);
        registerMapping(BigInteger.class, LiteralIntegerType.class);
        // registerMapping(BigInteger.class, LiteralNegativeIntegerType.class);
        // registerMapping(BigInteger.class,
        // LiteralNonNegativeIntegerType.class);
        // registerMapping(BigInteger.class,
        // LiteralNonPositiveIntegerType.class);
        // registerMapping(BigInteger.class, LiteralPositiveIntegerType.class);
        // registerMapping(BigInteger.class, LiteralUnsignedLongType.class);
        registerMapping(Boolean.class, LiteralBooleanType.class);
        registerMapping(Byte.class, LiteralByteType.class);
        registerMapping(byte[].class, LiteralBase64BinaryType.class);
        // registerMapping(byte[].class, LiteralHexBinaryType.class);
        registerMapping(Day.class, LiteralDayType.class);
        registerMapping(Double.class, LiteralDoubleType.class);
        registerMapping(Duration.class, LiteralDurationType.class);
        registerMapping(float.class, LiteralFloatType.class);
        registerMapping(Integer.class, LiteralIntType.class);
        // registerMapping(Integer.class, LiteralUnsignedShortType.class);
        registerMapping(LocalDate.class, LiteralDateType.class);
        registerMapping(LocalDateTime.class, LiteralDateTimeType.class);
        registerMapping(Locale.class, LiteralLanguageType.class);
        registerMapping(LocalTime.class, LiteralTimeType.class);
        registerMapping(Long.class, LiteralLongType.class);
        // registerMapping(Long.class, LiteralUnsignedIntType.class);
        registerMapping(Month.class, LiteralMonthType.class);
        registerMapping(MonthDay.class, LiteralMonthDayType.class);
        registerMapping(Short.class, LiteralShortType.class);
        // registerMapping(Short.class, LiteralUnsignedByteType.class);
        // registerMapping(String.class, LiteralAnySimpleType.class);
        // registerMapping(String.class, LiteralNormalizedStringType.class);
        registerMapping(String.class, LiteralStringType.class);
        registerMapping(URI.class, LiteralAnyURIType.class);
        registerMapping(Year.class, LiteralYearType.class);
        registerMapping(YearMonth.class, LiteralYearMonthType.class);
    }

    public final <T> void registerMapping(Class<T> bindingPayloadType, Class<? extends LiteralType<T>> literalType) {
        this.mappings.put(bindingPayloadType, literalType);
    }

    @Override
    public <T> LiteralType<T> getLiteralType(Class<? extends LiteralType<?>> literalType, Class<?> payloadType) {
        Optional<LiteralType> type;
        if (literalType != null && !literalType.equals(LiteralType.class)) {
            type = getLiteralTypeForLiteralType(literalType);
        } else if (payloadType != null) {
            type = getLiteralTypeForPayloadType(payloadType);
        } else {
            type = Optional.empty();
        }
        if (!type.isPresent()) {
            LOG.error("Could not find literal type for literalType {} and payloadType {}", literalType, payloadType);
        }
        return type.orElse(null);
    }

    @Override
    public Optional<LiteralType<?>> getLiteralType(String name) {
        return context.getInstances(LiteralType.class).stream()
                      .filter(i -> i.getName().equals(name))
                      .findFirst()
                      .map(t -> (LiteralType<?>) t);
    }

    @Override
    public Optional<LiteralType<?>> getLiteralType(URI name) {
        return context.getInstances(LiteralType.class).stream()
                      .filter(i -> i.getURI().equals(name))
                      .findFirst()
                      .map(t -> (LiteralType<?>) t);
    }

    private Optional<LiteralType> getLiteralTypeForPayloadType(Class<?> payloadType) {
        if (this.mappings.containsKey(payloadType)) {
            Class<?> typeClass = this.mappings.get(payloadType);
            return (Optional<LiteralType>) context.getInstance(typeClass);
        } else {
            return context.getInstances(LiteralType.class).stream()
                          .filter(i -> i.getPayloadType().equals(payloadType))
                          .findFirst();
        }
    }

    private Optional<LiteralType> getLiteralTypeForLiteralType(Class<?> literalType) {
        return (Optional<LiteralType>) context.getInstance(literalType);
    }

}
