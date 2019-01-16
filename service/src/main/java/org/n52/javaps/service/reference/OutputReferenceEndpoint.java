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
package org.n52.javaps.service.reference;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Objects;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import org.n52.svalbard.encode.stream.MissingStreamWriterException;
import org.n52.svalbard.encode.stream.StreamWriter;
import org.n52.svalbard.encode.stream.StreamWriterKey;
import org.n52.svalbard.encode.stream.StreamWriterRepository;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.shetland.ogc.wps.data.ValueProcessData;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.javaps.engine.EngineException;
import org.n52.javaps.engine.JobNotFoundException;
import org.n52.javaps.engine.OutputNotFoundException;
import org.n52.javaps.engine.OutputReference;
import org.n52.javaps.engine.OutputReferencer;
import org.n52.javaps.engine.ResultPersistence;
import org.n52.javaps.service.InternalServerErrorException;
import org.n52.javaps.service.NotFoundException;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.io.ByteStreams;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
@Controller
@RequestMapping("/results/{jobId}/**")
public class OutputReferenceEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(OutputReferenceEndpoint.class);

    private final OutputReferencer outputReferencer;

    private final ResultPersistence resultManager;

    private final StreamWriterRepository streamWriterRepository;

    @Inject
    public OutputReferenceEndpoint(OutputReferencer outputReferencer, ResultPersistence resultManager,
            StreamWriterRepository streamWriterRepository) {
        this.outputReferencer = Objects.requireNonNull(outputReferencer);
        this.resultManager = Objects.requireNonNull(resultManager);
        this.streamWriterRepository = Objects.requireNonNull(streamWriterRepository);
    }

    @RequestMapping(
            method = RequestMethod.GET)
    public void get(@PathVariable String jobId,
            HttpServletRequest request,
            HttpServletResponse response) throws NotFoundException, InternalServerErrorException {
        OutputReference reference = toOutputReference(request);
        LOG.info("Getting output {}", reference);
        ProcessData output = getOutput(reference);
        if (output.isGroup() || output.isReference()) {
            writeWrapper(output, response);
        } else if (output.isValue()) {
            writeRawValue(output, response);
        }
    }

    private void writeWrapper(ProcessData output,
            HttpServletResponse response) throws InternalServerErrorException {
        try {
            MediaType mediaType = MediaTypes.APPLICATION_XML;
            StreamWriter<? super ProcessData> writer = getWriter(output, mediaType);
            response.setHeader(HttpHeaders.CONTENT_TYPE, mediaType.toString());
            writer.write(output, response.getOutputStream());
        } catch (IOException | EncodingException ex) {
            throw new InternalServerErrorException(ex);
        }
    }

    private void writeRawValue(ProcessData output,
            HttpServletResponse response) throws InternalServerErrorException {
        ValueProcessData valueOutput = output.asValue();
        Format format = valueOutput.getFormat();

        format.getMimeType().ifPresent(mimeType -> response.setHeader(HttpHeaders.CONTENT_TYPE, mimeType));
        format.getEncoding().ifPresent(encoding -> response.setHeader(HttpHeaders.CONTENT_ENCODING, encoding));

        try (InputStream stream = valueOutput.getData()) {
            ByteStreams.copy(stream, response.getOutputStream());
        } catch (IOException ex) {
            throw new InternalServerErrorException(ex);
        }
    }

    private OutputReference toOutputReference(HttpServletRequest request) {
        URI toUri = UriComponentsBuilder.fromUriString(request.getRequestURI()).build().toUri();
        return this.outputReferencer.dereference(toUri);
    }

    private <T> StreamWriter<? super T> getWriter(T output,
            MediaType mediaType) throws InternalServerErrorException {
        StreamWriterKey key = new StreamWriterKey(output.getClass(), mediaType);
        return this.streamWriterRepository.getWriter(key).orElseThrow(() -> new InternalServerErrorException(
                new MissingStreamWriterException(key)));
    }

    private ProcessData getOutput(OutputReference reference) throws InternalServerErrorException, NotFoundException {
        try {
            return this.resultManager.getOutput(reference);
        } catch (JobNotFoundException | OutputNotFoundException ex) {
            throw new NotFoundException();
        } catch (EngineException ex) {
            throw new InternalServerErrorException(ex);
        }
    }
}
