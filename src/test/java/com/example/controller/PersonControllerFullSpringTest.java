package com.example.controller;

import com.example.controller.PersonController;
import com.example.entities.Person;
import com.example.services.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
@AutoConfigureMockMvc
public class PersonControllerFullSpringTest {

    @MockBean
    PersonService mockPersonService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void testServiceCalledFor_getAllPeople() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/persons");

        mockMvc.perform(requestBuilder);

        verify(mockPersonService, times(1)).findAll();
    }

    @Test
    void testGetPersonById() throws Exception {
        Long personId = 1L;

        Person person = new Person("Kadri", "kadri@2cool.com",26);
        when(mockPersonService.getPersonById(personId)).thenReturn(person);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/persons/" + personId.toString());
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(mockPersonService, times(1)).getPersonById(personId);
    }

    @Test
    void testGetPeopleByIdBadRequest() throws Exception {
        long personId = 0;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/persons/" + personId);
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testAddingAPerson() throws Exception {
        Person testPerson = new Person("Kadri", "kadri@2cool.com",26);

        String json = mapper.writeValueAsString(testPerson);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/addPerson");
        MvcResult result = mockMvc.perform((requestBuilder)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isOk())
                        .andReturn();

        verify(mockPersonService, times(1)).addPerson(any(Person.class));
    }


}
