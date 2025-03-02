package com.distribuida.alumno.controller;


import com.distribuida.alumno.service.IPropuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/propuesta")
public class PropuestaRestFullController {

    @Autowired
    private IPropuestaService propuestaService;


    @PutMapping("/{idPropuesta}/validar")
    public ResponseEntity<Boolean> validarPropuesta(@PathVariable Integer idPropuesta, @RequestParam("respuesta") Boolean respuesta ) {

        Boolean exito = this.propuestaService.validarPropuesta(idPropuesta, respuesta);

        if (Boolean.TRUE.equals(exito)) {
            return ResponseEntity.ok(Boolean.TRUE); // Retorna un HTTP 200 con true si fue exitoso
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Boolean.FALSE); // Retorna un HTTP 400 con false si falló
        }
    }

}
