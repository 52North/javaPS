/*
 * Copyright 2019 52°North Initiative for Geospatial Open Source
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
package org.n52.wp.javaps.transactional.rest;

import org.n52.javaps.transactional.DuplicateProcessException;
import org.n52.javaps.transactional.UnsupportedProcessException;
import org.n52.javaps.rest.serializer.ExceptionSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequestMapping(produces = "application/json")
public class TransactionalEngineExceptionAdvice {
    private static final String INVALID_PARAMETER = "InvalidParameter";
    private final ExceptionSerializer exceptionSerializer;

    @Autowired
    public TransactionalEngineExceptionAdvice(ExceptionSerializer serializer) {
        this.exceptionSerializer = serializer;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateProcessException.class)
    public io.swagger.model.Exception handle(DuplicateProcessException ex) {
        return exceptionSerializer.serializeException(INVALID_PARAMETER, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnsupportedProcessException.class)
    public io.swagger.model.Exception handle(UnsupportedProcessException ex) {
        return exceptionSerializer.serializeException(INVALID_PARAMETER, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnsupportedProcessException.class)
    public io.swagger.model.Exception handle(NotUndeployableProcessException ex) {
        return exceptionSerializer.serializeException(INVALID_PARAMETER, ex.getMessage());
    }
}
