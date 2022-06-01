package com.ashmeet.springboot.examples.r2dbc.repository;

import com.ashmeet.springboot.examples.r2dbc.model.Person;
import io.r2dbc.spi.Parameter.In;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonRepository extends R2dbcRepository<Person, UUID> {

  @Query("select json_agg(a.*) address, p.*  from person p "
      + "left join address a on p.person_id = a.person_id "
      + "where p.person_id IN "
      + "(select person_id from person "
      + "where email_id = :emailId order by :sortFields DESC limit :pageSize offset ( :pageSize * ( :pageNo - 1) ) ) "
      + "group by p.person_id")
  Flux<Person> findByEmailJoiningAddress(String emailId, Integer pageSize, Integer pageNo, String sortFields);

  Mono<Long> countByEmailId(String emailId);

  @Modifying
  @Query("update person set last_name = :lastName where email_id = :emailId")
  Mono<Integer> updateLastName(String lastName, String emailId);
}
