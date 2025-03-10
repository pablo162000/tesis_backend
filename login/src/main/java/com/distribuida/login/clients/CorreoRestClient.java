package com.distribuida.login.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "correoRestClient", url = "http://localhost:8282/API/tesis/")
public interface CorreoRestClient {


    @PostMapping("/correo/registro")
    String registrarUsuario(@RequestParam String usuario, @RequestParam String correo,  @RequestParam String enlaceVerificaion);


}
