package com.distribuida.login.controller;

import com.distribuida.login.repository.modelo.AuthResponse;
import com.distribuida.login.repository.modelo.LoginRequest;
import com.distribuida.login.repository.modelo.RegistroRequest;
import com.distribuida.login.service.IAuthService;
import com.distribuida.login.service.dto.DocenteDTO;
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
    public ResponseEntity<Boolean> registroUsuario(@RequestBody RegistroRequest registroRequest) {
        Boolean exito = this.authService.registroEstudiante(registroRequest); // Aquí obtenemos el valor de éxito


        // Verificamos si el valor es true
        if (Boolean.TRUE.equals(exito)) {
            return ResponseEntity.ok(Boolean.TRUE); // Retorna un HTTP 200 con true si fue exitoso
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Boolean.FALSE); // Retorna un HTTP 400 con false si falló
        }
    }

    @PostMapping("/registro/docente")
    public ResponseEntity<?> registroUsuarioDocente(@RequestBody RegistroRequest registroRequest) {
        DocenteDTO docenteDTO = this.authService.registroDocente(registroRequest); // Aquí obtenemos el valor de éxito

        // Verificamos si el valor es true
        if (docenteDTO!=null) {
            return ResponseEntity.ok(docenteDTO); // Retorna un HTTP 200 con true si fue exitoso
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST
            ).body("Error en el registro del docente. Verifique los datos ingresados."); // Retorna un HTTP 400 con false si falló
        }
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse response = this.authService.loginUsuario(loginRequest);
        return ResponseEntity.ok(response);
    }


}
