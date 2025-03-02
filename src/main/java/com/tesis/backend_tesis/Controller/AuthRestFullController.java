package com.tesis.backend_tesis.Controller;

import com.tesis.backend_tesis.repository.modelo.AuthResponse;
import com.tesis.backend_tesis.repository.modelo.LoginRequest;
import com.tesis.backend_tesis.repository.modelo.RegistroRequest;
import com.tesis.backend_tesis.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/auth")
public class AuthRestFullController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<Integer> registroUsuario(@RequestBody RegistroRequest registroRequest) {
        Integer id = authService.registroEstudiante(registroRequest);
        if (id != 0) {
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(id);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.loginUsuario(loginRequest);
        return ResponseEntity.ok(response);
    }



}
