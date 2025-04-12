package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.service.dto.UsuarioDTO;

public interface IUsuarioService {


    public UsuarioDTO insertar(UsuarioDTO usuarioDTO);

    public Boolean activarCuenta (String token, String password);

    public Boolean actulizarContrasena (String correo, String password);

    public Boolean recuperarCuenta (String correo);

    public Boolean recuperarContrasena (String password, String token);

    public UsuarioDTO buscarPorId(Integer idUsuario);

    public Boolean activarDesactivarCuenta(Integer id, Boolean accion);

    public void eliminarUsuariosNoVerificadosCarrera();

    public void eliminarUsuariosNoVerificados();
}
