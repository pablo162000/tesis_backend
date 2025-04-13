package com.tesis.backend_tesis.controller;

import com.tesis.backend_tesis.service.IPropuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(path = "/propuestas")
public class PropuestaRestFulController {

    @Autowired
    private IPropuestaService propuestaService;

    @PostMapping
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
                    tipo, tema.trim(), categoria, primerCorreo.trim(), segundoCorreo.trim(), tercerCorreo.trim(), idDocenteTutor, archivo);

            return ResponseEntity.ok(respuesta);

    }
/*
    @PutMapping("/validar")
    public ResponseEntity<Boolean> validarPropuesta(@RequestParam("idPropuesta") Integer idPropuesta,
                                                    @RequestParam("idDocenteDirector")  Integer idDocenteDirector,
                                                    @RequestParam("respuesta") Boolean respuesta,
                                                    @RequestParam(value="observaciones", required = false) String observaciones) {


        Boolean exito = this.propuestaService.validarPropuesta(idPropuesta,
                respuesta,
                observaciones);

            return ResponseEntity.ok(exito); // Retorna un HTTP 200 con true si fue exitoso

    }

 */

    @PutMapping("/{idPropuesta}/validar")
    public ResponseEntity<Boolean> validarPropuesta(@PathVariable Integer idPropuesta,
                                                    @RequestParam("respuesta") Boolean respuesta,
                                                    @RequestParam(value="observaciones", required = false) String observaciones,
                                                    @RequestParam("taskID") String taskID) {


        Boolean exito = this.propuestaService.validarPropuesta(idPropuesta,
                respuesta,
                observaciones, taskID);

        return ResponseEntity.ok(exito); // Retorna un HTTP 200 con true si fue exitoso

    }
/*
    @PutMapping(value = "/asignarrevisores")
    public ResponseEntity<Boolean> asignarRevisores(@RequestParam("idPropuesta") Integer idPropuesta,
                                                   @RequestParam("idDocente1") Integer idDocente1,
                                                   @RequestParam("idDocente2") Integer idDocente2,
                                                    @RequestPart("rubrica") MultipartFile rubrica,
                                                    @RequestPart("archivo") MultipartFile archivo ,
                                                    @RequestPart("oficio") MultipartFile oficio

    ) {

        Boolean exito = this.propuestaService.asignarRevisor(idPropuesta, idDocente1, idDocente2, rubrica, archivo, oficio);

        return ResponseEntity.ok(exito);
    }

 */


    @PutMapping(value = "/{idPropuesta}/asignarrevisores")
    public ResponseEntity<Boolean> asignarRevisores(@PathVariable Integer idPropuesta,
                                                    @RequestParam("idDocente1") Integer idDocente1,
                                                    @RequestParam("idDocente2") Integer idDocente2,
                                                    @RequestPart("rubrica") MultipartFile rubrica,
                                                    @RequestPart("archivo") MultipartFile archivo ,
                                                    @RequestPart("oficio") MultipartFile oficio, @RequestPart("taskID") String taskID

    ) {

        Boolean exito = this.propuestaService.asignarRevisor(idPropuesta, idDocente1, idDocente2, rubrica, archivo, oficio, taskID);

        return ResponseEntity.ok(exito);
    }

}
