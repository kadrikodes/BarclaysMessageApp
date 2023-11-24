package com.example.controller;

import com.example.entities.Message;
import com.example.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class MessageController {


    MessageService messageService;
    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.findAll();
    }

    @GetMapping("/messages/{messageId}")
    public Message getMessageById(@PathVariable long messageId) {
        Message message = messageService.getMessageById(messageId);

        if (message == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");

        return message;

    }

    @GetMapping("messages/sender/email/{email}")
    public List<Message> getMessageBySenderEmail(@PathVariable String email) {
        return messageService.getMessagesBySenderEmail(email);
    }

    @GetMapping("messages/sender/name/{name}")
    public List<Message> getMessageBySenderName(@PathVariable String name) {
        return messageService.getMessagesBySenderName(name);
    }

    @PostMapping("/addMessage")
    public Message addMessage(@RequestBody Message message) {
        return messageService.addMessage(message);
    }




}
