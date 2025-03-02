package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Usuarios;

public interface IUsuariosRepository {

    public Usuarios insertar(Usuarios usuarios);
    public boolean existeUsuarioConEmail(String email) ;
    public Usuarios buscarPorEmail(String email);
    public Usuarios buscarPorId(int id);
    public boolean esCorreoValido(String correo);

    public Boolean activarUsuario(Usuarios usuarios);
}
