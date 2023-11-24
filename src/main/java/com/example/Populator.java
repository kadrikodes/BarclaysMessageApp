//package com.example;
//
//import com.example.dataaccess.MessageRepository;
//import com.example.dataaccess.PersonRepository;
//import com.example.entities.Message;
//import com.example.entities.Person;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class Populator {
//
//    MessageRepository messageRepo;
//    PersonRepository personRepo;
//
//    @Autowired
//    public Populator(MessageRepository messageRepo, PersonRepository personRepo) {
//
//        this.messageRepo = messageRepo;
//        this.personRepo = personRepo;
//    }
//
//    public void populateMessages() {
//        Message message = new Message("I hate International breaks");
//        messageRepo.save(message);
//        message = new Message("Boy! I'm so tired");
//        messageRepo.save(message);
//    }
//
//    public void populatePeople() {
//        Person person = new Person("Kadri", "kadri@2cool.com",26);
//        personRepo.save(person);
//        person = new Person("K", "k@2cool.com",25);
//        personRepo.save(person);
//        person = new Person("Ka", "ka@2cool.com",24);
//        personRepo.save(person);
//        person = new Person("Kad", "kad@2cool.com",23);
//        personRepo.save(person);
//    }
//
////    @EventListener(ContextRefreshedEvent.class)
//    public void populate() {
//        Person bill = new Person("Bill", "bill@iscooler.com", 50);
//        bill = personRepo.save(bill);
//
//        Message message = new Message("This is a message", bill);
//        this.messageRepo.save(message);
//
//        message = new Message("I love Java!!! (Coffee, not the language)", bill);
//        this.messageRepo.save(message);
//
//        Person kadri = new Person("Kadri", "kadri@2cool.com", 26);
//        kadri = personRepo.save(kadri);
//
//        Message kMessage = new Message("I'm hungry", kadri);
//        this.messageRepo.save(kMessage);
//    }
//
//}
