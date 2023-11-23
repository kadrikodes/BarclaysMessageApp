package com.example.integrationtests.messageintegrationtests;

import com.example.TestConstants;
import com.example.entities.Message;
import com.example.entities.Person;
import com.example.services.MessageService;
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

@Sql({"classpath:test-persondata.sql", "classpath:test-messagedata.sql"})
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.sql.init.mode=never"})
public class MessageWithMockHttpRequestIT {

    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @Autowired
    MessageService messageService;

    @Test
    public void testGettingAllMessagesDavesWay() throws Exception {
                (this.mockMvc.perform(MockMvcRequestBuilders.get("/messages")))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().json(TestConstants.MESSAGE_EXPECTED_JSON))
                        .andReturn();
    }

    @Test
    public void testGettingAllMessagesBillsWay() throws Exception {
         MvcResult result =
                (this.mockMvc.perform(MockMvcRequestBuilders.get("/messages")))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        String contentAsJson = result.getResponse().getContentAsString();
        Message[] actualMessages = mapper.readValue(contentAsJson, Message[].class);

        assertEquals("First test message", actualMessages[0].getContent());
        assertEquals("Second test message", actualMessages[1].getContent());
        assertEquals("Third test message", actualMessages[2].getContent());
        assertEquals("Fourth test message", actualMessages[3].getContent());

    }

    @Test
    public void testGetMessageById() throws Exception {
        long messageId = 1000;

        MvcResult result =
                (this.mockMvc.perform(MockMvcRequestBuilders.get("/messages/" + messageId)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        String contentAsJson = result.getResponse().getContentAsString();
        Message actualMessages = mapper.readValue(contentAsJson, Message.class);

        assertEquals("First test message", actualMessages.getContent());
    }

    @Test
    public void testFindMessageBySenderEmail() throws Exception {
        String senderEmail = "k@2cool.com";
        String secondSenderEmail = "kadri@2cool.com";

        MvcResult result =
                (this.mockMvc.perform(MockMvcRequestBuilders.get("/messages/sender/email/" + senderEmail)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();
        MvcResult secondResult =
                (this.mockMvc.perform(MockMvcRequestBuilders.get("/messages/sender/email/" + secondSenderEmail)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        String contentAsJson = result.getResponse().getContentAsString();
        String secondContentAsJson = secondResult.getResponse().getContentAsString();

        Message[] messages = mapper.readValue(contentAsJson, Message[].class);
        Message[] secondMessages = mapper.readValue(secondContentAsJson, Message[].class);

        assertEquals(1, messages.length);

        assertEquals("Fourth test message", messages[0].getContent());
        assertEquals(4000, messages[0].getId());
        assertEquals("Third test message", secondMessages[0].getContent());
    }

    @Test
    public void testFindMessageBySenderName() throws Exception {
        String senderName = "K";
        String secondSenderName = "Kadri";

        MvcResult result =
                (this.mockMvc.perform(MockMvcRequestBuilders.get("/messages/sender/name/" + senderName)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();
        MvcResult secondResult =
                (this.mockMvc.perform(MockMvcRequestBuilders.get("/messages/sender/name/" + secondSenderName)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        String contentAsJson = result.getResponse().getContentAsString();
        String secondContentAsJson = secondResult.getResponse().getContentAsString();

        Message[] messages = mapper.readValue(contentAsJson, Message[].class);
        Message[] secondMessages = mapper.readValue(secondContentAsJson, Message[].class);

//        assertEquals(1, messages.length);

        assertEquals("Fourth test message", messages[0].getContent());
        assertEquals("Third test message", secondMessages[0].getContent());
    }

    @Test
    void testAddingAMessage() throws Exception {
        int originalNumberOfPersons = getNumberOfMessages();
        Person sender = new Person("Kadri", "kadri@2cool.com",26);
        Message testMessage = new Message("First test message", sender);

        String json = mapper.writeValueAsString(testMessage);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/addMessage");
        MvcResult result = mockMvc.perform((requestBuilder)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        Message message = mapper.readValue(contentAsString, Message.class);

        assertEquals(originalNumberOfPersons + 1, getNumberOfMessages());
        assertTrue(checkIfOnList(message.getContent(), message.getSender()));
    }

    private int getNumberOfMessages() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/messages");
        ResultActions resultActions = mockMvc.perform((requestBuilder)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("application/json"))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        Message[] messages = mapper.readValue(contentAsString, Message[].class);
        return messages.length;
    }

    private boolean checkIfOnList(String content, Person sender) throws Exception {
        boolean isOnList = false;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/messages");
        ResultActions resultActions = mockMvc.perform((requestBuilder)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("application/json"))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        Message[] messages = mapper.readValue(contentAsString, Message[].class);

        for (Message message : messages) {
            if (message.getContent().equals(content) && message.getSender().equals(sender)) {
                isOnList = true;
            }
        }
        return isOnList;
    }



}
