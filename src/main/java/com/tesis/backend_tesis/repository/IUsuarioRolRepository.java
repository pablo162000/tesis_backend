package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.UsuarioRol;

import java.util.List;

public interface IUsuarioRolRepository {

    public UsuarioRol insert(UsuarioRol usuarioRol);
    public UsuarioRol findById(Integer id);
    public List<UsuarioRol> findByIdUsuario(Integer idUsuario);
    public Boolean deleteUsuarioRolbyIdUsuario(Integer idUsuario);

    public Boolean deleteUsuarioRolbyIdUsuarioAndRol(Integer idUsuario, String rol);
    //public UsuarioRol findByNombre(String nombre);
    public List<UsuarioRol> findAll();

}
