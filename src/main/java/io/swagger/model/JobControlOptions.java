package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets jobControlOptions
 */
public enum JobControlOptions {
  
  SYNC_EXECUTE("sync-execute"),
  
  ASYNC_EXECUTE("async-execute");

  private String value;

  JobControlOptions(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static JobControlOptions fromValue(String text) {
    for (JobControlOptions b : JobControlOptions.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

