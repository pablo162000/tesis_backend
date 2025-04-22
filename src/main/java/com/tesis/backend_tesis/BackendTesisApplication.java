package com.tesis.backend_tesis;

import com.tesis.backend_tesis.service.ICarreraService;
import com.tesis.backend_tesis.service.IPropuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class BackendTesisApplication implements CommandLineRunner {

	@Autowired
	private IPropuestaService  propuestaService;

	public static void main(String[] args) {
		SpringApplication.run(BackendTesisApplication.class, args);




	}

	@Override
	public void run(String... args) throws Exception {


		System.out.println(this.propuestaService.diferentesCarreras(1,3,1));


	}
}
