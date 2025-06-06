package com.tesis.backend_tesis.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

//@FeignClient(name = "motorRestClient", url = "http://localhost:5041/API/tesis")
@FeignClient(name = "motor-procesos", url  = "${motor.url}")
public interface MotorRestClient {

    @PostMapping("process/iniciar")
    ResponseEntity<String> iniciarProceso(
            @RequestParam("propuestaId") Integer propuestaId,
            @RequestParam(value = "idEstudiante1", required = false) Integer idEstudiante1,
            @RequestParam("idDireccion") Integer idDireccion

    );


    @PostMapping("process/tasks/complete")
    ResponseEntity<String> completarTarea(
            @RequestParam("taskId") String taskId,
            @RequestBody Map<String, Object> variables);
}
