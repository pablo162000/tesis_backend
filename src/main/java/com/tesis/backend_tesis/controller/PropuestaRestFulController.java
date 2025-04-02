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
}
