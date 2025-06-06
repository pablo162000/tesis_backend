package com.tesis.backend_tesis.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//@FeignClient(name = "autenticacionRestClient"
//            ,url = "http://localhost:5050/API/tesis/")

@FeignClient(name = "autenticacion"
            ,url  = "${auth.url}")
public interface AutenticacionRestClient {


    @PostMapping(value = "validacion/generar-token")
    ResponseEntity<String> crearToken(@RequestParam(value = "correo") String correo);

    @GetMapping(value = "validacion/validar-token")
    ResponseEntity<String> validarToken(@RequestParam(value = "token") String token);

    @PostMapping(value = "sesion/generar-token")
    ResponseEntity<String> crearTokenSesion(@RequestParam(value = "username") String username,
                                            @RequestParam(value = "roles") List<String> roles);


}
