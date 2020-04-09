/*
 * Copyright (C) 2020 by 52 North Initiative for Geospatial Open Source Software GmbH
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
package org.n52.javaps.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * Execute
 */
@Validated
public class Execute {
    @JsonProperty("inputs")
    @Valid
    private List<Input> inputs;

    @JsonProperty("outputs")
    @Valid
    private List<Output> outputs = new ArrayList<>();

    @JsonProperty("mode")
    private ModeEnum mode = null;

    @JsonProperty("response")
    private ResponseEnum response = null;

    public Execute inputs(List<Input> inputs) {
        this.inputs = inputs;
        return this;
    }

    public Execute addInputsItem(Input inputsItem) {
        if (this.inputs == null) {
            this.inputs = new ArrayList<Input>();
        }
        this.inputs.add(inputsItem);
        return this;
    }

    public Execute mode(ModeEnum mode) {
    	this.mode = mode;
        return this;
    }

    public Execute response(ResponseEnum response) {
        this.response = response;
        return this;
    }

    /**
     * Get inputs
     *
     * @return inputs
     **/
    @Valid
    public List<Input> getInputs() {
        return inputs;
    }

    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }

    public Execute outputs(List<Output> outputs) {
        this.outputs = outputs;
        return this;
    }

    public Execute addOutputsItem(Output outputsItem) {
        this.outputs.add(outputsItem);
        return this;
    }

    /**
     * Get outputs
     *
     * @return outputs
     **/
    @NotNull
    @Valid
    public List<Output> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Output> outputs) {
        this.outputs = outputs;
    }

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

    /**
     * Get mode
     * @return mode
    **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    public ModeEnum getMode() {
      return mode;
    }

    public void setMode(ModeEnum mode) {
      this.mode = mode;
    }

    /**
     * Get response
     * @return response
    **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    public ResponseEnum getResponse() {
      return response;
    }

    public void setResponse(ResponseEnum response) {
      this.response = response;
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
        return Objects.equals(this.inputs, execute.inputs) &&
               Objects.equals(this.outputs, execute.outputs) &&
               Objects.equals(this.mode, execute.mode) &&
               Objects.equals(this.response, execute.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputs, outputs, mode, response);
    }

    @Override
    public String toString() {
        return String.format("Execute{inputs: %s, outputs: %s, mode: %s, response: %s}", inputs, outputs, mode, response);
    }
}
