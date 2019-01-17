/*
 * Copyright 2016-2019 52°North Initiative for Geospatial Open Source
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
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.n52.janmayen.stream.MoreCollectors;
import org.n52.janmayen.stream.Streams;
import org.n52.javaps.description.TypedProcessDescription;
import org.n52.javaps.description.TypedProcessInputDescription;
import org.n52.javaps.description.TypedProcessOutputDescription;
import org.n52.javaps.description.impl.TypedProcessDescriptionFactory;
import org.n52.javaps.io.InputHandlerRepository;
import org.n52.javaps.io.OutputHandlerRepository;
import org.n52.javaps.io.literal.LiteralTypeRepository;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.description.ProcessInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessOutputDescription;

import com.google.common.base.Strings;

/**
 *
 * @author Tom Kunicki, Christian Autermann
 */
public class AnnotatedAlgorithmMetadata {

    private static final String DUPLICATE_IDENTIFIER = "duplicated identifier: ";

    private final Class<?> algorithmClass;

    private final Map<OwsCode, AbstractOutputBinding<?>> outputBindings;

    private final Map<OwsCode, AbstractInputBinding<?>> inputBindings;

    private final ExecuteBinding executeBinding;

    private final TypedProcessDescription description;

    private final TypedProcessDescriptionFactory descriptionFactory;

    public AnnotatedAlgorithmMetadata(Class<?> algorithmClass, InputHandlerRepository parserRepository,
            OutputHandlerRepository generatorRepository, LiteralTypeRepository literalTypeRepository) {
        this.descriptionFactory = new TypedProcessDescriptionFactory();

        // this.formatRepository = Objects.requireNonNull(formatRepository);
        this.algorithmClass = Objects.requireNonNull(algorithmClass);

        checkDefaultConstructor(algorithmClass);

        this.executeBinding = getExecuteBinding(algorithmClass);
        this.inputBindings = getInputBindings(algorithmClass, parserRepository, literalTypeRepository);
        this.outputBindings = getOutputBindings(algorithmClass, generatorRepository, literalTypeRepository);
        this.description = getDescription(algorithmClass, this.inputBindings, this.outputBindings);
    }

    public Class<?> getAlgorithmClass() {
        return this.algorithmClass;
    }

    Map<OwsCode, AbstractOutputBinding<?>> getOutputBindings() {
        return Collections.unmodifiableMap(this.outputBindings);
    }

    private Map<OwsCode, AbstractOutputBinding<?>> getOutputBindings(Class<?> algorithmClass,
            OutputHandlerRepository generatorRepository,
            LiteralTypeRepository literalTypeRepository) {
        Stream<AbstractOutputBinding<Field>> s1 = this.parseElements(getFields(algorithmClass),
                Arrays.<AbstractOutputAnnotationParser<?, Field, AbstractOutputBinding<Field>>>asList(
                new LiteralOutputAnnotationParser<>(AbstractOutputBinding::field, literalTypeRepository),
                new ComplexOutputAnnotationParser<>(AbstractOutputBinding::field, generatorRepository),
                new BoundingBoxOutputAnnotationParser<>(AbstractOutputBinding::field)))
                .map(x -> (AbstractOutputBinding<Field>) x);
        Stream<AbstractOutputBinding<Method>> s2 = parseElements(getMethods(algorithmClass),
                Arrays.<AbstractOutputAnnotationParser<?, Method, AbstractOutputBinding<Method>>>asList(
                new LiteralOutputAnnotationParser<>(AbstractOutputBinding::method, literalTypeRepository),
                new ComplexOutputAnnotationParser<>(AbstractOutputBinding::method, generatorRepository),
                new BoundingBoxOutputAnnotationParser<>(AbstractOutputBinding::method)))
                .map(x -> (AbstractOutputBinding<Method>) x);

        BinaryOperator<AbstractOutputBinding<?>> merger = org.n52.janmayen.stream.Streams.throwingMerger((a,
                b) -> new RuntimeException(DUPLICATE_IDENTIFIER  + a.getDescription().getId()));
        Collector<AbstractOutputBinding<?>, ?, Map<OwsCode, AbstractOutputBinding<?>>> collector =
                java.util.stream.Collectors.toMap(b -> b.getDescription().getId(), java.util.function.Function
                        .identity(), merger, LinkedHashMap::new);
        return Collections.unmodifiableMap(Stream.concat(s1, s2).collect(collector));
    }

    Map<OwsCode, AbstractInputBinding<?>> getInputBindings() {
        return Collections.unmodifiableMap(this.inputBindings);
    }

    private Map<OwsCode, AbstractInputBinding<?>> getInputBindings(Class<?> algorithmClass,
            InputHandlerRepository parserRepository,
            LiteralTypeRepository literalTypeRepository) {
        Stream<AbstractInputBinding<Field>> s1 = parseElements(getFields(algorithmClass),
                Arrays.<AbstractInputAnnotationParser<?, Field, AbstractInputBinding<Field>>>asList(
                new LiteralInputAnnotationParser<>(AbstractInputBinding::field, literalTypeRepository),
                new ComplexInputAnnotationParser<>(AbstractInputBinding::field, parserRepository),
                new BoundingBoxInputAnnotationParser<>(AbstractInputBinding::field)))
                .map(x -> (AbstractInputBinding<Field>) x);
        Stream<AbstractInputBinding<Method>> s2 = parseElements(getMethods(algorithmClass),
                Arrays.<AbstractInputAnnotationParser<?, Method, AbstractInputBinding<Method>>>asList(
                new LiteralInputAnnotationParser<>(AbstractInputBinding::method, literalTypeRepository),
                new ComplexInputAnnotationParser<>(AbstractInputBinding::method, parserRepository),
                new BoundingBoxInputAnnotationParser<>(AbstractInputBinding::method)))
                .map(x -> (AbstractInputBinding<Method>) x);
        BinaryOperator<AbstractInputBinding<?>> merger = Streams.throwingMerger((a,
                b) -> new RuntimeException(DUPLICATE_IDENTIFIER + a.getDescription().getId()));
        Collector<AbstractInputBinding<?>, ?, LinkedHashMap<OwsCode, AbstractInputBinding<?>>> collector =
                java.util.stream.Collectors.toMap(b -> b.getDescription().getId(), java.util.function.Function
                        .identity(), merger, LinkedHashMap::new);
        return Collections.unmodifiableMap(Stream.concat(s1, s2).collect(collector));
    }

    ExecuteBinding getExecuteBinding() {
        return this.executeBinding;
    }

    private ExecuteBinding getExecuteBinding(Class<?> algorithmClass) {
        ExecuteAnnotationParser parser = new ExecuteAnnotationParser();
        return getMethods(algorithmClass).filter(method -> method.isAnnotationPresent(parser.getSupportedAnnotation()))
                .map(method -> parser.parse(method)).filter(Objects::nonNull).collect(MoreCollectors.toSingleResult(
                        () -> new RuntimeException("Multiple execute method bindings encountered for class "
                                + algorithmClass.getCanonicalName())));
    }

    public TypedProcessDescription getDescription() {
        return this.description;
    }

    private TypedProcessDescription getDescription(Class<?> algorithmClass,
            Map<OwsCode, AbstractInputBinding<?>> inputBindings,
            Map<OwsCode, AbstractOutputBinding<?>> outputBindings) {
        Function<AbstractInputBinding<?>, TypedProcessInputDescription<?>> getInputDescription =
                AbstractInputBinding::getDescription;
        List<ProcessInputDescription> inputs = inputBindings.values().stream().map(getInputDescription).collect(
                java.util.stream.Collectors.toList());
        Function<AbstractOutputBinding<?>, TypedProcessOutputDescription<?>> getOutputDescription =
                AbstractOutputBinding::getDescription;
        List<ProcessOutputDescription> outputs = outputBindings.values().stream().map(getOutputDescription).collect(
                java.util.stream.Collectors.toList());
        return getDescription(algorithmClass, inputs, outputs);
    }

    private TypedProcessDescription getDescription(Class<?> algorithmClass,
            List<ProcessInputDescription> inputs,
            List<ProcessOutputDescription> outputs) {
        org.n52.javaps.algorithm.annotation.Algorithm annotation = getProcessAnnotation(algorithmClass);
        String identifier = Strings.emptyToNull(annotation.identifier()) == null ? algorithmClass.getCanonicalName()
                : annotation.identifier();
        return descriptionFactory.process().withIdentifier(identifier).withTitle(annotation.title()).withAbstract(
                annotation.abstrakt()).withVersion(annotation.version()).storeSupported(annotation.storeSupported())
                .statusSupported(annotation.statusSupported()).withInput(inputs).withOutput(outputs).build();
    }

    public <M extends AccessibleObject & Member, B extends AbstractDataBinding<? super M, ?>> Stream<
            ? extends B> parseElements(Stream<M> members,
                    List<? extends AnnotationParser<?, M, ? extends B>> outputParser) {
        return members.flatMap(member -> outputParser.stream().filter(parser -> member.isAnnotationPresent(parser
                .getSupportedAnnotation())).map(parser -> parser.parse(member)).filter(Objects::nonNull));
    }

    private void checkDefaultConstructor(Class<?> algorithmClass) throws RuntimeException {
        try {
            Constructor<?> defaultConstructor = algorithmClass.getConstructor();

            if (!Modifier.isPublic(defaultConstructor.getModifiers())) {
                throw new RuntimeException(
                        "Classes with Algorithm annotation require public no-arg constructor, error introspecting "
                                + algorithmClass.getName());
            }
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException("Current security policy limits use of reflection, error introspecting "
                    + algorithmClass.getName());
        }
    }

    private org.n52.javaps.algorithm.annotation.Algorithm getProcessAnnotation(Class<?> algorithmClass1)
            throws RuntimeException {
        org.n52.javaps.algorithm.annotation.Algorithm annotation = algorithmClass1.getAnnotation(
                org.n52.javaps.algorithm.annotation.Algorithm.class);
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
        return Stream.concat(Stream.of(c.getSuperclass()), Arrays.stream(c.getInterfaces())).filter(Objects::nonNull)
                .flatMap(this::getSuperTypeStream).distinct();
    }

    private Stream<Class<?>> asClassStream(Class<?> clazz) {
        return Stream.concat(Stream.of(clazz), getSuperTypeStream(clazz));
    }

}
