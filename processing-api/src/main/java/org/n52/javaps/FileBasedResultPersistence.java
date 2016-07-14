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
import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.lifecycle.Destroyable;
import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.ogc.wps.DataTransmissionMode;
import org.n52.iceland.ogc.wps.Format;
import org.n52.iceland.ogc.wps.JobId;
import org.n52.iceland.ogc.wps.JobStatus;
import org.n52.iceland.ogc.wps.OutputDefinition;
import org.n52.iceland.ogc.wps.Result;
import org.n52.iceland.ogc.wps.StatusInfo;
import org.n52.iceland.ogc.wps.data.Body;
import org.n52.iceland.ogc.wps.data.FileBasedProcessData;
import org.n52.iceland.ogc.wps.data.GroupProcessData;
import org.n52.iceland.ogc.wps.data.ProcessData;
import org.n52.iceland.ogc.wps.data.ReferenceProcessData;
import org.n52.iceland.ogc.wps.data.ValueProcessData;
import org.n52.iceland.util.Chain;
import org.n52.iceland.util.JSONUtils;
import org.n52.iceland.util.MoreFiles;
import org.n52.javaps.io.EncodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author Christian Autermann
 */
public class FileBasedResultPersistence implements ResultPersistence, Constructable, Destroyable {
    private static final String GROUP_TYPE = "group";
    private static final String REFERENCE_TYPE = "reference";
    private static final String VALUE_TYPE = "value";
    private static final String META_JSON_FILE_NAME = ".meta.json";
    private static final Logger LOG = LoggerFactory.getLogger(FileBasedResultPersistence.class);
    private Timer timer;
    private Path basePath;
    private Duration duration = Duration.ofHours(2);
    private Duration checkInterval = Duration.ofHours(1);
    private Optional<OutputReferencer> referencer;

    public void setBasePath(Path basePath) {
        this.basePath = Objects.requireNonNull(basePath);
    }

    public void setDuration(Duration duration) {
        this.duration = Objects.requireNonNull(duration);
    }

    public void setCheckInterval(Duration checkInterval) {
        this.checkInterval = Objects.requireNonNull(checkInterval);
    }

    @Inject
    public void setReferencer(Optional<OutputReferencer> referencer) {
        this.referencer = Objects.requireNonNull(referencer);
    }

    public void setReferencer(OutputReferencer referencer) {
        setReferencer(Optional.ofNullable(referencer));
    }

    @Override
    public void init() {
        this.timer = new Timer();
        CleanupTask task = new CleanupTask(basePath, duration);
        this.timer.scheduleAtFixedRate(task, 0, checkInterval.toMillis());
    }

    @Override
    public void destroy() {
        this.timer.cancel();
    }

    @Override
    public void save(ProcessExecutionContext context) {
        try {
            String jobId = context.getJobId().getValue();
            Path directory = Files.createDirectories(basePath.resolve(jobId));
            OffsetDateTime expirationDate = getExpirationDate(directory);

            ObjectNode rootNode = JSONUtils.nodeFactory().objectNode()
                    .put(Keys.STATUS, context.getJobStatus().getValue())
                    .put(Keys.JOB_ID, jobId)
                    .put(Keys.EXPIRATION_DATE, expirationDate.toString());

            try {
                persist(directory,
                        context.getResult().getOutputs(),
                        context.getOutputDefinitions(),
                        rootNode.putArray(Keys.OUTPUTS));

            } catch (Throwable ex) {
                LOG.error("Error executing job " + context.getJobId(), ex);
                rootNode.put(Keys.STATUS, JobStatus.failed().getValue());
                rootNode.put(Keys.ERROR, persistFailureCause(directory, ex).toString());

            }

            Files.write(directory.resolve(META_JSON_FILE_NAME),
                        JSONUtils.print(rootNode).getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            LOG.error("Error writing result for job " + context.getJobId(), ex);
        }
    }

    @Override
    public StatusInfo getStatus(JobId jobId) throws EngineException, JobNotFoundException {

        try {
            JsonNode jobMetadata = getJobMetadata(jobId);
            OffsetDateTime expirationDate = getExpirationDate(jobId);

            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setJobId(jobId);
            statusInfo.setExpirationDate(expirationDate);
            statusInfo.setStatus(new JobStatus(jobMetadata.path(Keys.STATUS).textValue()));
            return statusInfo;
        } catch (IOException ex) {
            throw new EngineException(ex);
        }
    }

    @Override
    public Result getResult(JobId jobId) throws JobNotFoundException, EngineException {

        try {
            Result result = new Result();
            result.setJobId(jobId);
            result.setExpirationDate(getExpirationDate(jobId));

            JsonNode node = getJobMetadata(jobId);

            if (JobStatus.failed().getValue().equals(node.path(Keys.STATUS).textValue())) {
                Path path = Paths.get(node.path(Keys.ERROR).textValue());
                try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
                    throw new EngineException((Throwable) in.readObject());
                } catch (ClassNotFoundException ex) {
                    throw new EngineException(ex);
                }
            }

            for (JsonNode outputNode : node.path(Keys.OUTPUTS)) {
                OwsCode identifier = decodeIdentifier(outputNode);
                OutputReference reference = new OutputReference(jobId, identifier);
                result.addOutput(decodeOutput(reference, outputNode, false));
            }

            return result;
        } catch (IOException ex) {
            throw new EngineException(ex);
        }
    }

    private OffsetDateTime getExpirationDate(Path directory) throws IOException {
        return getLastModifiedTimeChecked(directory).plus(this.duration);
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
                          node.path(Keys.ENCODING).textValue(),
                          node.path(Keys.SCHEMA).textValue());
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
                ValueProcessData valueData = data.asValue();
                outputNode.put(Keys.TYPE, VALUE_TYPE);
                outputNode.put(Keys.DATA_TRANSMISSION_MODE, definition.getDataTransmissionMode().toString());
                encodeFormat(valueData.getFormat(), outputNode.putObject(Keys.FORMAT));
                Path outputFile = Files.createTempFile(directory, null, null);
                outputNode.put(Keys.FILE, outputFile.toString());
                try (InputStream in = valueData.getData()) {
                    Files.copy(in, outputFile, StandardCopyOption.REPLACE_EXISTING);
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
            throws EngineException {
        try {
            return getOutput(reference, reference.getOutputId(), getJobMetadata(reference.getJobId()).path(Keys.OUTPUTS));
        } catch (IOException ex) {
            throw new EngineException(ex);
        }
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
        DataTransmissionMode mode = DataTransmissionMode
                .fromString(node.path(Keys.DATA_TRANSMISSION_MODE).textValue())
                .orElse(DataTransmissionMode.VALUE);
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

    private Path persistFailureCause(Path directory, Throwable ex) throws IOException {
        Path outputFile = Files.createTempFile(directory, null, null);
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(outputFile))) {
            out.writeObject(ex);
        }
        return outputFile;
    }

    @Override
    public Set<JobId> getJobIds() {
        try {
            return Files.list(this.basePath)
                    .filter(Files::isDirectory)
                    .map(Path::getFileName)
                    .filter(Objects::nonNull)
                    .map(Path::toString)
                    .map(JobId::new)
                    .collect(toSet());
        } catch (IOException ex) {
            LOG.error("Could not list " + basePath, ex);
            return Collections.emptySet();
        }
    }

    private static Optional<OffsetDateTime> getLastModifiedTime(Path directory) {
        try {
            return Optional.of(getLastModifiedTimeChecked(directory));
        } catch (IOException ex) {
            LOG.warn("Could not read last modified time of " + directory, ex);
            return Optional.empty();
        }
    }

    private static OffsetDateTime getLastModifiedTimeChecked(Path directory) throws IOException {
        return Files.getLastModifiedTime(directory).toInstant().atOffset(ZoneOffset.UTC);
    }

    private static class CleanupTask extends TimerTask {
        private final Path basePath;
        private final Duration duration;

        CleanupTask(Path basePath, Duration duration) {
            this.basePath = basePath;
            this.duration = duration;
        }

        @Override
        public void run() {
            list(basePath)
                    .filter(shouldBeDeleted(OffsetDateTime.now().minus(duration)))
                    .forEach(this::delete);
        }

        private Predicate<Path> shouldBeDeleted(OffsetDateTime threshold) {
            return path -> Files.isDirectory(path) &&
                           getLastModifiedTime(path).filter(dt -> dt.compareTo(threshold) <= 0).isPresent();

        }

        private void delete(Path path) {
            try {
                MoreFiles.deleteRecursively(path);
            } catch (IOException ex) {
                LOG.warn("Could not delete " + path, ex);
            }
        }

        private Stream<Path> list(Path path) {
            try {
                return Files.list(path);
            } catch (IOException ex) {
                LOG.warn("Could not list " + path, ex);
                return Stream.empty();
            }
        }

    }

    private static interface Keys {
        String ERROR = "error";
        String STATUS = "status";
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
