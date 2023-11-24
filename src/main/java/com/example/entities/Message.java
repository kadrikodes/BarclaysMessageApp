package com.example.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Message {
    @Id
    @GeneratedValue
    private Long Id;

    private String content;

    @ManyToOne
    private Person sender;

    public Message(String content) {
        this.content = content;
    }

    public Message(String content, Person sender) {
        this.content = content;
        this.sender = sender;
    }

    public Message()  {}
//    public Long getId() {
//        return Id;
//    }

    public String getContent() {
        return content;
    }

//    public void setContent(String content) {
//        this.content = content;
//    }
    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }




}
