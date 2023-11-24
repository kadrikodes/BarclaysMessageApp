package com.example.controller;

import com.example.TestUtilities;

import com.example.entities.Message;
import com.example.services.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
@AutoConfigureMockMvc
class MessageControllerFullSpringTest {

    @MockBean
    MessageService mockMessageService;

    @Autowired
    MockMvc mockMvc;
    TestUtilities testUtilities = new TestUtilities();

    @Test
    void testServiceCalledFor_getAllMessages() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/messages");

        mockMvc.perform(requestBuilder);

        verify(mockMessageService, times(1)).findAll();
    }

    @Test
    void testGetAllMessagesFromHttpRequest() throws Exception {
        List<Message> messages = testUtilities.createMessageList();

        when(mockMessageService.findAll()).thenReturn(messages);

        ResultActions resultActions = this.mockMvc.perform(
                MockMvcRequestBuilders.get("/messages"));
        resultActions.andExpect(status().isOk());


        verify(mockMessageService, times(1)).findAll();
    }

    @Test
    void testGetMessageById() throws Exception {
        Long messageId = 1L;

        Message message = new Message("Howdy");
        when(mockMessageService.getMessageById(messageId)).thenReturn(message);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/messages/" + messageId.toString());
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(mockMessageService, times(1)).getMessageById(messageId);
    }

    @Test
    void testGetMessagesByIdBadRequest() throws Exception {
        long messageId = 0;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/messages/" + messageId);
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();
    }

}