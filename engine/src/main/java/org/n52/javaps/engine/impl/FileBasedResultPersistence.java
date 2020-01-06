/*
 * Copyright 2016-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.engine.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.util.MoreFiles;
import org.n52.janmayen.Chain;
import org.n52.janmayen.Json;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.janmayen.lifecycle.Destroyable;
import org.n52.javaps.engine.EngineException;
import org.n52.javaps.engine.EngineProcessExecutionContext;
import org.n52.javaps.engine.JobNotFoundException;
import org.n52.javaps.engine.OutputNotFoundException;
import org.n52.javaps.engine.OutputReference;
import org.n52.javaps.engine.OutputReferencer;
import org.n52.javaps.engine.ResultPersistence;
import org.n52.javaps.io.EncodingException;
import org.n52.javaps.settings.SettingsConstants;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.DataTransmissionMode;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.JobId;
import org.n52.shetland.ogc.wps.JobStatus;
import org.n52.shetland.ogc.wps.OutputDefinition;
import org.n52.shetland.ogc.wps.ResponseMode;
import org.n52.shetland.ogc.wps.Result;
import org.n52.shetland.ogc.wps.StatusInfo;
import org.n52.shetland.ogc.wps.data.Body;
import org.n52.shetland.ogc.wps.data.GroupProcessData;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.shetland.ogc.wps.data.ValueProcessData;
import org.n52.shetland.ogc.wps.data.impl.FileBasedProcessData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * @author Christian Autermann
 */
@Configurable
public class FileBasedResultPersistence implements ResultPersistence, Constructable, Destroyable {
    private static final String GROUP_TYPE = "group";

    private static final String REFERENCE_TYPE = "reference";

    private static final String VALUE_TYPE = "value";

    private static final String META_JSON_FILE_NAME = ".meta.json";

    private static final Logger LOG = LoggerFactory.getLogger(FileBasedResultPersistence.class);

    private static final String COULD_NOT_LIST = "Could not list ";

    private Timer timer;

    private Path basePath;

    private Duration duration = Duration.ofHours(2);

    private Duration checkInterval = Duration.ofHours(1);

    private Optional<OutputReferencer> referencer;

    @Setting(SettingsConstants.MISC_BASE_DIRECTORY)
    public void setBasePath(File baseDirectory) {
        this.basePath = baseDirectory.toPath();
    }

    @Setting(SettingsConstants.MISC_DURATION)
    public void setDuration(String durationString) {
        this.duration = Duration.parse(durationString);
    }

    @Setting(SettingsConstants.MISC_CHECK_INTERVAL)
    public void setCheckInterval(String checkIntervalString) {
        this.checkInterval = Duration.parse(checkIntervalString);
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
    public void save(EngineProcessExecutionContext context) {
        try {
            String jobId = context.getJobId().getValue();
            String processId = context.getProcessId().getValue();
            Path directory = Files.createDirectories(basePath.resolve(jobId));
            OffsetDateTime expirationDate = getExpirationDate(directory);

            ObjectNode rootNode = Json.nodeFactory().objectNode().put(Keys.STATUS, context.getJobStatus().getValue())
                                      .put(Keys.JOB_ID, jobId).put(Keys.PROCESS_ID, processId)
                                      .put(Keys.EXPIRATION_DATE, expirationDate.toString())
                                      .put(Keys.RESPONSE_MODE, context.getResponseMode().toString());

            try {
                persist(directory,
                        context.getEncodedOutputs(), context.getOutputDefinitions(),
                        rootNode.putArray(Keys.OUTPUTS));

            } catch (Throwable ex) {
                LOG.error("Error executing job " + context.getJobId(), ex);
                rootNode.put(Keys.STATUS, JobStatus.failed().getValue());
                rootNode.put(Keys.ERROR, persistFailureCause(directory, ex).toString());

            }

            Files.write(directory.resolve(META_JSON_FILE_NAME),
                        Json.print(rootNode).getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            LOG.error("Error writing result for job " + context.getJobId(), ex);
        }
    }

    @Override
    public StatusInfo getStatus(JobId jobId) throws EngineException {

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
    public Result getResult(JobId jobId) throws EngineException {

        try {
            Result result = new Result();
            result.setJobId(jobId);
            result.setExpirationDate(getExpirationDate(jobId));

            JsonNode node = getJobMetadata(jobId);

            ResponseMode.fromString(node.path(Keys.RESPONSE_MODE).textValue()).ifPresent(result::setResponseMode);

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

    private OffsetDateTime getExpirationDate(JobId jobId) throws JobNotFoundException, IOException {
        return getExpirationDate(getJobDirectory(jobId));
    }

    private Map<OwsCode, OutputDefinition> byId(Collection<OutputDefinition> definitions) {
        return definitions.stream().collect(toMap(OutputDefinition::getId, Function.identity()));
    }

    private void encodeFormat(Format format,
                              ObjectNode formatNode) {
        formatNode.put(Keys.MIME_TYPE, format.getMimeType().orElse(null))
                  .put(Keys.SCHEMA, format.getSchema().orElse(null))
                  .put(Keys.ENCODING, format.getEncoding().orElse(null));
    }

    private Format decodeFormat(JsonNode node) {
        return new Format(node.path(Keys.MIME_TYPE).textValue(),
                          node.path(Keys.ENCODING).textValue(),
                          node.path(Keys.SCHEMA).textValue());
    }

    private void persist(Path directory,
                         List<ProcessData> outputs,
                         Map<OwsCode, OutputDefinition> outputDefinitions,
                         ArrayNode outputsNode) throws IOException, EncodingException {

        for (ProcessData data : outputs) {
            OutputDefinition definition = outputDefinitions.get(data.getId());

            ObjectNode outputNode = outputsNode.addObject();
            outputNode.putObject(Keys.ID).put(Keys.VALUE, data.getId().getValue())
                      .put(Keys.CODE_SPACE, data.getId().getCodeSpace().map(URI::toString).orElse(null));
            if (data.isGroup()) {
                outputNode.put(Keys.TYPE, GROUP_TYPE);
                persist(directory,
                        data.asGroup().getElements(),
                        definition.getOutputsById(),
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

                Optional<String> encoding = valueData.getFormat().getEncoding();

                if (encoding.isPresent() && encoding.get().equals(Format.BASE64_ENCODING)) {

                    try (InputStream in = valueData.getData()) {

                        Encoder base64encoder = Base64.getEncoder();

                        OutputStream outputStream = null;

                        try (OutputStream fileOutputStream = new FileOutputStream(outputFile.toFile())) {
                            outputStream = base64encoder.wrap(fileOutputStream);
                            IOUtils.copy(in, outputStream);
                        } finally {
                            if (outputStream != null) {
                                try {
                                    outputStream.close();
                                } catch (IOException e) {
                                    LOG.trace("Outputstream closed while encoding value as base64. Ignoring.");
                                }
                            }
                        }
                    }

                } else {
                    try (InputStream in = valueData.getData()) {
                        Files.copy(in, outputFile, StandardCopyOption.REPLACE_EXISTING);
                    }
                }

            }
        }
    }

    private Path getJobDirectory(JobId jobId) throws JobNotFoundException {
        Path directory = basePath.resolve(jobId.getValue());

        if (!Files.exists(directory)) {
            throw new JobNotFoundException(jobId);
        }
        return directory;
    }

    private JsonNode getJobMetadata(JobId jobId) throws JobNotFoundException, IOException {
        return Json.loadPath(getJobDirectory(jobId).resolve(META_JSON_FILE_NAME));
    }

    @Override
    public ProcessData getOutput(OutputReference reference) throws EngineException {
        try {
            return getOutput(reference,
                             reference.getOutputId(),
                             getJobMetadata(reference.getJobId()).path(Keys.OUTPUTS));
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

    private ProcessData decodeOutput(OutputReference reference, JsonNode node, boolean dereference) throws IOException {
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
        URI codeSpace = Optional.ofNullable(node.path(Keys.ID)
                                                .path(Keys.CODE_SPACE)
                                                .textValue())
                                .map(URI::create)
                                .orElse(null);
        String value = node.path(Keys.ID).path(Keys.VALUE).textValue();
        return new OwsCode(value, codeSpace);
    }

    private ProcessData decodeReferenceData(OutputReference reference, JsonNode node) {
        ResolvableReferenceProcessData referenceProcessData = new ResolvableReferenceProcessData(
                reference.getOutputId().last());
        referenceProcessData.setURI(URI.create(node.path(Keys.HREF).textValue()));
        referenceProcessData.setFormat(decodeFormat(node.path(Keys.FORMAT)));
        if (!node.path(Keys.BODY).isMissingNode()) {
            referenceProcessData.setBody(Body.inline(node.path(Keys.BODY).textValue()));
        } else if (!node.path(Keys.BODY_HREF).isMissingNode()) {
            referenceProcessData.setBody(Body.reference(node.path(Keys.BODY_HREF).textValue()));
        }
        return referenceProcessData;
    }

    private ProcessData decodeValueData(OutputReference reference, JsonNode node, boolean dereference) {
        DataTransmissionMode mode = DataTransmissionMode.fromString(node.path(Keys.DATA_TRANSMISSION_MODE).textValue())
                                                        .orElse(DataTransmissionMode.VALUE);
        Format format = decodeFormat(node.path(Keys.FORMAT));
        if (dereference || mode == DataTransmissionMode.VALUE || !this.referencer.isPresent()) {
            Path path = Paths.get(node.path(Keys.FILE).textValue());
            return new FileBasedProcessData(reference.getOutputId().last(), format, path);
        } else {
            URI uri = this.referencer.get().reference(reference);
            return new ResolvableReferenceProcessData(reference.getOutputId().last(), format, uri);
        }
    }

    private ProcessData decodeGroupData(OutputReference reference, JsonNode node) throws IOException {
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
            if (ex instanceof EngineException) {
                out.writeObject(ex.getCause());
            } else {
                out.writeObject(ex);
            }
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
            LOG.error(COULD_NOT_LIST + basePath, ex);
            return Collections.emptySet();
        }
    }

    @Override
    public Set<JobId> getJobIds(OwsCode processId) {

        Set<JobId> jobIdsforProcess = new HashSet<>();

        try {
            Set<JobId> allJobIds = Files.list(this.basePath)
                                        .filter(Files::isDirectory)
                                        .map(Path::getFileName)
                                        .filter(Objects::nonNull)
                                        .map(Path::toString)
                                        .map(JobId::new)
                                        .collect(toSet());

            for (JobId jobId : allJobIds) {
                try {
                    JsonNode jsonMetadata = getJobMetadata(jobId);

                    String processIdFromMetadata = jsonMetadata.path(Keys.PROCESS_ID).textValue();

                    if (processIdFromMetadata.equals(processId.getValue())) {
                        jobIdsforProcess.add(jobId);
                    }

                } catch (JobNotFoundException e) {
                    LOG.error(e.getMessage());
                }
            }

        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return jobIdsforProcess;
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
            list(basePath).filter(shouldBeDeleted(OffsetDateTime.now().minus(duration))).forEach(this::delete);
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
                LOG.warn(COULD_NOT_LIST + path, ex);
                return Stream.empty();
            }
        }

    }

    private interface Keys {
        String PROCESS_ID = "processId";

        String RESPONSE_MODE = "responseMode";

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

        String VALUE = VALUE_TYPE;

        String BODY = "body";

        String BODY_HREF = "bodyHref";

        String MIME_TYPE = "mimeType";

        String SCHEMA = "schema";

    }

}
