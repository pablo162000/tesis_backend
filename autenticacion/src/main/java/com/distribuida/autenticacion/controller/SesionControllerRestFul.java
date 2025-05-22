package com.distribuida.autenticacion.controller;

import com.distribuida.autenticacion.service.ISesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/sesion")
public class SesionControllerRestFul {


    @Autowired
    private ISesionService sesionService;


    @PostMapping(value = "/generar-token")
    public ResponseEntity<String> crearToken(@RequestParam(value = "username") String username,
                                             @RequestParam(value = "roles")List<String> roles){
        String token = this.sesionService.generarToken(username, roles);
        return ResponseEntity.ok(token);
    }

}
