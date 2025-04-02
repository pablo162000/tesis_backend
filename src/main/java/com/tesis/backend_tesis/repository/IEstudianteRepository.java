package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Docente;
import com.tesis.backend_tesis.repository.modelo.Estudiante;

public interface IEstudianteRepository {

    public Estudiante insert(Estudiante estudiante);

    public Estudiante findById(Integer id);

    public Estudiante findByIdUsuario(Integer idUsuario);

    public Boolean deleteEstudianteByIdUsuario(Integer idUsuario);
}
