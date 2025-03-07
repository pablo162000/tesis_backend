package com.distribuida.login.repository;

import com.distribuida.login.repository.modelo.Carrera;

import java.util.List;

public interface ICarreraRepository {


    public Carrera insertar(Carrera carrera);
    public Carrera findById(Integer id);
    public List<Carrera> findAll();


}
