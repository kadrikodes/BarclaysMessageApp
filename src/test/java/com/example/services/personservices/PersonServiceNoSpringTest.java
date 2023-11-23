package com.example.services.personservices;

import com.example.TestUtilities;
import com.example.dataaccess.PersonRepository;
import com.example.entities.Person;
import com.example.services.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonServiceNoSpringTest {

    PersonRepository mockRepo;
    PersonService personService;

    TestUtilities testUtilities;

    @BeforeEach
    void beforeEach() {
        mockRepo = mock(PersonRepository.class);
        personService = new PersonService(mockRepo);
        testUtilities = new TestUtilities();
    }

    @Test
    void findAll() {
        List<Person> people = testUtilities.createPersonList();
        when(mockRepo.findAll()).thenReturn(people);
        List<Person> actualPeople = personService.findAll();

        Assertions.assertEquals(people, actualPeople);
    }

    @Test
    void testRepoCalled() {
        List<Person> actualPerson = personService.findAll();
        verify(mockRepo, times(1)).findAll();
    }

    @Test
    void testGetPersonByIdOptionalEmpty() {
        long personId = 1L;
        Optional<Person> optionalPerson = Optional.empty();
        when(mockRepo.findById(personId)).thenReturn(optionalPerson);

        Person actualPerson = personService.getPersonById(personId);

        assertNull(actualPerson);
    }

    @Test
    void testGetPersonByIdOptionalNotEmpty() {
        long personId = 1L;
        Optional<Person> optionalPerson = Optional.of(new Person("Kadri", "kadri@2cool.com",26));

        when(mockRepo.findById(personId)).thenReturn(optionalPerson);

        Person actualPerson = personService.getPersonById(personId);

        verify(mockRepo, times(1)).findById(personId);

        assertNotNull(actualPerson);

    }

    @Test
    void testGetPersonByIdOptionalNotEmptyDavesWay() {
        long personId = 1L;

        Person person = new Person("Kadri", "kadri@2cool.com",26);

        when(mockRepo.findById(any())).thenReturn(Optional.of(person));

        Person actualPerson = personService.getPersonById(personId);

        assertEquals(person.getName(), actualPerson.getName());
        assertEquals(person.getId(), actualPerson.getId());

    }

    @Test
    void testAddingAPerson() {
        Person person = new Person("Kadri", "kadri@2cool.com",26);
        personService.addPerson(person);

        verify(mockRepo, times(1)).save(person);
    }




}
