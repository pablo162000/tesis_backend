package com.tesis.backend_tesis.repository;


import com.tesis.backend_tesis.repository.modelo.VistaEstudiante;

import java.util.List;

public interface IVistaEstudianteRepository {

    public VistaEstudiante findByIdUsuario(Integer idUsuario);
    public VistaEstudiante findByIdEstudiante(Integer idEstudiante);
    public List<VistaEstudiante> findAll();
    public List<VistaEstudiante> findByEstado(Boolean activo);
    public VistaEstudiante findByCorreo(String correo);


}
