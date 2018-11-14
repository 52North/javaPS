package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.DataType;
import io.swagger.model.Format;
import io.swagger.model.TransmissionMode;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Output
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-14T13:20:59.832Z[GMT]")

public class Output extends DataType  {
  @JsonProperty("transmissionMode")
  private TransmissionMode transmissionMode = null;

  public Output transmissionMode(TransmissionMode transmissionMode) {
    this.transmissionMode = transmissionMode;
    return this;
  }

  /**
   * Get transmissionMode
   * @return transmissionMode
  **/
  @ApiModelProperty(value = "")

  @Valid

  public TransmissionMode getTransmissionMode() {
    return transmissionMode;
  }

  public void setTransmissionMode(TransmissionMode transmissionMode) {
    this.transmissionMode = transmissionMode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Output output = (Output) o;
    return Objects.equals(this.transmissionMode, output.transmissionMode) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transmissionMode, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Output {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    transmissionMode: ").append(toIndentedString(transmissionMode)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

