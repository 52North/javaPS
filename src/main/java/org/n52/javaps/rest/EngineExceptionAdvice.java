/*
 * Copyright (C) 2016 by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.javaps.rest;

import org.n52.javaps.engine.EngineException;
import org.n52.javaps.engine.InputDecodingException;
import org.n52.javaps.engine.JobNotFoundException;
import org.n52.javaps.engine.OutputEncodingException;
import org.n52.javaps.engine.OutputNotFoundException;
import org.n52.javaps.engine.ProcessNotFoundException;
import org.n52.javaps.engine.UnsupportedInputFormatException;
import org.n52.javaps.engine.UnsupportedOutputFormatException;
import org.n52.javaps.rest.serializer.ExceptionSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequestMapping(produces = "application/json")
public class EngineExceptionAdvice {
    private static final String INVALID_PARAMETER = "InvalidParameter";
    private static final String NO_APPLICABLE_CODE = "NoApplicableCode";
    private final ExceptionSerializer exceptionSerializer;

    @Autowired
    public EngineExceptionAdvice(ExceptionSerializer serializer) {
        this.exceptionSerializer = serializer;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(JobNotFoundException.class)
    public io.swagger.model.Exception handle(JobNotFoundException ex) {
        return exceptionSerializer.serializeException(INVALID_PARAMETER, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProcessNotFoundException.class)
    public io.swagger.model.Exception handle(ProcessNotFoundException ex) {
        return exceptionSerializer.serializeException(INVALID_PARAMETER, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OutputNotFoundException.class)
    public io.swagger.model.Exception handle(OutputNotFoundException ex) {
        return exceptionSerializer.serializeException(INVALID_PARAMETER, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InputDecodingException.class})
    public io.swagger.model.Exception handle(InputDecodingException ex) {
        return exceptionSerializer.serializeException(INVALID_PARAMETER, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(OutputEncodingException.class)
    public io.swagger.model.Exception handle(OutputEncodingException ex) {
        return exceptionSerializer.serializeException(INVALID_PARAMETER, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EngineException.class)
    public io.swagger.model.Exception handle(UnsupportedInputFormatException ex) {
        return exceptionSerializer.serializeException(INVALID_PARAMETER, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EngineException.class)
    public io.swagger.model.Exception handle(UnsupportedOutputFormatException ex) {
        return exceptionSerializer.serializeException(INVALID_PARAMETER, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(EngineException.class)
    public io.swagger.model.Exception handle(EngineException ex) {
        return exceptionSerializer.serializeException(NO_APPLICABLE_CODE, ex.getMessage());
    }
}
