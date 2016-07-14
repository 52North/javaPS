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
package org.n52.javaps;

import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import javax.inject.Inject;

import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.ogc.wps.DataTransmissionMode;
import org.n52.iceland.ogc.wps.Format;
import org.n52.iceland.ogc.wps.JobId;
import org.n52.iceland.ogc.wps.OutputDefinition;
import org.n52.iceland.ogc.wps.Result;
import org.n52.iceland.ogc.wps.data.Body;
import org.n52.iceland.ogc.wps.data.FileBasedProcessData;
import org.n52.iceland.ogc.wps.data.GroupProcessData;
import org.n52.iceland.ogc.wps.data.ProcessData;
import org.n52.iceland.ogc.wps.data.ReferenceProcessData;
import org.n52.iceland.util.Chain;
import org.n52.iceland.util.JSONUtils;
import org.n52.javaps.io.EncodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author Christian Autermann
 */
public class FileBasedResultManager implements ResultManager {
    private static final String GROUP_TYPE = "group";
    private static final String REFERENCE_TYPE = "reference";
    private static final String VALUE_TYPE = "value";
    private static final String META_JSON_FILE_NAME = ".meta.json";

    private Path basePath;
    private Duration duration;
    private Optional<OutputReferencer> referencer;

    public void setBasePath(Path basePath) {
        this.basePath = Objects.requireNonNull(basePath);
    }

    public void setDuration(Duration duration) {
        this.duration = Objects.requireNonNull(duration);
    }

    @Inject
    public void setReferencer(Optional<OutputReferencer> referencer) {
        this.referencer = Objects.requireNonNull(referencer);
    }

    public void setReferencer(OutputReferencer referencer) {
        setReferencer(Optional.ofNullable(referencer));
    }

    @Override
    public Result getResult(JobId jobId) throws JobNotFoundException, IOException {

        Result result = new Result();
        result.setJobId(jobId);
        result.setExpirationDate(getExpirationDate(jobId));

        JsonNode node = getJobMetadata(jobId);

        for (JsonNode outputNode : node.path(Keys.OUTPUTS)) {
            OwsCode identifier = decodeIdentifier(outputNode);
            OutputReference reference = new OutputReference(jobId, identifier);
            result.addOutput(decodeOutput(reference, node, false));
        }

        return result;
    }

    @Override
    public void saveResult(Result result, Collection<OutputDefinition> outputDefinitions)
            throws IOException, EncodingException {
        String jobId = result.getJobId().get().getValue();
        Path directory = Files.createDirectories(basePath.resolve(jobId));
        ObjectNode rootNode = JSONUtils.nodeFactory().objectNode();
        rootNode.put(Keys.JOB_ID, jobId);
        rootNode.put(Keys.EXPIRATION_DATE, getExpirationDate(directory).toString());
        persist(directory, result.getOutputs(), outputDefinitions, rootNode.putArray(Keys.OUTPUTS));
        Files.write(directory.resolve(META_JSON_FILE_NAME), JSONUtils.print(rootNode).getBytes(StandardCharsets.UTF_8));
    }

    private OffsetDateTime getExpirationDate(Path directory) throws IOException {
        FileTime lastModifiedTime = Files.getLastModifiedTime(directory);
        return lastModifiedTime.toInstant().plus(this.duration).atOffset(ZoneOffset.UTC);
    }

    private Map<OwsCode, OutputDefinition> byId(Collection<OutputDefinition> definitions) {
        return definitions.stream().collect(toMap(OutputDefinition::getId, Function.identity()));
    }

    private void encodeFormat(Format format, ObjectNode formatNode) {
        formatNode.put(Keys.MIME_TYPE, format.getMimeType().orElse(null))
                .put(Keys.SCHEMA, format.getSchema().orElse(null))
                .put(Keys.ENCODING, format.getEncoding().orElse(null));
    }

    private Format decodeFormat(JsonNode node) {
        return new Format(node.path(Keys.MIME_TYPE).textValue(),
                          node.path(Keys.SCHEMA).textValue(),
                          node.path(Keys.ENCODING).textValue());
    }

    private void persist(Path directory, List<ProcessData> outputs, Collection<OutputDefinition> outputDefinitions,
                         ArrayNode outputsNode) throws IOException, EncodingException {
        Map<OwsCode, OutputDefinition> byId = byId(outputDefinitions);

        for (ProcessData data : outputs) {
            OutputDefinition definition = byId.get(data.getId());

            ObjectNode outputNode = outputsNode.addObject();
            outputNode.putObject(Keys.ID)
                    .put(Keys.VALUE, data.getId().getValue())
                    .put(Keys.CODE_SPACE, data.getId().getCodeSpace().map(URI::toString).orElse(null));
            if (data.isGroup()) {
                outputNode.put(Keys.TYPE, GROUP_TYPE);
                persist(directory,
                        data.asGroup().getElements(),
                        definition.getOutputs(),
                        outputNode.putArray(Keys.OUTPUTS));
            } else if (data.isReference()) {
                outputNode.put(Keys.TYPE, REFERENCE_TYPE);
                encodeFormat(data.asReference().getFormat(), outputNode.putObject(Keys.FORMAT));
                outputNode.put(Keys.HREF, data.asReference().getURI().toString());
                if (data.asReference().getBody().isPresent()) {
                    Body body = data.asReference().getBody().get();
                    if (body.isInline()) {
                        outputNode.put(Keys.BODY, body.asInline().getBody());
                    } else if (body.isReferenced()) {
                        outputNode.put(Keys.BODY_HREF, body.asReferenced().getHref().toString());
                    }
                }
            } else if (data.isValue()) {
                outputNode.put(Keys.TYPE, VALUE_TYPE);
                outputNode.put(Keys.DATA_TRANSMISSION_MODE, definition.getDataTransmissionMode().toString());
                encodeFormat(data.asReference().getFormat(), outputNode.putObject(Keys.FORMAT));
                Path outputFile = Files.createTempFile(directory, null, null);
                outputNode.put(Keys.FILE, outputFile.toString());
                try (InputStream in = data.asValue().getData()) {
                    Files.copy(in, outputFile);
                }
            }
        }
    }

    private OffsetDateTime getExpirationDate(JobId jobId) throws JobNotFoundException, IOException {
        return getExpirationDate(getJobDirectory(jobId));
    }

    private Path getJobDirectory(JobId jobId) throws JobNotFoundException {
        Path directory = basePath.resolve(jobId.getValue());

        if (!Files.exists(directory)) {
            throw new JobNotFoundException(jobId);
        }
        return directory;
    }

    private JsonNode getJobMetadata(JobId jobId) throws JobNotFoundException, IOException {
        return JSONUtils.loadPath(getJobDirectory(jobId).resolve(META_JSON_FILE_NAME));
    }

    @Override
    public ProcessData getOutput(OutputReference reference)
            throws JobNotFoundException, OutputNotFoundException, IOException {
        return getOutput(reference, reference.getOutputId(), getJobMetadata(reference.getJobId()).path(Keys.OUTPUTS));
    }

    private ProcessData getOutput(OutputReference reference, Chain<OwsCode> tail, JsonNode outputs)
            throws OutputNotFoundException, IOException {
        for (JsonNode node : outputs) {
            OwsCode id = decodeIdentifier(node);

            if (id.equals(tail.first())) {
                Optional<Chain<OwsCode>> next = tail.tail();
                if (next.isPresent()) {
                    return getOutput(reference, next.get(), outputs.path(Keys.OUTPUTS));
                } else {
                    return decodeOutput(reference, node, true);
                }
            }
        }

        throw new OutputNotFoundException();
    }

    private ProcessData decodeOutput(OutputReference reference, JsonNode node,
                                     boolean dereference) throws IOException {
        switch (node.path(Keys.TYPE).textValue()) {
            case REFERENCE_TYPE:
                return decodeReferenceData(reference, node);
            case VALUE_TYPE:
                return decodeValueData(reference, node, dereference);
            case GROUP_TYPE:
                return decodeGroupData(reference, node);
            default:
                throw new IOException("Unsupported output type");
        }
    }

    private OwsCode decodeIdentifier(JsonNode node) {
        URI codeSpace = Optional.ofNullable(node.path(Keys.ID).path(Keys.CODE_SPACE).textValue())
                .map(URI::create).orElse(null);
        String value = node.path(Keys.ID).path(Keys.VALUE).textValue();
        OwsCode id = new OwsCode(value, codeSpace);
        return id;
    }

    private ProcessData decodeReferenceData(OutputReference reference, JsonNode node) {
        ReferenceProcessData referenceProcessData = new ReferenceProcessData(reference.getOutputId().last());
        referenceProcessData.setURI(URI.create(node.path(Keys.HREF).textValue()));
        referenceProcessData.setFormat(decodeFormat(node.path(Keys.FORMAT)));
        if (!node.path(Keys.BODY).isMissingNode()) {
            referenceProcessData.setBody(Body.inline(node.path(Keys.BODY).textValue()));
        } else if (!node.path(Keys.BODY_HREF).isMissingNode()) {
            referenceProcessData.setBody(Body.reference(node.path(Keys.BODY_HREF).textValue()));
        }
        return referenceProcessData;
    }

    private ProcessData decodeValueData(OutputReference reference, JsonNode node,
                                        boolean dereference) {
        DataTransmissionMode mode = DataTransmissionMode.valueOf(node.path(Keys.DATA_TRANSMISSION_MODE).textValue());
        Format format = decodeFormat(node.path(Keys.FORMAT));
        if (dereference || mode == DataTransmissionMode.VALUE || !this.referencer.isPresent()) {
            Path path = Paths.get(node.path(Keys.FILE).textValue());
            return new FileBasedProcessData(reference.getOutputId().last(), format, path);
        } else {
            URI uri = this.referencer.get().reference(reference);
            return new ReferenceProcessData(reference.getOutputId().last(), format, uri);
        }
    }

    private ProcessData decodeGroupData(OutputReference reference, JsonNode node)
            throws IOException {
        GroupProcessData groupProcessData = new GroupProcessData(reference.getOutputId().last());
        for (JsonNode childNode : node.path(Keys.OUTPUTS)) {
            OutputReference childRefernce = reference.child(decodeIdentifier(childNode));
            ProcessData childOutput = decodeOutput(childRefernce, childNode, false);
            groupProcessData.addElement(childOutput);
        }
        return groupProcessData;
    }

    private interface Keys {
        String FORMAT = "format";
        String ENCODING = "encoding";
        String HREF = "href";
        String JOB_ID = "jobId";
        String ID = "id";
        String FILE = "file";
        String EXPIRATION_DATE = "expirationDate";
        String OUTPUTS = "outputs";
        String CODE_SPACE = "codeSpace";
        String TYPE = "type";
        String DATA_TRANSMISSION_MODE = "dataTransmissionMode";
        String VALUE = "value";
        String BODY = "body";
        String BODY_HREF = "bodyHref";
        String MIME_TYPE = "mimeType";
        String SCHEMA = "schema";

    }

}
