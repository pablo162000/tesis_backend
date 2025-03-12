package com.distribuida.administrativos.controller;


import com.distribuida.administrativos.repository.modelo.VistaDocente;
import com.distribuida.administrativos.service.IDocenteService;
import com.distribuida.administrativos.service.dto.DocenteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/docentes")
public class DocenteRestController {

    private static final Logger logger = LoggerFactory.getLogger(DocenteRestController.class);

    @Autowired
    private IDocenteService docenteService;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<DocenteDTO> obtenerDocentePorIdUsuario(@PathVariable Integer idUsuario) {

        DocenteDTO docente = this.docenteService.buscarPorIdUsuario(idUsuario);

        if (docente == null) {
            logger.warn("Docente con ID {} no encontrado", idUsuario);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(docente);
    }

    @GetMapping("/existe")
    public ResponseEntity<Boolean> existencia(@RequestParam String cedula) {

        boolean existe = docenteService.existeDocente(cedula);

        return ResponseEntity.ok(existe);
    }

}