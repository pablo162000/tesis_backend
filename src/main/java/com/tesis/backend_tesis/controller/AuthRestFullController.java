package com.tesis.backend_tesis.controller;

import com.tesis.backend_tesis.repository.modelo.AuthResponse;
import com.tesis.backend_tesis.repository.modelo.LoginRequest;
import com.tesis.backend_tesis.repository.modelo.RegistroRequest;
import com.tesis.backend_tesis.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(path = "/auth")
public class AuthRestFullController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<Boolean> registroUsuario(@RequestBody RegistroRequest registroRequest) {
        Boolean registro = this.authService.registroNuevoUsuario(registroRequest);
        return ResponseEntity.ok(registro);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse response = this.authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    /*
    @PostMapping("/seleccionar-rol")
    public ResponseEntity<AuthResponse> seleccionarRol(@RequestParam Integer idUsuario, @RequestParam String rol) {
        AuthResponse authResponse = this.authService.seleccionarRol(idUsuario, rol);
        return ResponseEntity.ok(authResponse);
    }
     */
    /*
    @PutMapping("/usuarios/{idUsuario}/rol")
    public ResponseEntity<AuthResponse> seleccionarRol(@PathVariable Integer idUsuario,
                                                       @RequestBody Map<String, String> body) {
        AuthResponse authResponse = this.authService.seleccionarRol(idUsuario, body.get("rol"));
        return ResponseEntity.ok(authResponse);
    }

     */

    //{
    //  "rol": "Director"
    //}



}
