package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Carrera;

import java.util.List;

public interface ICarreraRepository {

    public Carrera insert(Carrera carrera);
    public Boolean existeCarreraPorNombre(String nombre);
    public Carrera findById(Integer id);
    public Carrera update(Carrera carrera);
    public void actualizar(Integer idUsuario);

    public Carrera findByIdDireccion(Integer idDireccion);
    public Carrera findByNombre(String nombre);
    public List<Carrera> findByFacultad(Integer idFacultad);
    public List<Carrera> findAll();
}
