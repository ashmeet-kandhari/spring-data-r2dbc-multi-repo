package com.ashmeet.springboot.examples.r2dbc.deserializer;

import com.ashmeet.springboot.examples.r2dbc.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import org.springframework.core.convert.converter.Converter;

public class PgRowDeserializer implements Converter<Row, Person> {

  private final ObjectMapper objectMapper;

  public PgRowDeserializer(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public Person convert(Row source) {
    Person person = new Person();
    person.setId(source.get("person_id", UUID.class));
    person.setFirstName(source.get("first_name", String.class));
    person.setLastName(source.get("last_name", String.class));
    person.setEmailId(source.get("email_id", String.class));
    person.setCreatedTimestamp(source.get("created_timestamp", Instant.class));
    try {
      person.setAddress(objectMapper.readValue(Objects.requireNonNull(source.get("address", Json.class)).asString(), new TypeReference<>() {
      }));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    return person;
  }
}
