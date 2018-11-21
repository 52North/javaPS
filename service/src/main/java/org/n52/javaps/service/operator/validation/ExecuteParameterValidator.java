/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.service.operator.validation;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.InputOccurence;
import org.n52.shetland.ogc.wps.OutputDefinition;
import org.n52.shetland.ogc.wps.ResponseMode;
import org.n52.shetland.ogc.wps.data.FormattedProcessData;
import org.n52.shetland.ogc.wps.data.GroupProcessData;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.shetland.ogc.wps.data.ReferenceProcessData;
import org.n52.shetland.ogc.wps.data.ValueProcessData;
import org.n52.shetland.ogc.wps.description.BoundingBoxInputDescription;
import org.n52.shetland.ogc.wps.description.BoundingBoxOutputDescription;
import org.n52.shetland.ogc.wps.description.ComplexInputDescription;
import org.n52.shetland.ogc.wps.description.ComplexOutputDescription;
import org.n52.shetland.ogc.wps.description.GroupInputDescription;
import org.n52.shetland.ogc.wps.description.GroupOutputDescription;
import org.n52.shetland.ogc.wps.description.LiteralInputDescription;
import org.n52.shetland.ogc.wps.description.LiteralOutputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescription;
import org.n52.shetland.ogc.wps.description.ProcessInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessInputDescriptionContainer;
import org.n52.shetland.ogc.wps.description.ProcessOutputDescription;
import org.n52.shetland.ogc.wps.description.ProcessOutputDescriptionContainer;
import org.n52.iceland.request.operator.ParameterValidator;
import org.n52.janmayen.Chain;
import org.n52.janmayen.stream.MoreCollectors;
import org.n52.javaps.engine.Engine;
import org.n52.javaps.io.bbox.BoundingBoxInputOutputHandler;
import org.n52.javaps.io.literal.LiteralInputOutputHandler;
import org.n52.shetland.ogc.wps.request.ExecuteRequest;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.exception.CompositeOwsException;
import org.n52.shetland.ogc.ows.exception.InvalidParameterValueException;
import org.n52.shetland.ogc.ows.exception.MissingParameterValueException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

/**
 * @author Christian Autermann
 */
public class ExecuteParameterValidator implements ParameterValidator<ExecuteRequest> {
    private static final String INPUT = "Input";

    private static final String IDENTIFIER = "Identifier";

    private static final String OUTPUT = "Output";

    private static final String VALUE_INVALID = "The value '%s' of the parameter '%s' is invalid: %s";

    private static final String UNSUPPORTED_FORMAT = "unsupported format";

    private final Engine engine;

    @Inject
    public ExecuteParameterValidator(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void validate(ExecuteRequest request) throws OwsExceptionReport {

        CompositeOwsException exception = new CompositeOwsException();

        if (request.getId() == null) {
            exception.add(new MissingParameterValueException(IDENTIFIER));
        } else {
            ProcessDescription description = engine.getProcessDescription(request.getId()).orElse(null);

            if (description == null) {
                exception.add(new InvalidParameterValueException(IDENTIFIER, request.getId().getValue()));
            } else {
                try {
                    validate(request, description);
                } catch (OwsExceptionReport ex) {
                    exception.add(ex);
                }
            }
        }

        exception.throwIfNotEmpty();
    }

    private void validate(ExecuteRequest request,
            ProcessDescription description) throws OwsExceptionReport {
        CompositeOwsException exception = new CompositeOwsException();

        if (request.getResponseMode() == ResponseMode.RAW && (request.getOutputs().size() > 1 || (request.getOutputs()
                .isEmpty() && description.getOutputs().size() > 1))) {
            exception.add(new InvalidParameterValueException().at("responseMode").withMessage(
                    "The value 'raw' of the parameter 'responseMode' is invalid. Single output is required."));
        }

        try {
            validateInputs(request.getInputs(), description);
        } catch (OwsExceptionReport ex) {
            exception.add(ex);
        }

        try {
            validateOutputs(request.getOutputs(), description);
        } catch (OwsExceptionReport ex) {
            exception.add(ex);
        }

        try {
            validateCardinalities(request.getInputs(), description);
        } catch (OwsExceptionReport ex) {
            exception.add(ex);
        }

        exception.throwIfNotEmpty();
    }

    private void validateCardinalities(List<ProcessData> inputs,
            ProcessDescription description) throws OwsExceptionReport {

        CompositeOwsException exception = new CompositeOwsException();

        InputOccurenceCollector collector = new InputOccurenceCollector();

        Map<Chain<OwsCode>, BigInteger> cardinalities = inputs.stream().collect(MoreCollectors.toCardinalities(
                ProcessData::getId, ProcessData::isGroup, x -> x.asGroup().stream()));

        Map<Chain<OwsCode>, InputOccurence> occurences = description.getInputDescriptions().stream().map(input -> input
                .visit(collector)).collect(HashMap::new, Map::putAll, Map::putAll);

        // check the cardinalities of existing inputs
        // ignore if there is no cardinality, as this
        // will be catched by another method
        cardinalities.forEach((chain,
                cardinality) ->
        Optional.ofNullable(occurences.get(chain)).filter(occurence -> !occurence.isInBounds(cardinality)).ifPresent(
                occurence -> exception.add(new InvalidParameterValueException().at(INPUT).withMessage(
                        "The input %s has an invalid cardinality of %s; should be in %s.", chain.toString(),
                        cardinality, occurence))));

        // check for missing inputs
        occurences.forEach((chain,
                occurence) -> {
            if (occurence.isRequired() && !cardinalities.containsKey(chain)) {
                exception.add(new MissingParameterValueException().at(INPUT).withMessage("The input %s is required",
                        chain.toString()));
            }
        });

        exception.throwIfNotEmpty();
    }

    private void validateInputs(List<ProcessData> inputs,
            ProcessInputDescriptionContainer descriptions) throws OwsExceptionReport {
        CompositeOwsException exception = new CompositeOwsException();
        inputs.stream().forEach(input -> {
            try {
                validateInput(input, descriptions);
            } catch (OwsExceptionReport ex) {
                exception.add(ex);
            }
        });
        exception.throwIfNotEmpty();
    }

    private void validateOutputs(Collection<OutputDefinition> outputs,
            ProcessOutputDescriptionContainer processDescription) throws OwsExceptionReport {
        CompositeOwsException exception = new CompositeOwsException();

        outputs.stream().map(OutputDefinition::getId).collect(MoreCollectors.toDuplicateStream()).map(
                ExecuteParameterValidator::duplicateOutput).forEach(exception::add);

        for (OutputDefinition output : outputs) {
            ProcessOutputDescription description = processDescription.getOutput(output.getId());

            if (description == null) {
                exception.add(invalidOutput(output, "no output with the specified identifier"));
            } else if (output.hasOutputs()) {
                if (description.isGroup()) {
                    try {
                        validateOutputs(output.getOutputs(), description.asGroup());
                    } catch (OwsExceptionReport ex) {
                        exception.add(ex);
                    }
                } else {
                    exception.add(invalidOutput(output, "output does not allow nested inputs"));
                }
            } else {
                try {
                    if (!output.getFormat().isEmpty()) {
                        description.visit(new OutputFormatValidator(output));
                    }
                } catch (OwsExceptionReport ex) {
                    exception.add(ex);
                }
            }
        }
        exception.throwIfNotEmpty();
    }

    private void validateFormat(FormattedProcessData input,
            ProcessInputDescription inputDescription) throws OwsExceptionReport {
        if (!input.getFormat().isEmpty()) {
            inputDescription.visit(new InputFormatValidator(input));
        }

    }

    private void validateInput(ReferenceProcessData input,
            ProcessInputDescription inputDescription) throws OwsExceptionReport {
        CompositeOwsException exception = new CompositeOwsException();
        if (input.getURI() == null) {
            exception.add(invalidInput(input, "missing input reference uri"));
        }
        try {
            validateFormat(input, inputDescription);
        } catch (OwsExceptionReport ex) {
            exception.add(ex);
        }
        exception.throwIfNotEmpty();
    }

    private void validateInput(ValueProcessData input,
            ProcessInputDescription inputDescription) throws OwsExceptionReport {
        validateFormat(input, inputDescription);
    }

    private void validateInput(GroupProcessData input,
            ProcessInputDescription inputDescription) throws OwsExceptionReport {
        CompositeOwsException exception = new CompositeOwsException();
        if (!inputDescription.isGroup()) {
            exception.add(invalidInput(input, "input does not allow nested inputs"));
        } else {
            try {
                validateInputs(input.getElements(), inputDescription.asGroup());
            } catch (OwsExceptionReport ex) {
                exception.add(ex);
            }
        }
        exception.throwIfNotEmpty();
    }

    private void validateInput(ProcessData input,
            ProcessInputDescriptionContainer descriptions) throws OwsExceptionReport {
        ProcessInputDescription description = descriptions.getInput(input.getId());
        if (description == null) {
            throw invalidInput(input, "no input with the specified identifier");
        } else if (input.isGroup()) {
            validateInput(input.asGroup(), description);
        } else if (input.isReference()) {
            validateInput(input.asReference(), description);
        } else if (input.isValue()) {
            validateInput(input.asValue(), description);
        }
    }

    private static OwsExceptionReport invalidInput(ProcessData input,
            String messageDetail) {
        String id = input.getId().getValue();
        return new InvalidParameterValueException().at(INPUT).withMessage(VALUE_INVALID, id, INPUT, messageDetail);
    }

    private static OwsExceptionReport invalidOutput(OutputDefinition output,
            String messageDetail) {
        String id = output.getId().getValue();
        return new InvalidParameterValueException().at(OUTPUT).withMessage(VALUE_INVALID, id, OUTPUT, messageDetail);
    }

    private static OwsExceptionReport duplicateOutput(OwsCode id) {
        return new InvalidParameterValueException().at(OUTPUT).withMessage("Duplicate output definition for output %s",
                id);
    }

    private static class OutputFormatValidator implements ProcessOutputDescription.ThrowingVisitor<OwsExceptionReport> {

        private final OutputDefinition output;

        OutputFormatValidator(OutputDefinition output) {
            this.output = Objects.requireNonNull(output);
        }

        @Override
        public void visit(BoundingBoxOutputDescription description) throws OwsExceptionReport {
            checkCompatibility(BoundingBoxInputOutputHandler.FORMATS.stream());
        }

        @Override
        public void visit(ComplexOutputDescription description) throws OwsExceptionReport {
            checkCompatibility(Stream.concat(Stream.of(description.getDefaultFormat()), description
                    .getSupportedFormats().stream()));
        }

        @Override
        public void visit(LiteralOutputDescription description) throws OwsExceptionReport {
            checkCompatibility(LiteralInputOutputHandler.FORMATS.stream());
        }

        @Override
        public void visit(GroupOutputDescription output) throws OwsExceptionReport {
            throw unsupportedFormat();
        }

        private OwsExceptionReport unsupportedFormat() {
            return invalidOutput(output, UNSUPPORTED_FORMAT);
        }

        private void checkCompatibility(Stream<Format> formats) throws OwsExceptionReport {
            if (!formats.anyMatch(f -> f.isCompatible(output.getFormat()))) {
                throw unsupportedFormat();
            }
        }

    }

    private static class InputFormatValidator implements ProcessInputDescription.ThrowingVisitor<OwsExceptionReport> {
        private final FormattedProcessData input;

        InputFormatValidator(FormattedProcessData input) {
            this.input = Objects.requireNonNull(input);
        }

        @Override
        public void visit(BoundingBoxInputDescription description) throws OwsExceptionReport {
            checkCompatibility(BoundingBoxInputOutputHandler.FORMATS.stream());
        }

        @Override
        public void visit(ComplexInputDescription description) throws OwsExceptionReport {
            checkCompatibility(Stream.concat(Stream.of(description.getDefaultFormat()), description
                    .getSupportedFormats().stream()));
        }

        @Override
        public void visit(LiteralInputDescription description) throws OwsExceptionReport {
            checkCompatibility(LiteralInputOutputHandler.FORMATS.stream());
        }

        @Override
        public void visit(GroupInputDescription description) throws OwsExceptionReport {
            throw unsupportedFormat();
        }

        private OwsExceptionReport unsupportedFormat() {
            return invalidInput(input, UNSUPPORTED_FORMAT);
        }

        private void checkCompatibility(Stream<Format> formats) throws OwsExceptionReport {
            if (!formats.anyMatch(f -> f.isCompatible(input.getFormat()))) {
                throw unsupportedFormat();
            }
        }
    }

    private static class InputOccurenceCollector implements ProcessInputDescription.ReturningVisitor<Map<Chain<OwsCode>,
            InputOccurence>> {
        private final Optional<Chain<OwsCode>> parent;

        InputOccurenceCollector(Chain<OwsCode> parent) {
            this.parent = Optional.ofNullable(parent);
        }

        InputOccurenceCollector() {
            this(null);
        }

        @Override
        public Map<Chain<OwsCode>, InputOccurence> visit(BoundingBoxInputDescription input) {
            return visitNonGroup(input);
        }

        @Override
        public Map<Chain<OwsCode>, InputOccurence> visit(ComplexInputDescription input) {
            return visitNonGroup(input);
        }

        @Override
        public Map<Chain<OwsCode>, InputOccurence> visit(LiteralInputDescription input) {
            return visitNonGroup(input);
        }

        @Override
        public Map<Chain<OwsCode>, InputOccurence> visit(GroupInputDescription input) {
            Chain<OwsCode> chain = getChain(input);
            Map<Chain<OwsCode>, InputOccurence> occurences = new HashMap<>(input.getInputs().size() + 1);
            occurences.put(chain, input.getOccurence());
            InputOccurenceCollector collector = new InputOccurenceCollector(chain);
            input.getInputDescriptions().stream().map(sub -> sub.visit(collector)).forEach(occurences::putAll);
            return occurences;
        }

        private Chain<OwsCode> getChain(ProcessInputDescription input) {
            return this.parent.map(c -> c.child(input.getId())).orElseGet(() -> new Chain<>(input.getId()));
        }

        private Map<Chain<OwsCode>, InputOccurence> visitNonGroup(ProcessInputDescription input) {
            return Collections.singletonMap(getChain(input), input.getOccurence());
        }
    }

}
