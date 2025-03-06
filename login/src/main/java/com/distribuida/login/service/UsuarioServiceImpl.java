package com.distribuida.login.service;

import com.distribuida.login.repository.IUsuarioRepository;
import com.distribuida.login.repository.modelo.Usuario;
import com.distribuida.login.service.dto.UsuarioDTO;
import com.distribuida.login.service.dto.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IEncriptionService encriptionService;


    @Autowired
    private Converter converter;

    @Override
    public Boolean activarCuenta(Integer id) {
        return this.usuarioRepository.activarUsuario(id);
    }

    @Override
    public UsuarioDTO buscarPorEmail(String email) {

        return this.converter.toDTO(this.usuarioRepository.buscarPorEmail(email));
    }

    @Override
    public List<UsuarioDTO> buscarTodosUsuaiosEstudiante() {
        List<Usuario> usuarios = usuarioRepository.findAllWithRol("estudiante");

        // Convertir la lista de Usuario a una lista de UsuarioDTO
        return this.converter.toUsuarioDTOList(usuarios);
    }

    @Override
    public Boolean actualizarContrasena(String correo, String contrasena) {

        try {
            Usuario usuario = this.usuarioRepository.buscarPorEmail(correo);

            if (usuario == null) {
                return false; // Usuario no encontrado
            }

            usuario.setPassword(this.encriptionService.encriptPass(contrasena));
            usuario.setActivo(Boolean.TRUE);
            usuarioRepository.actualizar(usuario); // Guardar cambios

            return true; // Contraseña actualizada exitosamente
        } catch (Exception e) {
            e.printStackTrace(); // Log del error
            return false; // Error al actualizar la contraseña
        }

    }

    @Override
    public List<UsuarioDTO> buscarEstudiantePorEstado(Boolean activo) {
        // Asegurar que activo sea un valor booleano explícito
        if (Boolean.TRUE.equals(activo)) {
            activo = Boolean.TRUE;
        } else if (Boolean.FALSE.equals(activo)) {
            activo = Boolean.FALSE;
        }

        List<Usuario> usuarios = this.usuarioRepository.findEstudianteByEstado(activo, "estudiante");

        return this.converter.toUsuarioDTOList(usuarios);
    }

    @Override
    public Boolean activarDesactivarCuenta(Integer id, Boolean accion) {

        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del usuario no puede ser nulo.");
        }

        if (accion == null ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La acción de activación o desactivación no puede ser nula.");
        }

        Usuario usuario = this.usuarioRepository.buscarPorId(id);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.");
        }

        return this.usuarioRepository.activarDesactivarUsuario(id, accion);
    }

}
