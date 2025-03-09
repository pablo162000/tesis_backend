package com.distribuida.administrativos.service;

import com.distribuida.administrativos.repository.modelo.VistaDocente;
import com.distribuida.administrativos.repository.modelo.VistaEstudiante;

import java.util.List;

public interface IEstudianteService {

    public VistaEstudiante buscarViewDocentePorId(Integer id);
    public List<VistaEstudiante> buscarTodosViewDocente();
    public List<VistaEstudiante> buscarViewDocentePorEstado(Boolean activo);

}
