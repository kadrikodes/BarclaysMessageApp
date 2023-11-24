package com.example.controller;

import com.example.controller.MessageController;
import com.example.entities.Message;
import com.example.services.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MessageControllerNoSpringTest {

    MessageService mockMessageService;
    MessageController messageController;

    @BeforeEach
    void beforeEach() {
        this.mockMessageService = mock(MessageService.class);
        this.messageController = new MessageController(mockMessageService);
    }

    @Test
    void getAllMessages() {
        messageController.getAllMessages();
        Mockito.verify(mockMessageService, times(1)).findAll();
    }

    @Test
    void testGetMessageByIdDavesWay() {
        Long messageId = 1L;
//        try {
//            this.messageController.getMessageById(messageId);
//        } catch (ResponseStatusException rse) {}

        when(mockMessageService.getMessageById(messageId)).thenReturn(new Message("howdy"));
        this.messageController.getMessageById(messageId);

        verify(mockMessageService, times(1)).getMessageById(messageId);
    }


    @Test
    void testGetMessagesByIdBadRequest() {
        when(mockMessageService.getMessageById(0)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            this.messageController.getMessageById(0);
        });
    }
}