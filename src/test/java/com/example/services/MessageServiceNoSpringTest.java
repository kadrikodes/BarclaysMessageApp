package com.example.services;

import com.example.TestUtilities;
import com.example.dataaccess.MessageRepository;
import com.example.entities.Message;
import com.example.entities.Person;
import com.example.services.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MessageServiceNoSpringTest {
    MessageRepository mockRepo;
    MessageService messageService;

    TestUtilities testUtilities;

    @BeforeEach
    void beforeEach() {
        mockRepo = mock(MessageRepository.class);
        messageService = new MessageService(mockRepo);
        testUtilities = new TestUtilities();
    }

    @Test
    void findAll() {
        List<Message> messages = testUtilities.createMessageList();
        when(mockRepo.findAll()).thenReturn(messages);
        List<Message> actualMessages = messageService.findAll();

        assertEquals(messages, actualMessages);
    }

    @Test
    void testRepoCalled() {
        List<Message> actualMessages = messageService.findAll();
        verify(mockRepo, times(1)).findAll();
    }

    @Test
    void testGetMessagesByIdOptionalEmpty() {
        long messageId = 1L;
        Optional<Message> optionalMessage = Optional.empty();
        when(mockRepo.findById(messageId)).thenReturn(optionalMessage);

        Message actualMessage = messageService.getMessageById(messageId);

        assertNull(actualMessage);
    }

    @Test
    void testGetMessageByIdOptionalNotEmpty() {
        long messageId = 1L;
        Optional<Message> optionalMessage = Optional.of(new Message("Howdy"));

        when(mockRepo.findById(messageId)).thenReturn(optionalMessage);

        Message actualMessage = messageService.getMessageById(messageId);

        verify(mockRepo, times(1)).findById(messageId);

        assertNotNull(actualMessage);

    }

    @Test
    void testGetMessageByIdOptionalNotEmptyDavesWay() {
        long messageId = 1L;

        Message message = new Message("Howdy");

        when(mockRepo.findById(any())).thenReturn(Optional.of(message));

        Message actualMessage = messageService.getMessageById(messageId);

       assertEquals(message.getContent(), actualMessage.getContent());
       assertEquals(message.getId(), actualMessage.getId());

    }

    @Test
    void testFindMessageByName() {
        String name = "Kadri";
        String email = "kadri@email.com";
        int age = 26;

        Person sender = new Person(name, email, age);

        Message message = new Message("Howdy", sender);

        List<Message> messages = new ArrayList<>();
        messages.add(message);

        when(mockRepo.findMessagesBySenderName(name)).thenReturn(messages);

        messageService.getMessagesBySenderName(name);

        verify(mockRepo, times(1)).findMessagesBySenderName(name);
    }

    @Test
    void testFindMessageByEmail() {
        String name = "Kadri";
        String email = "kadri@email.com";
        int age = 26;

        Person sender = new Person(name, email, age);

        Message message = new Message("Howdy", sender);

        List<Message> messages = new ArrayList<>();
        messages.add(message);

        when(mockRepo.findMessagesBySenderEmail(email)).thenReturn(messages);

        messageService.getMessagesBySenderEmail(email);

        verify(mockRepo, times(1)).findMessagesBySenderEmail(email);
    }



}
