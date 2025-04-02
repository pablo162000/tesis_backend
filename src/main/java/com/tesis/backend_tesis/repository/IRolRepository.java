package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Facultad;
import com.tesis.backend_tesis.repository.modelo.Rol;

import java.util.List;

public interface IRolRepository {

    public Rol findById(Integer id);
    public Rol findByNombre(String nombre);
    public List<Rol> findAll();

}
