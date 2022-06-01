package com.ashmeet.springboot.examples.r2dbc.model;

import com.ashmeet.springboot.examples.r2dbc.deserializer.TimestampDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {

  @JsonProperty("id")
  private UUID addressId;
  private String city;
  private String state;
  @JsonProperty("created_timestamp")
  @JsonDeserialize(using = TimestampDeserializer.class)
  private Instant createdTimestamp;

}
