package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Estudiantes;

public interface IEstudiantesRepository {

    public Estudiantes insertar(Estudiantes estudiantes);
    public Estudiantes findByIdUsuario (Integer num);
}
