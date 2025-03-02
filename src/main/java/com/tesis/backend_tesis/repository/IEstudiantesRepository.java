package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Estudiantes;
import com.tesis.backend_tesis.service.dto.EstudianteDTO;

public interface IEstudiantesRepository {

    public Estudiantes insertar(Estudiantes estudiantes);
    public Estudiantes findByIdUsuario (Integer num);
    public Estudiantes existeEstudiante (String correo);
    public Estudiantes findById (Integer id);
}
