package com.example.integrationtests;

import com.example.TestConstants;
import com.example.entities.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("classpath:test-persondata.sql")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.sql.init.mode=never"})
public class PersonWithMockHttpRequestIT {

    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;

    @Test
    public void testGettingAllPeopleDavesWay() throws Exception {
        MvcResult result =
                (this.mockMvc.perform(MockMvcRequestBuilders.get("/persons")))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().json(TestConstants.PERSON_EXPECTED_JSON))
                        .andReturn();

    }

    @Test
    public void testGettingAllPeopleBillsWay() throws Exception {
        MvcResult result =
                (this.mockMvc.perform(MockMvcRequestBuilders.get("/persons")))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        String contentAsJson = result.getResponse().getContentAsString();
        Person[] actualPeople = mapper.readValue(contentAsJson, Person[].class);

        assertEquals("Ka", actualPeople[0].getName());
        assertEquals("Kad", actualPeople[1].getName());
        assertEquals("Kadri", actualPeople[2].getName());
        assertEquals("K", actualPeople[3].getName());

    }

    @Test
    public void testGetPersonById() throws Exception {
        long firstPersonId = 10;
        long secondPersonId = 20;

        MvcResult result =
                (this.mockMvc.perform(MockMvcRequestBuilders.get("/persons/" + firstPersonId)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();
        MvcResult resultTwo =
                (this.mockMvc.perform(MockMvcRequestBuilders.get("/persons/" + secondPersonId)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        String contentAsJson = result.getResponse().getContentAsString();
        String secondContentAsJson = resultTwo.getResponse().getContentAsString();

        Person actualPerson = mapper.readValue(contentAsJson, Person.class);
        Person secondActualPerson = mapper.readValue(secondContentAsJson, Person.class);

        assertEquals("Ka", actualPerson.getName());
        assertEquals("Kad", secondActualPerson.getName());
    }

    @Test
    void testAddingAPerson() throws Exception {
        int originalNumberOfPersons = getNumberOfPersons();
        Person testPerson = new Person("Kadri", "kadri@2cool.com",26);

        String json = mapper.writeValueAsString(testPerson);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/addPerson");
        MvcResult result = mockMvc.perform((requestBuilder)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        Person person = mapper.readValue(contentAsString, Person.class);

        assertEquals(originalNumberOfPersons + 1, getNumberOfPersons());
        assertTrue(checkIfOnList(person.getName(), person.getEmail(), person.getAge()));
    }

    private int getNumberOfPersons() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/persons");
        ResultActions resultActions = mockMvc.perform((requestBuilder)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("application/json"))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        Person[] persons = mapper.readValue(contentAsString, Person[].class);
        return persons.length;
    }

    private boolean checkIfOnList(String name, String email, int age) throws Exception {
        boolean isOnList = false;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/persons");
        ResultActions resultActions = mockMvc.perform((requestBuilder)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("application/json"))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        Person[] persons = mapper.readValue(contentAsString, Person[].class);

        for (Person person : persons) {
            if (person.getName().equals(name)&&person.getEmail().equals(email)&&person.getAge()==age) {
                isOnList = true;
            }
        }
        return isOnList;
    }

}
