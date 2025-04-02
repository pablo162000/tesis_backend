package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Facultad;


import java.util.List;

public interface IFacultadRepository {

    public Facultad findById(Integer id);
    public Facultad findByNombre(String nombre);
    public List<Facultad> findAll();
}
