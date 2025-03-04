package com.distribuida.administrativos.controller;


import com.distribuida.administrativos.repository.modelo.RegistroRequest;
import com.distribuida.administrativos.service.IDocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/secretaria")
public class SecretariaRestController {

    @Autowired
    private IDocenteService docenteService;


    @PostMapping("/docente")
    public ResponseEntity<Boolean> registroUsuarioDocente(@RequestBody RegistroRequest registroRequest){

        Boolean resultado = this.docenteService.guardarDocente(registroRequest);

        if (Boolean.TRUE.equals(resultado)) {
            return ResponseEntity.ok(Boolean.TRUE);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Boolean.FALSE);
        }
    }



}
