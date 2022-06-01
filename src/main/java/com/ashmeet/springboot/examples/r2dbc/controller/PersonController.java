package com.ashmeet.springboot.examples.r2dbc.controller;


import com.ashmeet.springboot.examples.r2dbc.model.Person;
import com.ashmeet.springboot.examples.r2dbc.service.PersonService;
import java.util.Map;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/persons")
public class PersonController {

  private final PersonService personService;

  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @GetMapping(produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  public Mono<Page<Person>> getPersonDetails(@RequestParam(defaultValue = "1") @Min(1) Integer pageNo,
      @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer pageSize,
      @RequestParam(defaultValue = "created_timestamp") String sortFields,
      @RequestParam String emailId) {

    return personService.getPersonDetails(emailId, pageSize, pageNo, sortFields);

  }

  @PutMapping(value = "/lastName",consumes = MimeTypeUtils.APPLICATION_JSON_VALUE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  public Mono<Map<String, Integer>> updateLastName(@RequestBody Person person, @RequestParam String emailId) {
    return personService.updateLastName(emailId, person.getLastName());
  }


}
