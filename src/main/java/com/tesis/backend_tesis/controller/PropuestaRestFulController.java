package com.tesis.backend_tesis.controller;

import com.tesis.backend_tesis.service.IPropuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(path = "/propuestas")
public class PropuestaRestFulController {

    @Autowired
    private IPropuestaService propuestaService;

    @PostMapping("/guardar")
    public ResponseEntity<String> guardarPropuesta(
            @RequestParam String tipo,
            @RequestParam String tema,
            @RequestParam String categoria,
            @RequestParam String primerCorreo,
            @RequestParam(required = false) String segundoCorreo,
            @RequestParam(required = false) String tercerCorreo,
            @RequestParam(required = false) Integer idDocenteTutor,
            @RequestParam MultipartFile archivo) throws IOException {

            String respuesta = propuestaService.guardar(
                    tipo, tema, categoria, primerCorreo, segundoCorreo, tercerCorreo, idDocenteTutor, archivo);

            return ResponseEntity.ok(respuesta);

    }

    @PutMapping("/validar")
    public ResponseEntity<Boolean> validarPropuesta(@RequestParam("idPropuesta") Integer idPropuesta,
                                                    @RequestParam("respuesta") Boolean respuesta,
                                                    @RequestParam(value="observaciones", required = false) String observaciones,
                                                    @RequestParam("taskID") String taskID
                                                    ) {


        Boolean exito = this.propuestaService.validarPropuesta(idPropuesta,
                respuesta,
                observaciones, taskID);

            return ResponseEntity.ok(exito); // Retorna un HTTP 200 con true si fue exitoso

    }

    @PutMapping(value = "/asignarrevisores")
    public ResponseEntity<Boolean> asignarRevisores(@RequestParam("idPropuesta") Integer idPropuesta,
                                                   @RequestParam("idDocente") Integer idDocente,
                                                   @RequestParam("tipoRevisor") String tipoRevisor,
                                                    @RequestParam("taskId") String taskId) {

        Boolean exito = this.propuestaService.asignarRevisor(idPropuesta, idDocente, tipoRevisor, taskId);

        return ResponseEntity.ok(exito);
    }

}
