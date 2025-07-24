package com.tesis.backend_tesis.repository;


import com.tesis.backend_tesis.repository.modelo.Usuario;

import java.util.List;

public interface IUsuarioRepository {

    public Usuario insert(Usuario usuarios);
    public Boolean existeUsuarioConEmail(String email) ;
    public Boolean existeUsuarioConCedula(String cedula) ;
    public Usuario update(Usuario usuario);
    public Usuario buscarPorEmail(String email);
    public Usuario findById(Integer id);
    public Boolean activarUsuario(Integer id);
    public Usuario actualizar(Usuario usuario);

    public List<Usuario> findAllWithRol(String rol);

    public List<Usuario> findEstudianteByEstado(Boolean activo, String rol);

    public Boolean activarDesactivarUsuario(Integer id, Boolean accion);

    public Boolean deleteUsuario(Integer id);

    public List<Usuario> findUsuariosNoVerificadosAntesDe();


}
