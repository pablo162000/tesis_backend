package com.distribuida.autenticacion.controller;


import com.distribuida.autenticacion.service.IValidarCorreoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/validacion")
public class ValidarCorreoControllerRestful {

    @Autowired
    private IValidarCorreoService validarCorreoService;


    @PostMapping(value = "/generar-token")
    public ResponseEntity<String> crearToken(@RequestParam(value = "correo") String correo){
        String token = validarCorreoService.generarTokenCorreo(correo);
        return ResponseEntity.ok(token);
    }


    @GetMapping(value = "/validar-token")
    public ResponseEntity<String> validarToken(@RequestParam(value = "token") String token){
        String correo = validarCorreoService.validarTokenCorreo(token);
        return ResponseEntity.ok(correo);
    }

}
