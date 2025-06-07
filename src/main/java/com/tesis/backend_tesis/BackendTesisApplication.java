package com.tesis.backend_tesis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class BackendTesisApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(BackendTesisApplication.class, args);


	}

	@Override
	public void run(String... args) throws Exception {



	}
}
