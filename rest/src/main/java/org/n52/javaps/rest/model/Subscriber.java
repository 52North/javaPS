/*
 * Copyright 2016-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.rest.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Optional URIs for callbacks for this job. Support for this parameter is not
 * required and the parameter may be removed from the API definition, if
 * conformance class **&#x27;callback&#x27;** is not listed in the conformance
 * declaration under &#x60;/conformance&#x60;.
 */
@Validated
@javax.annotation.Generated(
        value = "io.swagger.codegen.v3.generators.java.SpringCodegen",
        date = "2021-03-02T08:46:15.112Z[GMT]")

public class Subscriber {
    @JsonProperty("successUri")
    private String successUri;

    @JsonProperty("inProgressUri")
    private String inProgressUri;

    @JsonProperty("failedUri")
    private String failedUri;

    public Subscriber successUri(String successUri) {
        this.successUri = successUri;
        return this;
    }

    /**
     * Get successUri
     *
     * @return successUri
     **/
    public String getSuccessUri() {
        return successUri;
    }

    public void setSuccessUri(String successUri) {
        this.successUri = successUri;
    }

    public Subscriber inProgressUri(String inProgressUri) {
        this.inProgressUri = inProgressUri;
        return this;
    }

    /**
     * Get inProgressUri
     *
     * @return inProgressUri
     **/
    public String getInProgressUri() {
        return inProgressUri;
    }

    public void setInProgressUri(String inProgressUri) {
        this.inProgressUri = inProgressUri;
    }

    public Subscriber failedUri(String failedUri) {
        this.failedUri = failedUri;
        return this;
    }

    /**
     * Get failedUri
     *
     * @return failedUri
     **/

    public String getFailedUri() {
        return failedUri;
    }

    public void setFailedUri(String failedUri) {
        this.failedUri = failedUri;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Subscriber subscriber = (Subscriber) o;
        return Objects.equals(this.successUri, subscriber.successUri)
                && Objects.equals(this.inProgressUri, subscriber.inProgressUri)
                && Objects.equals(this.failedUri, subscriber.failedUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(successUri, inProgressUri, failedUri);
    }

    @Override
    public String toString() {
        return String.format("Subscriber{successUri: %s, inProgressUri: %s, failedUri: %s}", successUri, inProgressUri,
                failedUri);
    }
}
