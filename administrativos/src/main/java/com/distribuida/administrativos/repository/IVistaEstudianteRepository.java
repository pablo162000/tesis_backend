package com.distribuida.administrativos.repository;

import com.distribuida.administrativos.repository.modelo.VistaEstudiante;

import java.util.List;

public interface IVistaEstudianteRepository {

    public VistaEstudiante findById(Integer id);
    public List<VistaEstudiante> findAll();
    public List<VistaEstudiante> findByEstado(Boolean activo);


}
