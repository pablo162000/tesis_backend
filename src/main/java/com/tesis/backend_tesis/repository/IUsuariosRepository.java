package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Estudiantes;
import com.tesis.backend_tesis.repository.modelo.Usuarios;

public interface IUsuariosRepository {

    public Usuarios insertar(Usuarios usuarios);
    public boolean existeUsuarioConEmail(String email) ;
    public Usuarios buscarPorEmail(String email);
}
