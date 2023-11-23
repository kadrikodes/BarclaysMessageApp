package com.example.controller.personcontrollertest;

import com.example.controller.PersonController;
import com.example.entities.Person;
import com.example.services.PersonService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PersonControllerSomeSpringTest {

    @Autowired
    PersonController uut;

    @MockBean
    PersonService mockPersonService;

    @Test
    public void getObjectsFromContextAndTestBehavior() {
        List<Person> people = uut.getAllPeople();
        Mockito.verify(mockPersonService, Mockito.times(1)).findAll();
    }

    @Test
    void testGetPersonById() {
        Long personId = 1L;
        try {
            this.uut.getPersonById(personId);
        } catch (ResponseStatusException rse) {}

        verify(mockPersonService, times(1)).getPersonById(personId);
    }

    @Test
    void testGetMessagesByIdBadRequest() {
        when(mockPersonService.getPersonById(0)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            this.uut.getPersonById(0);
        });
    }

    @Test
    void testAddingAPerson() {
        Person testPerson = new Person("Kadri", "kadri@2cool.com",26);
        Person person = uut.addPerson(testPerson);

        verify(mockPersonService, times(1)).addPerson(any(Person.class));
    }

}
