package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BarclayMessageApplication {
	public static void main(String[] args) {

		ApplicationContext applicationContext = SpringApplication.run(BarclayMessageApplication.class, args);

//		Populator populator = applicationContext.getBean(Populator.class);
//		populator.populateMessages();
//		populator.populatePeople();
//		populator.populate();
	}

}
