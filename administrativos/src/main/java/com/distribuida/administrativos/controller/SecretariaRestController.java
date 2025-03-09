package com.distribuida.administrativos.controller;
import com.distribuida.administrativos.repository.modelo.RegistroRequest;

import com.distribuida.administrativos.repository.modelo.VistaDocente;
import com.distribuida.administrativos.repository.modelo.VistaEstudiante;
import com.distribuida.administrativos.service.IDocenteService;
import com.distribuida.administrativos.service.IEstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/secretaria")
public class SecretariaRestController {

    @Autowired
    private IEstudianteService estudianteService;

    @Autowired
    private IDocenteService docenteService;


    // Buscar un docente por ID
    @GetMapping("/vistadocente/{id}")
    public ResponseEntity<VistaDocente> buscarViewDocentePorId(@PathVariable Integer id) {
        VistaDocente docente = this.docenteService.buscarViewDocentePorId(id);
        return ResponseEntity.ok(docente);
    }

    // Obtener todos los docentes
    @GetMapping("/vistadocente")
    public ResponseEntity<List<VistaDocente>> obtenerTodosViewDocente() {
        List<VistaDocente> docentes = this.docenteService.buscarTodosViewDocente();
        return docentes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(docentes);

    }

    // Buscar docentes por estado (activo/inactivo)
    @GetMapping("/vistadocente/estado/{activo}")
    public ResponseEntity<List<VistaDocente>> buscarViewDocentgePorEstado(@PathVariable Boolean activo) {
        List<VistaDocente> docentes = this.docenteService.buscarViewDocentePorEstado(activo);
        return ResponseEntity.ok(docentes);
    }

    // Buscar un docente por ID
    @GetMapping("/vistaestudiante/{id}")
    public ResponseEntity<VistaEstudiante> buscarViewEstudiantePorId(@PathVariable Integer id) {
        VistaEstudiante estudiante = this.estudianteService.buscarViewDocentePorId(id);
        return ResponseEntity.ok(estudiante);
    }

    // Obtener todos los docentes
    @GetMapping("/vistaestudiante")
    public ResponseEntity<List<VistaEstudiante>> obtenerTodosViewEstudiante() {
        List<VistaEstudiante> estudiantes = this.estudianteService.buscarTodosViewDocente();
        return estudiantes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(estudiantes);

    }

    // Buscar docentes por estado (activo/inactivo)
    @GetMapping("/vistaestudiante/estado/{activo}")
    public ResponseEntity<List<VistaEstudiante>> buscarViewEstudiantePorEstado(@PathVariable Boolean activo) {
        List<VistaEstudiante> estudiantes = this.estudianteService.buscarViewDocentePorEstado(activo);
        return ResponseEntity.ok(estudiantes);
    }


    @PostMapping("/docente")
    public ResponseEntity<Boolean> registroUsuarioDocente(@RequestBody RegistroRequest registroRequest) {
        Boolean resultado = this.docenteService.guardarDocente(registroRequest);

        if (Boolean.TRUE.equals(resultado)) {
            return ResponseEntity.ok(Boolean.TRUE);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Boolean.FALSE);
        }
    }

}
