package com.distribuida.login.repository;

import com.distribuida.login.repository.modelo.Carrera;

public interface ICarreraRepository {


    public Carrera insertar(Carrera carrera);
    public Carrera buscarPorId(int id);

}
