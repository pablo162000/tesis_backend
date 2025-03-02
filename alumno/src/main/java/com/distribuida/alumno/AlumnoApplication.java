package com.distribuida.alumno;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AlumnoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AlumnoApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println("Ejecucion Correcta de microservicio Alumno");
    }
}
