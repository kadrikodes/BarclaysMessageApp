package com.example.controller;

import com.example.entities.Message;
import com.example.services.MessageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MessageControllerSomeSpringTest {

    @Autowired
    MessageController uut;

    @MockBean
    MessageService mockMessageService;

    @Test
    public void getObjectsFromContextAndTestBehavior() {
        List<Message> messages = uut.getAllMessages();
        Mockito.verify(mockMessageService, Mockito.times(1)).findAll();
    }

    @Test
    void testGetMessageById() {
        Long messageId = 1L;
        try {
            this.uut.getMessageById(messageId);
        } catch (ResponseStatusException rse) {}

        verify(mockMessageService, Mockito.times(1)).getMessageById(messageId);
    }

    @Test
    void testGetMessagesByIdBadRequest() {
        when(mockMessageService.getMessageById(0)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            this.uut.getMessageById(0);
        });
    }
}
