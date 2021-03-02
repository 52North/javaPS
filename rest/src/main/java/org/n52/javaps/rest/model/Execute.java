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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Execute
 */
@Validated
public class Execute {
    @JsonProperty("id")
    private String id;

    @JsonProperty("inputs")
    private Input inputs;

    @JsonProperty("outputs")
    private Output outputs;

    /**
     * Gets or Sets mode
     */
    public enum ModeEnum {
        SYNC("sync"),

        ASYNC("async"),

        AUTO("auto");

        private String value;

        ModeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static ModeEnum fromValue(String text) {
            for (ModeEnum b : ModeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("mode")
    private ModeEnum mode;

    /**
     * Gets or Sets response
     */
    public enum ResponseEnum {
        RAW("raw"),

        DOCUMENT("document");

        private String value;

        ResponseEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static ResponseEnum fromValue(String text) {
            for (ResponseEnum b : ResponseEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("response")
    private ResponseEnum response;

    @JsonProperty("subscriber")
    private Subscriber subscriber;

    public Execute id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     **/
    @NotNull
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Execute inputs(Input inputs) {
        this.inputs = inputs;
        return this;
    }

    /**
     * Get inputs
     *
     * @return inputs
     **/
    @Valid
    public Input getInputs() {
        return inputs;
    }

    public void setInputs(Input inputs) {
        this.inputs = inputs;
    }

    public Execute outputs(Output outputs) {
        this.outputs = outputs;
        return this;
    }

    /**
     * Get outputs
     *
     * @return outputs
     **/
    @NotNull
    @Valid
    public Output getOutputs() {
        return outputs;
    }

    public void setOutputs(Output outputs) {
        this.outputs = outputs;
    }

    public Execute mode(ModeEnum mode) {
        this.mode = mode;
        return this;
    }

    /**
     * Get mode
     *
     * @return mode
     **/
    @NotNull
    public ModeEnum getMode() {
        return mode;
    }

    public void setMode(ModeEnum mode) {
        this.mode = mode;
    }

    public Execute response(ResponseEnum response) {
        this.response = response;
        return this;
    }

    /**
     * Get response
     *
     * @return response
     **/
    @NotNull
    public ResponseEnum getResponse() {
        return response;
    }

    public void setResponse(ResponseEnum response) {
        this.response = response;
    }

    public Execute subscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
        return this;
    }

    /**
     * Get subscriber
     *
     * @return subscriber
     **/
    @Valid
    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Execute execute = (Execute) o;
        return Objects.equals(this.inputs, execute.inputs) && Objects.equals(this.outputs, execute.outputs)
                && Objects.equals(this.mode, execute.mode) && Objects.equals(this.response, execute.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputs, outputs, mode, response);
    }

    @Override
    public String toString() {
        return String.format("Execute{inputs: %s, outputs: %s, mode: %s, response: %s}", inputs, outputs, mode,
                response);
    }
}
