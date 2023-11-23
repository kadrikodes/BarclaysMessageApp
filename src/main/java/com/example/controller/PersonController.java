package com.example.controller;

import com.example.entities.Person;
import com.example.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class PersonController {


    PersonService personService;
    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/persons")
    public List<Person> getAllPeople() {
        return personService.findAll();
    }

    @GetMapping("/persons/create")
    public List<Person> createPeopleList() {
        return personService.findAll();
    }

    @GetMapping("/persons/{personId}")
    public Person getPersonById(@PathVariable long personId) {
        Person person = personService.getPersonById(personId);

        if (person == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");

        return person;
    }

    @PostMapping("/addPerson")
    public Person addPerson(@RequestBody Person person) {
        return personService.addPerson(person);
    }

}
