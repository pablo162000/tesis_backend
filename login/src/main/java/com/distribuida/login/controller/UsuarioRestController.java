package com.distribuida.login.controller;

import com.distribuida.login.repository.modelo.Usuario;
import com.distribuida.login.service.IUsuarioService;
import com.distribuida.login.service.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;


@RestController
@CrossOrigin
@RequestMapping(path = "/usuarios")
public class UsuarioRestController {

    @Autowired
    private IUsuarioService usuarioService;



    @PutMapping("/activar/{id}")
    public ResponseEntity<Boolean>  activarUsuario(@PathVariable Integer id) {
        Boolean exito = this.usuarioService.activarCuenta(id);

        if (Boolean.TRUE.equals(exito)) {
            return ResponseEntity.ok(true); // Retorna HTTP 200 con true si fue exitoso
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo activar la cuenta. Verifique que el ID del usuario sea correcto.");
        }
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<UsuarioDTO>  existeUsuarioCorreo(@PathVariable String correo) {

        // Llamar al servicio que verifica si el usuario existe por su ID
        UsuarioDTO usuarioDTO = this.usuarioService.buscarPorEmail(correo);

        if (usuarioDTO != null) {

            return ResponseEntity.ok(usuarioDTO);
        }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

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


    @GetMapping("/estudiantes/estado/activacion")
    public ResponseEntity<List<UsuarioDTO>> obtenerEstudiantesPorEstado(@RequestParam("estado") Boolean estado) {
        List<UsuarioDTO> estudiantes = usuarioService.buscarEstudiantePorEstado(estado);

        if (estudiantes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(estudiantes);
    }

    @PutMapping("/activardesactivar")
    public ResponseEntity<Boolean>  activarDesactivarUsuario(@RequestParam("id") Integer id,
                                                             @RequestParam("accion") Boolean accion) {

        Boolean exito = this.usuarioService.activarDesactivarCuenta(id,accion );

        if (Boolean.TRUE.equals(exito)) {
            return ResponseEntity.ok(true); // Retorna HTTP 200 con true si fue exitoso
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo activar la cuenta. Verifique que el ID del usuario sea correcto.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerEstudiantePorId(@PathVariable Integer id) {

        UsuarioDTO usuarioDTO = this.usuarioService.buscarPorId(id);

        if (usuarioDTO != null) {
            return ResponseEntity.ok(usuarioDTO);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

}
