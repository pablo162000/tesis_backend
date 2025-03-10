package com.distribuida.login.repository;

import com.distribuida.login.repository.modelo.Usuario;

import java.time.LocalDateTime;
import java.util.List;

public interface IUsuarioRepository {

    public Usuario insertar(Usuario usuarios);
    public boolean existeUsuarioConEmail(String email) ;

    public Usuario buscarPorEmail(String email);
    public Usuario buscarPorId(Integer id);
    public Boolean activarUsuario(Integer id);
    public Usuario actualizar(Usuario usuario);
    public List<Usuario> findAllWithRol(String rol);

    public List<Usuario> findEstudianteByEstado(Boolean activo, String rol);

    public Boolean activarDesactivarUsuario(Integer id, Boolean accion);

    public Boolean deleteUsuario(Integer id);

    public List<Usuario> findUsuariosNoVerificadosAntesDe();
}
