package com.distribuida.alumno.repository;

import com.distribuida.alumno.repository.modelo.EstadoValidacion;
import com.distribuida.alumno.repository.modelo.VistaPropuesta;

import java.util.List;

public interface IVistaPropuestaRepository {

    public VistaPropuesta findById(Integer id);
    public List<VistaPropuesta> findAll();
    public List<VistaPropuesta> findByEstadoValidacion(EstadoValidacion estadoValidacion);
    public List<VistaPropuesta> findByEstadoAprobacion(Boolean activo);




}
