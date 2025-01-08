package com.tesis.backend_tesis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class BackendTesisApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BackendTesisApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Ejecucion Correcta");
	}
}
