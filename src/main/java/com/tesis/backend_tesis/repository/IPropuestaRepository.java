package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Propuesta;

import java.util.List;

public interface IPropuestaRepository {

    public Propuesta crear(Propuesta propuesta);
    public Propuesta buscarPorId(Integer id);
    public List<Propuesta> findByEstudianteId(Integer idEstudiante);

    public List<Propuesta> finall();


    public List<Propuesta> buscarPorApellidoEstudiante(String apellido);
    public List<Propuesta> buscarPorPeriodo(String periodo);
    public List<Propuesta> buscarPorEstado(String estado);
    public Propuesta actualizarEstado(Propuesta propuesta);
    /*
    public Propuesta eliminar(Long id);
    public Propuesta actualizar(Propuesta propuesta);
    public List<Propuesta> buscarTodos();

     */
}
