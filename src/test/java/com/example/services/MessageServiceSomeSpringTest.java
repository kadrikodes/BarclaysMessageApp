package com.example.services;

import com.example.TestUtilities;
import com.example.dataaccess.MessageRepository;
import com.example.entities.Message;
import com.example.services.MessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MessageServiceSomeSpringTest {

    @Autowired
    MessageService messageService;
    TestUtilities testUtilities = new TestUtilities();


    @MockBean
    MessageRepository mockRepo;

    @BeforeEach
    void beforeEach() {
        reset(mockRepo);
    }

    @Test
    void findAllWithSpringDi() {
        List<Message> messages = testUtilities.createMessageList();
        when(mockRepo.findAll()).thenReturn(messages);
        List<Message> actualMessages = messageService.findAll();

        Assertions.assertEquals(messages, actualMessages);
    }

    @Test
    void testRepoCalled() {
        List<Message> actualMessages = messageService.findAll();
        verify(mockRepo, times(1)).findAll();
    }

    @Test
    void testGetMessagesByIdOptionalEmpty() {
        long messageId = 1;
        Optional<Message> optionalMessage = Optional.empty();
        when(mockRepo.findById(messageId)).thenReturn(optionalMessage);

        Message actualMessage = messageService.getMessageById(messageId);

        assertNull(actualMessage);
    }

    @Test
    void testGetMessageByIdOptionalNotEmpty() {
        long messageId = 1;
        Optional<Message> optionalMessage = Optional.of(new Message("Howdy"));
        when(mockRepo.findById(messageId)).thenReturn(optionalMessage);

        Message actualMessage = messageService.getMessageById(messageId);

        // Assert that the repository's findById method is called with the correct messageId
        verify(mockRepo, times(1)).findById(messageId);

        // Assert that the actualMessage is not null
        assertNotNull(actualMessage);


    }

    @Test
    void testGetMessageByIdOptionalNotEmptyDavesWay() {
        long messageId = 1;

        Message message = new Message("Howdy");
        when(mockRepo.findById(any())).thenReturn(Optional.of(message));

        Message actualMessage = messageService.getMessageById(messageId);

        assertEquals(message.getContent(), actualMessage.getContent());
        assertEquals(message.getId(), actualMessage.getId());

    }

}
