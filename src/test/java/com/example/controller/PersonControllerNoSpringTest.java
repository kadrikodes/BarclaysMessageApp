package com.example.controller;

import com.example.controller.PersonController;
import com.example.entities.Person;
import com.example.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PersonControllerNoSpringTest {

    PersonService mockPersonService;
    PersonController personController;

    @BeforeEach
    void beforeEach() {
        this.mockPersonService = mock(PersonService.class);
        this.personController = new PersonController(mockPersonService);
    }

    @Test
    void getAllPeople() {

        PersonService mockPersonService = mock(PersonService.class);

        PersonController personController = new PersonController(mockPersonService);
        List<Person> people = personController.getAllPeople();

        Mockito.verify(mockPersonService, times(1)).findAll();
    }

    @Test
    void testGetPersonById() {
        Long personId = 1L;
//        try {
//            this.personController.getPersonById(personId);
//        } catch (ResponseStatusException rse) {}
        when(mockPersonService.getPersonById(personId)).thenReturn(new Person("Kadri", "kadri@2cool.com",26));
        personController.getPersonById(personId);

        verify(mockPersonService, times(1)).getPersonById(personId);
    }

    @Test
    void testGetMessagesByIdBadRequest() {
        when(mockPersonService.getPersonById(0)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            this.personController.getPersonById(0);
        });
    }

    @Test
    public void testAddingAPerson() {
        Person person = new Person();
        personController.addPerson(person);

        verify(mockPersonService, times(1)).addPerson(person);
    }
}