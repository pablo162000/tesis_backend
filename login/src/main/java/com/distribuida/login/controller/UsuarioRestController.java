package com.distribuida.login.controller;

import com.distribuida.login.service.IUsuarioService;
import com.distribuida.login.service.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
@CrossOrigin
@RequestMapping(path = "/usuarios")
public class UsuarioRestController {

    @Autowired
    private IUsuarioService usuarioService;



    @PutMapping("/{id}/activar")
    public ResponseEntity<Boolean>  activarUsuario(@PathVariable Integer id) {

        Boolean exito = this.usuarioService.activarCuenta(id);

        if (Boolean.TRUE.equals(exito)) {
            return ResponseEntity.ok(Boolean.TRUE); // Retorna un HTTP 200 con true si fue exitoso
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Boolean.FALSE); // Retorna un HTTP 400 con false si falló
        }
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<UsuarioDTO>  existeUsuarioCorreo(@PathVariable String correo) {

        // Llamar al servicio que verifica si el usuario existe por su ID
        UsuarioDTO usuarioDTO = this.usuarioService.buscarPorEmail(correo);

        System.out.println(correo);

        if (usuarioDTO != null) {
            // Convertir la entidad Usuario a UsuarioDTO usando el mapper
            return ResponseEntity.ok(usuarioDTO);  // Devolver el DTO con un código HTTP 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Devolver 404 si no se encuentra
        }

    }

    @GetMapping("/estudiantes")
    public ResponseEntity<List<UsuarioDTO>>  buscarTodosUsuariosEstudiantes() {

        // Llamar al servicio que verifica si el usuario existe por su ID
        List<UsuarioDTO> usuariosDTO = this.usuarioService.buscarTodosUsuaiosEstudiante();



        if (!usuariosDTO.isEmpty()) {
            // Convertir la entidad Usuario a UsuarioDTO usando el mapper
            return ResponseEntity.ok(usuariosDTO);  // Devolver el DTO con un código HTTP 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Devolver 404 si no se encuentra
        }

    }


    @PutMapping("/activardocente")
    public ResponseEntity<String>  primerIngresoDocente(@RequestParam("correo") String correo,
                                                         @RequestParam("password") String password) {

// Validaciones de entrada
        if (correo == null || correo.isBlank()) {
            return ResponseEntity.badRequest().body("El correo del docente no puede estar vacío.");
        }

        if (password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body("La nueva contraseña no puede estar vacía.");
        }

        // Buscar usuario por correo
        UsuarioDTO usuarioDTO = this.usuarioService.buscarPorEmail(correo);

        if (usuarioDTO == null) {
            return ResponseEntity.badRequest().body("No se encontró un docente con ese correo.");
        }

        // Verificar si es su primer ingreso (usuario inactivo)
        if (Boolean.TRUE.equals(usuarioDTO.getActivo())) {
            return ResponseEntity.badRequest().body("El docente ya ha activado su cuenta.");
        }

        // Actualizar contraseña y activar docente
        this.usuarioService.actualizarContrasena(correo, password);

        return ResponseEntity.ok("Contraseña actualizada. Ahora puede ingresar al sistema.");
    }

}
