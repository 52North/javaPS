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
package org.n52.javaps.algorithm.annotation;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.n52.javaps.utils.Streams.throwingMerger;
import static org.n52.javaps.utils.Streams.toSingleResult;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.n52.javaps.description.ProcessDescription;
import org.n52.javaps.description.ProcessInputDescription;
import org.n52.javaps.description.ProcessOutputDescription;
import org.n52.javaps.description.impl.ProcessDescriptionImpl;
import org.n52.javaps.io.GeneratorRepository;
import org.n52.javaps.io.ParserRepository;
import org.n52.javaps.ogc.ows.OwsCode;

import com.google.common.base.Strings;

/**
 *
 * @author Tom Kunicki, Christian Autermann
 */
public class AnnotatedAlgorithmMetadata {
    private final Class<?> algorithmClass;

    private final Map<OwsCode, AbstractOutputBinding<?, ?>> outputBindings;
    private final Map<OwsCode, AbstractInputBinding<?, ?>> inputBindings;
    private final ExecuteBinding executeBinding;
    private final ProcessDescription description;

    public AnnotatedAlgorithmMetadata(Class<?> algorithmClass, ParserRepository parserRepository, GeneratorRepository generatorRepository) {

        //this.formatRepository = Objects.requireNonNull(formatRepository);
        this.algorithmClass = Objects.requireNonNull(algorithmClass);

        checkDefaultConstructor(algorithmClass);

        this.executeBinding = getExecuteBinding(algorithmClass);
        this.inputBindings = getInputBindings(algorithmClass, parserRepository);
        this.outputBindings = getOutputBindings(algorithmClass, generatorRepository);
        this.description = getDescription(algorithmClass, this.inputBindings, this.outputBindings);
    }

    public Class<?> getAlgorithmClass() {
        return this.algorithmClass;
    }

    Map<OwsCode, AbstractOutputBinding<?, ?>> getOutputBindings() {
        return Collections.unmodifiableMap(this.outputBindings);
    }

    Map<OwsCode, AbstractInputBinding<?, ?>> getInputBindings() {
        return Collections.unmodifiableMap(this.inputBindings);
    }

    ExecuteBinding getExecuteBinding() {
        return this.executeBinding;
    }

    public ProcessDescription getDescription() {
        return this.description;
    }

    public <M extends AccessibleObject & Member, B extends AbstractDataBinding<? super M, ?>> Stream<? extends B> parseElements(
            Stream<M> members, List<? extends AnnotationParser<?, M, ? extends B>> outputParser) {
        return members.flatMap(member -> outputParser.stream()
                .filter(parser -> member.isAnnotationPresent(parser.getSupportedAnnotation()))
                .map(parser -> parser.parse(member))
                .filter(Objects::nonNull));
    }

    private void checkDefaultConstructor(Class<?> algorithmClass)
            throws RuntimeException {
        try {
            Constructor<?> defaultConstructor = algorithmClass.getConstructor();

            if (!Modifier.isPublic(defaultConstructor.getModifiers())) {
                throw new RuntimeException("Classes with Algorithm annotation require public no-arg constructor, error introspecting "
                                           + algorithmClass.getName());
            }
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException("Current security policy limits use of reflection, error introspecting "
                                       + algorithmClass.getName());
        }
    }

    private Map<OwsCode, AbstractInputBinding<?, ?>> getInputBindings(Class<?> algorithmClass, ParserRepository parserRepository) {
        Stream<AbstractInputBinding<?, ?>> s1 = parseElements(getFields(algorithmClass), Arrays
                .asList(new LiteralInputAnnotationParser<>(AbstractInputBinding::field),
                        new ComplexInputAnnotationParser<>(AbstractInputBinding::field, parserRepository)))
                .map(x -> (AbstractInputBinding<?,?>)x);
        Stream<AbstractInputBinding<?, ?>> s2 = parseElements(getMethods(algorithmClass), Arrays
                .asList(new LiteralInputAnnotationParser<>(AbstractInputBinding::method),
                        new ComplexInputAnnotationParser<>(AbstractInputBinding::method, parserRepository)))
                .map(x -> (AbstractInputBinding<?,?>)x);
        BinaryOperator<AbstractInputBinding<?, ?>> merger = throwingMerger((a, b) ->
                new RuntimeException("duplicated identifier: " + a.getDescription().getId()));
        Collector<AbstractInputBinding<?, ?>, ?, LinkedHashMap<OwsCode, AbstractInputBinding<?, ?>>> collector
                = toMap(b -> b.getDescription().getId(), identity(), merger, LinkedHashMap::new);
        return Collections.unmodifiableMap(Stream.concat(s1, s2).collect(collector));
    }

    private Map<OwsCode, AbstractOutputBinding<?, ?>> getOutputBindings(Class<?> algorithmClass, GeneratorRepository generatorRepository) {
        Stream<AbstractOutputBinding<?, ?>> s1 = parseElements(getFields(algorithmClass), Arrays
                    .asList(new LiteralOutputAnnotationParser<>(AbstractOutputBinding::field),
                            new ComplexOutputAnnotationParser<>(AbstractOutputBinding::field, generatorRepository)))
                .map(x -> (AbstractOutputBinding<?,?>)x);
        Stream<AbstractOutputBinding<?, ?>> s2 = parseElements(getMethods(algorithmClass), Arrays
                    .asList(new LiteralOutputAnnotationParser<>(AbstractOutputBinding::method),
                            new ComplexOutputAnnotationParser<>(AbstractOutputBinding::method, generatorRepository)))
                .map(x -> (AbstractOutputBinding<?,?>)x);
        BinaryOperator<AbstractOutputBinding<?, ?>> merger = throwingMerger((a, b) ->
                new RuntimeException("duplicated identifier: " + a.getDescription().getId()));
        Collector<AbstractOutputBinding<?, ?>, ?, Map<OwsCode, AbstractOutputBinding<?, ?>>> collector
                = toMap(b -> b.getDescription().getId(), identity(), merger, LinkedHashMap::new);
        return Collections.unmodifiableMap(Stream.concat(s1, s2).collect(collector));
    }

    private ExecuteBinding getExecuteBinding(Class<?> algorithmClass) {
        ExecuteAnnotationParser parser = new ExecuteAnnotationParser();
        return getMethods(algorithmClass)
                .filter(method -> method.isAnnotationPresent(parser.getSupportedAnnotation()))
                .map(method -> parser.parse(method))
                .filter(Objects::nonNull)
                .collect(toSingleResult(() ->
                        new RuntimeException("Multiple execute method bindings encountered for class "
                                             + algorithmClass.getCanonicalName())));
    }

    private ProcessDescription getDescription(Class<?> algorithmClass, Map<OwsCode, AbstractInputBinding<?, ?>> inputBindings, Map<OwsCode, AbstractOutputBinding<?, ?>> outputBindings) {
        List<ProcessInputDescription> inputs = inputBindings.values().stream().map(AbstractInputBinding::getDescription).collect(toList());
        List<ProcessOutputDescription> outputs = outputBindings.values().stream().map(AbstractOutputBinding::getDescription).collect(toList());
        return getDescription(algorithmClass, inputs, outputs);
    }

    private ProcessDescription getDescription(Class<?> algorithmClass,
            List<ProcessInputDescription> inputs, List<ProcessOutputDescription> outputs) {
        org.n52.javaps.algorithm.annotation.Algorithm annotation = getProcessAnnotation(algorithmClass);
        String identifier = Strings.emptyToNull(annotation.identifier()) == null
                            ? algorithmClass.getCanonicalName() : annotation.identifier();
        return ProcessDescriptionImpl.builder()
                        .withIdentifier(identifier)
                        .withTitle(annotation.title())
                        .withAbstract(annotation.abstrakt())
                        .withVersion(annotation.version())
                        .storeSupported(annotation.storeSupported())
                        .statusSupported(annotation.statusSupported())
                        .withInput(inputs)
                        .withOutput(outputs)
                        .build();
    }

    private org.n52.javaps.algorithm.annotation.Algorithm getProcessAnnotation(Class<?> algorithmClass1)
            throws RuntimeException {
        org.n52.javaps.algorithm.annotation.Algorithm annotation = algorithmClass1.getAnnotation(org.n52.javaps.algorithm.annotation.Algorithm.class);
        if (annotation == null) {
            throw new RuntimeException("Class isn't annotated with an Algorithm annotation");
        }
        return annotation;
    }

    private Stream<Method> getMethods(Class<?> clazz) {
        return asClassStream(clazz).map(Class::getDeclaredMethods).flatMap(Arrays::stream);
    }

    private Stream<Field> getFields(Class<?> clazz) {
        return asClassStream(clazz).map(Class::getDeclaredFields).flatMap(Arrays::stream);
    }

    private Stream<Class<?>> getSuperTypeStream(Class<?> c) {
        return Stream.concat(Stream.of(c.getSuperclass()),
                             Arrays.stream(c.getInterfaces()))
                .filter(Objects::nonNull)
                .flatMap(this::getSuperTypeStream)
                .distinct();
    }

    private Stream<Class<?>> asClassStream(Class<?> clazz) {
        return Stream.concat(Stream.of(clazz), getSuperTypeStream(clazz));
    }

}