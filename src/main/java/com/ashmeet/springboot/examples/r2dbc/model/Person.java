package com.ashmeet.springboot.examples.r2dbc.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable {

  @Id
  @Column("person_id")
  private UUID id;
  @Column
  private String firstName;
  @Column
  private String lastName;
  @Column
  private String emailId;
  @Column
  private Instant createdTimestamp;
  @Column
  private List<Address> address;
}
