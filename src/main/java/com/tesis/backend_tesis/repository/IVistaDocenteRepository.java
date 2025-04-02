package com.tesis.backend_tesis.repository;


import com.tesis.backend_tesis.repository.modelo.VistaDocente;
import com.tesis.backend_tesis.repository.modelo.VistaEstudiante;

import java.util.List;

public interface IVistaDocenteRepository {

    public VistaDocente findByIdUsuario(Integer idUsuario);
    public VistaDocente findByIdDocente(Integer idDocente);
    public List<VistaDocente> findAll();
    public List<VistaDocente> findByEstado(Boolean activo);
    public VistaDocente findByCorreo(String correo);


}
