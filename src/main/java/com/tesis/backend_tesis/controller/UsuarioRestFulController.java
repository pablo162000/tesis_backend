package com.tesis.backend_tesis.controller;


import com.tesis.backend_tesis.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/usuarios")
public class UsuarioRestFulController {

    @Autowired
    private IUsuarioService usuarioService;


    @PutMapping("/activar")
    public ResponseEntity<String> validarCorreo(@RequestParam(value = "token") String token,
                                                @RequestParam(value = "password") String password) {
        Boolean resultado = this.usuarioService.activarCuenta(token, password);

        if (resultado) {
            return ResponseEntity.ok("Correo validado exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al activar la cuenta.");
        }
    }

    @PostMapping("/recuperar")
    public ResponseEntity<String> recuperarCuenta(@RequestParam(value = "correo") String correo) {
        Boolean resultado = this.usuarioService.recuperarCuenta(correo);

        if (resultado) {
            return ResponseEntity.ok("Inicio de recuperaciond e ceunta exitoso.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al tartar de recuperar la cuenta.");
        }
    }

    @PutMapping("/recuperarcontrasena")
    ResponseEntity<String> recuperarContrasena(@RequestParam(value = "password") String password,
                                               @RequestParam(value = "token")String token){

        Boolean resultado = this.usuarioService.recuperarContrasena(password, token);

        if (resultado) {
            return ResponseEntity.ok("Cuenta recuperada exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al recuperar la cuenta.");
        }
    }

}
