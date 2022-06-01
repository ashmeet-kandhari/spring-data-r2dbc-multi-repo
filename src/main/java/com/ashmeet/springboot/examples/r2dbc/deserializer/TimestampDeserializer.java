package com.ashmeet.springboot.examples.r2dbc.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;

public class TimestampDeserializer extends JsonDeserializer<Instant> {

  @Override
  public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    return Instant.parse(jsonParser.getText() + "Z");

  }
}
