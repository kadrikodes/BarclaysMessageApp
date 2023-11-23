package com.example;

import com.example.entities.Message;
import com.example.entities.Person;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TestUtilities {

    public ArrayList<Message> createMessageList() {
        ArrayList<Message> messages = new ArrayList<>();

        Message firstMessage = new Message("First Message");
        messages.add(firstMessage);
        Message secondMessage = new Message("Second Message");
        messages.add(secondMessage);
        Message thirdMessage = new Message("Third Message");
        messages.add(thirdMessage);

        return messages;
    }

    public List<Person> createPersonList() {
        ArrayList<Person> people = new ArrayList<>();

        Person firstPerson = new Person("Kadri", "kadri@2cool.com",26);
        people.add(firstPerson);
        Person secondPerson = new Person("Rais", "rais@2cool.com",22);
        people.add(secondPerson);
        Person thirdPerson = new Person("Divin", "divin@2cool.com",28);
        people.add(thirdPerson);
        Person fourthPerson = new Person("Sam", "sam@2cool.com",27);
        people.add(fourthPerson);
        Person fifthPerson = new Person("Esra", "esra@2cool.com",2);
        people.add(fifthPerson);

        return people;
    }
}
