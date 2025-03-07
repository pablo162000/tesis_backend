package com.distribuida.login.controller;

import com.distribuida.login.clients.AdministrativoRestClient;
import com.distribuida.login.clients.EstudianteRestClient;
import com.distribuida.login.repository.modelo.AuthResponse;
import com.distribuida.login.repository.modelo.LoginRequest;
import com.distribuida.login.repository.modelo.RegistroRequest;
import com.distribuida.login.service.IAuthService;
import com.distribuida.login.service.dto.DocenteDTO;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@CrossOrigin
@RequestMapping(path = "/auth")
public class AuthRestFullController {

    @Autowired
    private IAuthService authService;

    @Autowired
    private EstudianteRestClient estudianteRestClient;

    @Autowired
    private AdministrativoRestClient administrativoRestClient;



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

        // Comprobamos si ya existe un estudiante con la misma cédula
        Boolean existeEstudiante = this.estudianteRestClient.existencia(registroRequest.getCedula());

        if (Boolean.TRUE.equals(existeEstudiante)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La cédula ya está registrada en estudiante.");
        }

        // Comprobamos si ya existe un docente con la misma cédula
        Boolean existeDocente = this.administrativoRestClient.existencia(registroRequest.getCedula());

        if (Boolean.TRUE.equals(existeDocente)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La cédula ya está registrada en docente.");
        }

        DocenteDTO docenteDTO = this.authService.registroDocente(registroRequest); // Registro del docente


        if (docenteDTO != null) {
            return ResponseEntity.ok(docenteDTO); // Retorna HTTP 200 si el registro es exitoso
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Error en el registro del docente. Verifique los datos ingresados.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse response = this.authService.loginUsuario(loginRequest);
        return ResponseEntity.ok(response);
    }


}
