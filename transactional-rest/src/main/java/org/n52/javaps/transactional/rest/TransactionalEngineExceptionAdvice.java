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
package org.n52.javaps.transactional.rest;

import org.n52.javaps.rest.EngineExceptionAdvice;
import org.n52.javaps.rest.MediaTypes;
import org.n52.javaps.rest.model.Exception;
import org.n52.javaps.rest.serializer.ExceptionSerializer;
import org.n52.javaps.transactional.DuplicateProcessException;
import org.n52.javaps.transactional.UndeletableProcessException;
import org.n52.javaps.transactional.UnsupportedProcessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * {@link RestControllerAdvice} to the {@link TransactionalApi}.
 *
 * @author Christian Autermann
 */
@RestControllerAdvice(assignableTypes = TransactionalApi.class)
@RequestMapping(produces = MediaTypes.APPLICATION_JSON)
public class TransactionalEngineExceptionAdvice extends EngineExceptionAdvice {

    /**
     * Set the {@link ExceptionSerializer}.
     *
     * @param serializer The {@link ExceptionSerializer}.
     */
    @Autowired
    public TransactionalEngineExceptionAdvice(ExceptionSerializer serializer) {
        super(serializer);
    }

    /**
     * Handle a {@link DuplicateProcessException}.
     *
     * @param ex The exception.
     * @return The response.
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateProcessException.class)
    public Exception handle(DuplicateProcessException ex) {
        return getExceptionSerializer().serializeException(INVALID_PARAMETER, ex.getMessage());
    }

    /**
     * Handle a {@link UnsupportedProcessException}.
     *
     * @param ex The exception.
     * @return The response.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnsupportedProcessException.class)
    public Exception handle(UnsupportedProcessException ex) {
        return getExceptionSerializer().serializeException(INVALID_PARAMETER, ex.getMessage());
    }

    /**
     * Handle a {@link UndeletableProcessException}.
     *
     * @param ex The exception.
     * @return The response.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UndeletableProcessException.class)
    public Exception handle(UndeletableProcessException ex) {
        return getExceptionSerializer().serializeException(INVALID_PARAMETER, ex.getMessage());
    }
}
