package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Docente;

public interface IDocenteRepository {

    public Docente insert(Docente docente);

    public Docente findById(Integer id);

    public Docente findByIdUsuario(Integer idUsuario);

    public Boolean deleteDocenteByIdUsuario(Integer idUsuario);

    public Docente findByCedula(String cedula);

}
