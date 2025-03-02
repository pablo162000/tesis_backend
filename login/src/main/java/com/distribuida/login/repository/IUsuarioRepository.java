package com.distribuida.login.repository;

import com.distribuida.login.repository.modelo.Usuario;

import java.util.List;

public interface IUsuarioRepository {

    public Usuario insertar(Usuario usuarios);
    public boolean existeUsuarioConEmail(String email) ;
    public Usuario buscarPorEmail(String email);
    public Usuario buscarPorId(int id);
    public Boolean activarUsuario(Integer id);

    public List<Usuario> findAllWithRol(String rol);

}
