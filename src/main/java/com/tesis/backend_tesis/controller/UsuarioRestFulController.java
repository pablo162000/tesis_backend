package com.tesis.backend_tesis.controller;


import com.tesis.backend_tesis.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping(path = "/usuarios")
public class UsuarioRestFulController {

    @Autowired
    private IUsuarioService usuarioService;


    @PutMapping("/validar")
    public ResponseEntity<Boolean> validarCorreo(@RequestParam(value = "token") String token,
                                                 @RequestParam(value = "password") String password) {
        Boolean resultado = this.usuarioService.activarCuenta(token, password);
        return ResponseEntity.ok(resultado);
    }


    @PostMapping("/recuperar")
    public ResponseEntity<Boolean> recuperarCuenta(@RequestParam(value = "correo") String correo) {
        Boolean resultado = this.usuarioService.recuperarCuenta(correo);
        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/recuperarcontrasena")
    ResponseEntity<Boolean> recuperarContrasena(@RequestParam(value = "password") String password,
                                               @RequestParam(value = "token")String token){
        Boolean resultado = this.usuarioService.recuperarContrasena(password, token);
        return ResponseEntity.ok(resultado);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/actualizar")
    public ResponseEntity<Boolean> actulizarContrasena(@RequestParam(value = "correo") String correo,
                                                       @RequestParam(value = "oldPassword") String oldFpassword,
                                                       @RequestParam(value = "newPassword") String newPassword) {
        Boolean resultado = this.usuarioService.actulizarContrasena(correo, oldFpassword, newPassword);
        return ResponseEntity.ok(resultado);
    }


    @PreAuthorize("hasAnyRole('direccion', 'secretaria', 'ADMIN')")
    @PutMapping("/activardesactivar/{idUsuario}")
    public ResponseEntity<Boolean>  activarDesactivarUsuario(@PathVariable("idUsuario") Integer idUsuario,
                                                             @RequestParam("accion") Boolean accion) {
        Boolean exito = this.usuarioService.activarDesactivarCuenta(idUsuario,accion );
        return ResponseEntity.ok(exito);
    }

}
