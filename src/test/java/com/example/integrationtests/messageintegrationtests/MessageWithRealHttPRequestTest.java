package com.example.integrationtests.messageintegrationtests;

import com.example.entities.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MessageWithRealHttPRequestTest {

    ObjectMapper mapper = new ObjectMapper();


    @Test
    public void testGettingAllMessages() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:8080/messages");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        Message[] messages = mapper.readValue(response.getEntity().getContent(), Message[].class);

        assertEquals("First test message", messages[0].getContent());
        assertEquals("Second test message", messages[1].getContent());
        assertEquals("Third test message", messages[2].getContent());
        assertEquals("Fourth test message", messages[3].getContent());

    }

    @Test
    public void testGetMessageById() throws Exception {
        long messageId = 100;

        HttpUriRequest request = new HttpGet("http://localhost:8080/messages/" + messageId);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        Message messages = mapper.readValue(response.getEntity().getContent(), Message.class);

        assertEquals("First test message", messages.getContent());

    }

    @Test
    public void testFindMessagesBySenderEmail() throws IOException {
        String senderEmail = "kadri@2cool.com";

        HttpUriRequest request = new HttpGet("http://localhost:8080/messages/sender/email/" + senderEmail);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        Message[] messages = mapper.readValue(response.getEntity().getContent(), Message[].class);

        assertEquals("Fourth test message", messages[0].getContent());
    }

    @Test
    public void testFindMessagesBySenderName() throws IOException {
        String senderName = "Kadri";

        HttpUriRequest request = new HttpGet("http://localhost:8080/messages/sender/name/" + senderName);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        Message[] messages = mapper.readValue(response.getEntity().getContent(), Message[].class);

        assertEquals("Fourth test message", messages[0].getContent());
    }



}
