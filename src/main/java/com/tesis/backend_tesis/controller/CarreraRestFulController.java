package com.tesis.backend_tesis.controller;


import com.tesis.backend_tesis.repository.modelo.CarreraRequest;
import com.tesis.backend_tesis.repository.modelo.RegistroRequest;
import com.tesis.backend_tesis.service.ICarreraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/carreras")
public class CarreraRestFulController {


    @Autowired
    private ICarreraService carreraService;

    @PreAuthorize("hasAnyRole('direccion', 'secretaria', 'ADMIN')")
    @PostMapping("/registro")
    public ResponseEntity<Boolean> registroCarrera(@RequestBody CarreraRequest carreraRequest) {
        Boolean registro = this.carreraService.insertar(carreraRequest);
        return ResponseEntity.ok(registro);

    }

    @PreAuthorize("hasAnyRole('direccion', 'secretaria', 'ADMIN')")
    @PutMapping("/registrousuario")
    public ResponseEntity<Boolean> registroUsarioCarrera(@RequestBody RegistroRequest RegistroRequest) {
        Boolean registro = this.carreraService.insertarUsuarioCarrera(RegistroRequest);
        return ResponseEntity.ok(registro);
    }


    @PreAuthorize("hasAnyRole('direccion', 'secretaria', 'ADMIN')")
    @PutMapping("/{idCarrera}/autoridades/{idUsuario}")
    public ResponseEntity<Boolean> registroAutoridades(@PathVariable Integer idCarrera,
                                                       @PathVariable Integer idUsuario) {
        Boolean registro = this.carreraService.insertarAutoridadesCarrera(idCarrera, idUsuario);
        return ResponseEntity.ok(registro);
    }
}
