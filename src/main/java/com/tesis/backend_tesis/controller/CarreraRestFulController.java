package com.tesis.backend_tesis.controller;


import com.tesis.backend_tesis.repository.modelo.CarreraRequest;
import com.tesis.backend_tesis.repository.modelo.RegistroRequest;
import com.tesis.backend_tesis.service.ICarreraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/carreras")
public class CarreraRestFulController {


    @Autowired
    private ICarreraService carreraService;


    @PostMapping("/registro")
    public ResponseEntity<Boolean> registroUsuario(@RequestBody CarreraRequest carreraRequest) {
        Boolean registro = this.carreraService.insertar(carreraRequest);
        return ResponseEntity.ok(registro);

    }


    @PutMapping("/registrousuario")
    public ResponseEntity<Boolean> registroUsarioCarrera(@RequestBody RegistroRequest RegistroRequest) {
        Boolean registro = this.carreraService.insertarUsuarioCarrera(RegistroRequest);
        return ResponseEntity.ok(registro);

    }

    /*
    @PutMapping("/vincularautoridades")
    public ResponseEntity<Boolean> registroAutoridades(@RequestParam Integer idCarrera,
                                                       @RequestParam Integer idUsuario,
                                                       @RequestParam String tipo) {
        Boolean registro = this.carreraService.insertarAutoridadesCarrera(idCarrera, idUsuario, tipo);
        return ResponseEntity.ok(registro);
    }
     */

    @PutMapping("/{idCarrera}/autoridades/{idUsuario}")
    public ResponseEntity<Boolean> registroAutoridades(@PathVariable Integer idCarrera,
                                                       @PathVariable Integer idUsuario,
                                                       @RequestParam String tipo) {
        Boolean registro = this.carreraService.insertarAutoridadesCarrera(idCarrera, idUsuario, tipo);
        return ResponseEntity.ok(registro);
    }
}
