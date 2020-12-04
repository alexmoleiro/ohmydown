package com.alexmoleiro.healthchecker;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class HealthcheckerApplication {

	public static void main(String[] args) {
		run(HealthcheckerApplication.class, args);
	}

}
