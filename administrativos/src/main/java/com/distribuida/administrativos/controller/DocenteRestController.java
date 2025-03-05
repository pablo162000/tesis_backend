package com.distribuida.administrativos.controller;


import com.distribuida.administrativos.service.IDocenteService;
import com.distribuida.administrativos.service.dto.DocenteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/docentes")
public class DocenteRestController {

    private static final Logger logger = LoggerFactory.getLogger(DocenteRestController.class);

    @Autowired
    private IDocenteService docenteService;

    @GetMapping("/{idDocente}")
    public ResponseEntity<DocenteDTO> obtenerDocente(@PathVariable Integer idDocente) {

        DocenteDTO docente = this.docenteService.buscarPorId(idDocente);

        if (docente == null) {
            logger.warn("Docente con ID {} no encontrado", idDocente);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(docente);
    }

    @GetMapping("/existe")
    public ResponseEntity<Boolean> existencia(@RequestParam String cedula) {

        // Verifica si el docente con la cédula proporcionada existe
        Boolean exito = this.docenteService.existeEstudiante(cedula);

        // Si el docente existe, retorna true con un estado 200 OK
        if (Boolean.TRUE.equals(exito)) {
            return ResponseEntity.ok(true);
        }

        // Si el docente no existe, retorna false con un estado 404 Not Found
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
    }

}
