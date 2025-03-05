package com.distribuida.administrativos.repository;

import com.distribuida.administrativos.repository.modelo.Docente;

public interface IDocenteRepository {

    public Docente insert(Docente doocente);

    public Docente findById(Integer id);

    public Docente findByCedula(String cedula);
}
