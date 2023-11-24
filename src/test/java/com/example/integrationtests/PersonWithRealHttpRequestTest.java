package com.example.integrationtests;

import com.example.entities.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PersonWithRealHttpRequestTest {

    ObjectMapper mapper;
    CloseableHttpClient httpClient;
    HttpResponse response;
    HttpResponse firstTestResponse;
    HttpResponse secondResponse;
    HttpGet getRequest;
    HttpPost request;
    HttpUriRequest uriRequest;

    @BeforeEach
    public void setUp() {
        uriRequest = new HttpGet("http://localhost:8080/persons");
        mapper = new ObjectMapper();
        httpClient = HttpClientBuilder.create().build();
        getRequest = new HttpGet("http://localhost:8080/persons");
        request = new HttpPost("http://localhost:8080/addPerson");

    }

    @AfterEach
    public void tearDown() throws IOException {
        httpClient.close();
    }


    @Test
    @Disabled
    public void testGettingAllPeople() throws IOException {
        firstTestResponse = HttpClientBuilder.create().build().execute(uriRequest);

        Person[] people = mapper.readValue(firstTestResponse.getEntity().getContent(), Person[].class);

        assertEquals("K", people[0].getName());
        assertEquals("Ka", people[1].getName());
        assertEquals("Kad", people[2].getName());
        assertEquals("Kadri", people[3].getName());

        }

    @Test
    @Disabled
    public void testPersonById() throws Exception {
        long personId = 10;
        long secondPersonId = 20;

        HttpUriRequest request = new HttpGet("http://localhost:8080/persons/" + personId);
        HttpUriRequest secondRequest = new HttpGet("http://localhost:8080/persons/" + secondPersonId);

        response = HttpClientBuilder.create().build().execute(request);
        secondResponse = HttpClientBuilder.create().build().execute(secondRequest);

        Person person = mapper.readValue(response.getEntity().getContent(), Person.class);
        Person secondPerson = mapper.readValue(secondResponse.getEntity().getContent(), Person.class);

        assertEquals("K", person.getName());
        assertEquals("Ka", secondPerson.getName());

    }

    @Test
    @Disabled
    void testAddingAPerson() throws Exception {
        Person testPerson = new Person("Kadrii", "kadrii@2cool.com",26);

        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(testPerson);
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);

        request.setEntity(entity);

        httpClient.execute(request);

        response = HttpClientBuilder.create().build().execute(request);

        assertTrue(checkIfOnList(testPerson.getName(), testPerson.getEmail(), testPerson.getAge()));

    }

    private boolean checkIfOnList(String name, String email, int age) throws IOException {
        boolean isOnList = false;

        response = HttpClientBuilder.create().build().execute(getRequest);

        Person[] persons = mapper.readValue(response.getEntity().getContent(), Person[].class);

        for (Person person : persons) {
            if (person.getName().equals(name)&&person.getEmail().equals(email)&&person.getAge()==age) {
                isOnList = true;
            }
        }
        return isOnList;
    }
}
