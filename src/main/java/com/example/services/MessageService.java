package com.example.services;

import com.example.dataaccess.MessageRepository;
import com.example.dataaccess.PersonRepository;
import com.example.entities.Message;
import com.example.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private PersonRepository personRepository;

//    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Autowired
    public MessageService(MessageRepository messageRepository, PersonRepository personRepository) {
        this.messageRepository = messageRepository;
        this.personRepository = personRepository;
    }


    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    public Message getMessageById(long messageId) {
       Optional<Message> message = messageRepository.findById(messageId);
       return message.orElse(null);
    }

    public List<Message> getMessagesBySenderEmail(String email) {
        return messageRepository.findMessagesBySenderEmail(email);
    }

    public List<Message> getMessagesBySenderName(String name) {
        return messageRepository.findMessagesBySenderName(name);
    }

    public Message addMessage(Message message) {
        Person sender = message.getSender();

        // Check if the sender is not null
        if (sender != null) {
            String name = sender.getName();
            Optional<Person> prospectivePerson = personRepository.findPersonByName(name);

            if (prospectivePerson.isEmpty()) {
                Person savedSender = personRepository.save(sender);
                message.setSender(savedSender);
            } else {
                message.setSender(prospectivePerson.get());
            }

            return messageRepository.save(message);
        } else {
            // Handle the case where sender is null
            throw new IllegalArgumentException("Sender cannot be null");
        }

    }
}
