package com.distribuida.alumno.service;

import com.distribuida.alumno.repository.modelo.Propuesta;
import com.distribuida.alumno.service.dto.PropuestaDTO;

import java.util.List;

public interface IPropuestaService {

    public Propuesta guardar(Propuesta propuesta);

    public Propuesta buscar(Integer id);

    public List<PropuestaDTO> buscarPorIdEstudiante(Integer idEstudiante);

    public Boolean validarPropuesta(Integer idPropuesta, Boolean estadoValidacion);
}
