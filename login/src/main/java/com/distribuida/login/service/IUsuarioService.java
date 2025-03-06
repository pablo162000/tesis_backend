package com.distribuida.login.service;


import com.distribuida.login.repository.modelo.Usuario;
import com.distribuida.login.service.dto.UsuarioDTO;

import java.util.List;

public interface IUsuarioService {

    public Boolean activarCuenta(Integer id) ;
    public UsuarioDTO buscarPorEmail(String email);
    public List<UsuarioDTO> buscarTodosUsuaiosEstudiante();
    public Boolean actualizarContrasena(String correo, String contrasena);

    public List<UsuarioDTO> buscarEstudiantePorEstado(Boolean activo);

    public Boolean activarDesactivarCuenta(Integer id,Boolean accion);

}
