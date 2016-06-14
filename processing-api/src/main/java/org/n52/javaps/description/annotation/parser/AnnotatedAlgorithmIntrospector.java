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
package org.n52.javaps.description.annotation.parser;

import java.awt.im.spi.InputMethodDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.description.ComplexInputDescription;
import org.n52.javaps.description.ComplexOutputDescription;
import org.n52.javaps.description.LiteralInputDescription;
import org.n52.javaps.description.LiteralOutputDescription;
import org.n52.javaps.description.ProcessDescription;
import org.n52.javaps.description.ProcessDescriptionBuilder;
import org.n52.javaps.description.ProcessInputDescription;
import org.n52.javaps.description.ProcessOutputDescription;
import org.n52.javaps.description.annotation.Process;
import org.n52.javaps.description.annotation.binding.ExecuteMethodBinding;
import org.n52.javaps.description.annotation.binding.InputBinding;
import org.n52.javaps.description.annotation.binding.OutputBinding;
import org.n52.javaps.description.impl.ProcessDescriptionImpl;

/**
 *
 * @author tkunicki
 */
public class AnnotatedAlgorithmIntrospector {

    private final static List<InputAnnotationParser<?, Field, ? extends ProcessInputDescription, ?>> INPUT_FIELD_PARSERS;
    private final static List<InputAnnotationParser<?, Method, ? extends ProcessInputDescription, ?>> INPUT_METHOD_PARSERS;
    private final static List<OutputAnnotationParser<?, Field, ? extends ProcessOutputDescription, ?>> OUTPUT_FIELD_PARSERS;
    private final static List<OutputAnnotationParser<?, Method, ? extends ProcessOutputDescription, ?>> OUTPUT_METHOD_PARSERS;

    private final static ExecuteAnnotationParser PROCESS_PARSER;

    static {

        List<InputAnnotationParser<?, Method, ?, ?>> inputMethodParsers = new LinkedList<>();
        List<InputAnnotationParser<?, Field, ?, ?>> inputFieldParsers = new LinkedList<>();
        List<OutputAnnotationParser<?, Field, ?, ?>> outputFieldParsers = new LinkedList<>();
        List<OutputAnnotationParser<?, Method, ?, ?>> outputMethodParsers = new LinkedList<>();

        InputAnnotationParser<?, Method, LiteralInputDescription, ?> literalInputMethod = new LiteralDataInputAnnotationParser<>(asdf());
        InputAnnotationParser<?, Field, LiteralInputDescription, ?> literalInputField = new LiteralDataInputAnnotationParser<>(InputBinding::field);
        OutputAnnotationParser<?, Method, LiteralOutputDescription, ?> literalOutputMethod = new LiteralDataOutputAnnotationParser<>(OutputBinding::method);
        OutputAnnotationParser<?, Field, LiteralOutputDescription, ?> literalOutputField = new LiteralDataOutputAnnotationParser<>(OutputBinding::field);
        InputAnnotationParser<?, Method, ComplexInputDescription, ?> complexInputMethod = new ComplexDataInputAnnotationParser<>(asdf());
        InputAnnotationParser<?, Field, ComplexInputDescription, ?> complexInputField = new ComplexDataInputAnnotationParser<>(InputBinding::field);
        OutputAnnotationParser<?, Method, ComplexOutputDescription, ?> complexOutputMethod = new ComplexDataOutputAnnotationParser<>(OutputBinding::method);
        OutputAnnotationParser<?, Field, ComplexOutputDescription, ?> complexOutputField = new ComplexDataOutputAnnotationParser<>(OutputBinding::field);

        inputFieldParsers.add(literalInputField);
        inputFieldParsers.add(complexInputField);
        inputMethodParsers.add(literalInputMethod);
        inputMethodParsers.add(complexInputMethod);
        outputFieldParsers.add(literalOutputField);
        outputFieldParsers.add(complexOutputField);
        outputMethodParsers.add(literalOutputMethod);
        outputMethodParsers.add(complexOutputMethod);

        INPUT_FIELD_PARSERS = Collections.unmodifiableList(inputFieldParsers);
        INPUT_METHOD_PARSERS = Collections.unmodifiableList(inputMethodParsers);
        OUTPUT_FIELD_PARSERS = Collections.unmodifiableList(outputFieldParsers);
        OUTPUT_METHOD_PARSERS = Collections.unmodifiableList(outputMethodParsers);
        PROCESS_PARSER = new ExecuteAnnotationParser();
    }

    private static <D extends ProcessInputDescription> Function<Method, InputBinding<Method, D>> asdf() {
        return InputBinding::method;
    }

    private final static Map<Class<?>, AnnotatedAlgorithmIntrospector> INTROSPECTOR_MAP = new HashMap<>();

    public static synchronized AnnotatedAlgorithmIntrospector getInstrospector(Class<?> algorithmClass) {
        AnnotatedAlgorithmIntrospector introspector = INTROSPECTOR_MAP.get(algorithmClass);
        if (introspector == null) {
            introspector = new AnnotatedAlgorithmIntrospector(algorithmClass);
            INTROSPECTOR_MAP.put(algorithmClass, introspector);
        }
        return introspector;
    }

    private final Class<?> algorithmClass;
    private ProcessDescription algorithmDescriptor;
    private ExecuteMethodBinding executeMethodBinding;
    private Map<OwsCodeType, InputBinding<?, ?>> inputBindingMap;
    private Map<OwsCodeType, OutputBinding<?, ?>> outputBindingMap;

    public AnnotatedAlgorithmIntrospector(Class<?> algorithmClass) {

        this.algorithmClass = algorithmClass;

        inputBindingMap = new LinkedHashMap<>();
        outputBindingMap = new LinkedHashMap<>();

        parseClass();

        inputBindingMap = Collections.unmodifiableMap(inputBindingMap);
        outputBindingMap = Collections.unmodifiableMap(outputBindingMap);
    }

    private void parseClass() {

        if (!algorithmClass.isAnnotationPresent(Process.class)) {
            throw new RuntimeException("Class isn't annotated with an Algorithm annotation");
        }

        boolean validContructor = false;
        try {
            Constructor<?> defaultConstructor = algorithmClass.getConstructor();
            validContructor = (defaultConstructor.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC;
        } catch (NoSuchMethodException ex) {
            // inherit error message on fall through...
        } catch (SecurityException ex) {
            throw new RuntimeException("Current security policy limits use of reflection, error introspecting " + algorithmClass.getName());
        }
        if (!validContructor) {
            throw new RuntimeException("Classes with Algorithm annotation require public no-arg constructor, error introspecting " + algorithmClass.getName());
        }


        Process algorithm = algorithmClass.getAnnotation(Process.class);

        final ProcessDescriptionBuilder<?, ?> algorithmBuilder = ProcessDescriptionImpl.builder();
        algorithmBuilder.withIdentifier(algorithm.identifier().length() > 0 ? algorithm.identifier() : algorithmClass.getCanonicalName());

        algorithmBuilder.withTitle(algorithm.title())
                .withAbstract(algorithm.abstrakt())
                .withVersion(algorithm.version())
                .storeSupported(algorithm.storeSupported())
                .statusSupported(algorithm.statusSupported());

        parseElements(algorithmClass.getDeclaredMethods(), INPUT_METHOD_PARSERS, OUTPUT_METHOD_PARSERS);
        parseElements(algorithmClass.getDeclaredFields(), INPUT_FIELD_PARSERS, OUTPUT_FIELD_PARSERS);

        for (Method method : algorithmClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PROCESS_PARSER.getSupportedAnnotation())) {
                ExecuteMethodBinding executeMethodBinding = PROCESS_PARSER.parse(method);
                if (executeMethodBinding != null) {
                    if (this.executeMethodBinding != null) {
                        // we need to error out here because ordering of
                        // getDeclaredMethods() or
                        // getMethods() is not guarenteed to be consistent, if
                        // it were consistent
                        // maybe we could ignore this state, but having an
                        // algorithm behave
                        // differently betweeen runtimes would be bad...
                        throw new RuntimeException("Multiple execute method bindings encountered for class " + getClass().getCanonicalName());
                    }
                    this.executeMethodBinding = executeMethodBinding;
                }
            }
        }

        algorithmBuilder.withInput(inputBindingMap.values().stream().map(x -> x.getDescription()));
        algorithmBuilder.withOutput(outputBindingMap.values().stream().map(x -> x.getDescription()));
        algorithmDescriptor = algorithmBuilder.build();
    }

    public ProcessDescription getProcessDescription() {
        return algorithmDescriptor;
    }

    public ExecuteMethodBinding getExecuteMethodBinding() {
        return executeMethodBinding;
    }

    public Map<OwsCodeType, InputBinding<?, ?>> getInputBindingMap() {
        return Collections.unmodifiableMap(inputBindingMap);
    }

    public Map<OwsCodeType, OutputBinding<?, ?>> getOutputBindingMap() {
        return Collections.unmodifiableMap(outputBindingMap);
    }

    public <M extends AccessibleObject & Member> void parseElements(M members[],
            List<InputAnnotationParser<?, M, ?, ?>> inputParser,
            List<OutputAnnotationParser<?, M, ?, ?>> outputParser) {
        for (M member : members) {
            for (OutputAnnotationParser<?, M, ?, ?> parser : outputParser) {
                if (member.isAnnotationPresent(parser.getSupportedAnnotation())) {
                    OutputBinding<?, ?> binding = parser.parse(member);
                    if (binding != null) {
                        outputBindingMap.put(binding.getDescription().getId(), binding);
                    }
                }
            }
            for (InputAnnotationParser<?, M, ?, ?> parser : inputParser) {
                if (member.isAnnotationPresent(parser.getSupportedAnnotation())) {
                    InputBinding<?, ?> binding = parser.parse(member);
                    if (binding != null) {
                        inputBindingMap.put(binding.getDescription().getId(), binding);
                    }
                }
            }
        }
    }

}
