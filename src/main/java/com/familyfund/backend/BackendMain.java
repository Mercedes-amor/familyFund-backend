package com.familyfund.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling   //  habilitamos los @Scheduled. Para que verifique al cierre de mes los mini objetivos cumplidos y MaxiGoal
public class BackendMain {

	public static void main(String[] args) {
		SpringApplication.run(BackendMain.class, args);
		System.out.println("Hola Familyfund!");
	}

}
