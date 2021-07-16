/*
 * Copyright 2016-2021 52°North Spatial Information Research GmbH
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
package org.n52.javaps.rest;

import org.n52.javaps.algorithm.ExecutionException;
import org.n52.javaps.engine.EngineException;
import org.n52.javaps.engine.InputDecodingException;
import org.n52.javaps.engine.JobNotFoundException;
import org.n52.javaps.engine.OutputEncodingException;
import org.n52.javaps.engine.OutputNotFoundException;
import org.n52.javaps.engine.ProcessNotFoundException;
import org.n52.javaps.engine.UnsupportedInputFormatException;
import org.n52.javaps.engine.UnsupportedOutputFormatException;
import org.n52.javaps.rest.model.Exception;
import org.n52.javaps.rest.serializer.ExceptionSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice(assignableTypes = { Api.class, LandingPageApi.class, ConformanceApi.class, ProcessesApi.class,
        ProcessesApiExtension.class, })
@RequestMapping(produces = MediaTypes.APPLICATION_JSON)
public class EngineExceptionAdvice {
    protected static final String INVALID_PARAMETER = "InvalidParameter";
    protected static final String NO_APPLICABLE_CODE = "NoApplicableCode";
    private final ExceptionSerializer exceptionSerializer;

    @Autowired
    public EngineExceptionAdvice(ExceptionSerializer exceptionSerializer) {
        this.exceptionSerializer = Objects.requireNonNull(exceptionSerializer);
    }

    protected ExceptionSerializer getExceptionSerializer() {
        return exceptionSerializer;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(JobNotFoundException.class)
    public Exception handle(JobNotFoundException ex) {
        return getExceptionSerializer().serializeException(INVALID_PARAMETER, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProcessNotFoundException.class)
    public Exception handle(ProcessNotFoundException ex) {
        return getExceptionSerializer().serializeException(INVALID_PARAMETER, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OutputNotFoundException.class)
    public Exception handle(OutputNotFoundException ex) {
        return getExceptionSerializer().serializeException(INVALID_PARAMETER, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ InputDecodingException.class })
    public Exception handle(InputDecodingException ex) {
        return getExceptionSerializer().serializeException(INVALID_PARAMETER, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(OutputEncodingException.class)
    public Exception handle(OutputEncodingException ex) {
        return getExceptionSerializer().serializeException(INVALID_PARAMETER, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnsupportedInputFormatException.class)
    public Exception handle(UnsupportedInputFormatException ex) {
        return getExceptionSerializer().serializeException(INVALID_PARAMETER, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnsupportedOutputFormatException.class)
    public Exception handle(UnsupportedOutputFormatException ex) {
        return getExceptionSerializer().serializeException(INVALID_PARAMETER, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(EngineException.class)
    public Exception handle(EngineException ex) {
        return getExceptionSerializer().serializeException(NO_APPLICABLE_CODE, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(java.lang.Exception.class)
    public Exception handle(java.lang.Exception ex) {
        return getExceptionSerializer().serializeException(NO_APPLICABLE_CODE, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ExecutionException.class)
    public Exception handle(ExecutionException ex) {
        return getExceptionSerializer().serializeException(NO_APPLICABLE_CODE, ex.getMessage());
    }
}
