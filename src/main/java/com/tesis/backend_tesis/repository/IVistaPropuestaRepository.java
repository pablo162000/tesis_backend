package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.EstadoAprobacion;
import com.tesis.backend_tesis.repository.modelo.EstadoValidacion;
import com.tesis.backend_tesis.repository.modelo.VistaPropuesta;

import java.util.List;

public interface IVistaPropuestaRepository {

    public VistaPropuesta findById(Integer idPropuesta);
    public List<VistaPropuesta> findByCarrera(String carrera);
    public List<VistaPropuesta> findByEstadoValidacion(EstadoValidacion estadoValidacion, String carrera);
    public List<VistaPropuesta> findByEstadoAprobacion(EstadoAprobacion estadoAprobacion, String carrera);
    public List<VistaPropuesta> findByPeriodo(String periodo, String carrera);
    public List<VistaPropuesta> findByTipo(String tipo, String carrera);
    public List<VistaPropuesta> findByCategoria(String categoria, String carrera);
    public List<VistaPropuesta> findByTipoCategoria(String tipo,String categoria, String carrera);
    public List<VistaPropuesta> findByTema(String tema, String carrera);
    public List<VistaPropuesta> findByTutor(Integer idUsuario, String facultad);
    public List<VistaPropuesta> findByRevisor (Integer idUsuario, String facultad);


    public List<VistaPropuesta> findAll();




}
