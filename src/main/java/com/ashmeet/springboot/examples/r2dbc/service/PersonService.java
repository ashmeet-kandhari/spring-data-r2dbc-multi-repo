package com.ashmeet.springboot.examples.r2dbc.service;

import com.ashmeet.springboot.examples.r2dbc.model.AppConstants;
import com.ashmeet.springboot.examples.r2dbc.model.Person;
import com.ashmeet.springboot.examples.r2dbc.repository.PersonRepository;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Slf4j
@Service
public class PersonService {

  private final PersonRepository personRepository;


  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public Mono<Page<Person>> getPersonDetails(String emailId, Integer pageSize, Integer pageNo, String sortFields) {
    log.info("Using read context or read db url to get results");

    Context routingContext = Context.of(AppConstants.SUBSCRIPTION_CONTEXT, AppConstants.READ);

    Mono<Page<Person>> results;
    String email = emailId.toLowerCase();

    results = this.personRepository.findByEmailJoiningAddress(email, pageSize, pageNo, sortFields)
        .collectList()
        .zipWith(this.personRepository.countByEmailId(email))
        .contextWrite(routingContext)
        .map(objects -> new PageImpl<>(objects.getT1(), PageRequest.of(pageNo, pageSize, Sort.by(sortFields)), objects.getT2()));

    return results;
  }

  public Mono<Map<String, Integer>> updateLastName(String emailId, String lastName) {
    log.info("Using write context or write db url to update");
    Context routingContext = Context.of(AppConstants.SUBSCRIPTION_CONTEXT, AppConstants.WRITE);
    Mono<Map<String, Integer>> results;
    String email = emailId.toLowerCase();

    results = this.personRepository.updateLastName(lastName, email)
        .contextWrite(routingContext)
        .map(objects -> Collections.singletonMap("updated", objects));

    return results;
  }

}
