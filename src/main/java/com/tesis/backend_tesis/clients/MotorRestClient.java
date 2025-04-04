package com.tesis.backend_tesis.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "motorRestClient", url = "http://localhost:5041/API/tesis")
public interface MotorRestClient {

    @PostMapping("/process/start")
    void iniciarProceso(@RequestBody Integer propuestaId);

}
