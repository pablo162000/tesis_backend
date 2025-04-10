package com.tesis.backend_tesis.controller;


import com.tesis.backend_tesis.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/usuarios")
public class UsuarioRestFulController {

    @Autowired
    private IUsuarioService usuarioService;


    @PutMapping("/activar")
    public ResponseEntity<Boolean> validarCorreo(@RequestParam(value = "token") String token,
                                                @RequestParam(value = "password") String password) {
        Boolean resultado = this.usuarioService.activarCuenta(token, password);
/*
        if (resultado) {
            return ResponseEntity.ok("Correo validado exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al activar la cuenta.");
        }

 */

        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/recuperar")
    public ResponseEntity<Boolean> recuperarCuenta(@RequestParam(value = "correo") String correo) {
        Boolean resultado = this.usuarioService.recuperarCuenta(correo);

        /*
        if (resultado) {
            return ResponseEntity.ok("Inicio de recuperaciond e ceunta exitoso.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al tartar de recuperar la cuenta.");
        }

         */
        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/recuperarcontrasena")
    ResponseEntity<Boolean> recuperarContrasena(@RequestParam(value = "password") String password,
                                               @RequestParam(value = "token")String token){

        Boolean resultado = this.usuarioService.recuperarContrasena(password, token);

        /*
        if (resultado) {
            return ResponseEntity.ok("Cuenta recuperada exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al recuperar la cuenta.");
        }

         */

        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/actualizar")
    public ResponseEntity<Boolean> actulizarContrasena(@RequestParam(value = "correo") String correo,
                                                       @RequestParam(value = "password") String password) {
        Boolean resultado = this.usuarioService.actulizarContrasena(correo, password);

        /*
        if (resultado) {
            return ResponseEntity.ok("Inicio de recuperaciond e ceunta exitoso.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al tartar de recuperar la cuenta.");
        }

         */
        return ResponseEntity.ok(resultado);
    }

}
