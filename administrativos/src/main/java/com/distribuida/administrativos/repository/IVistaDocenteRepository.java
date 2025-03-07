package com.distribuida.administrativos.repository;

import com.distribuida.administrativos.repository.modelo.VistaDocente;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public interface IVistaDocenteRepository {

    public VistaDocente findById(Integer id);
    public List<VistaDocente> findAll();
    public List<VistaDocente> findByEstado(Boolean activo);


}
