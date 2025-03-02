package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.repository.modelo.Estudiantes;

public interface IEstudiantesService {

    public Estudiantes insertar(Estudiantes estudiantes);
    public Estudiantes existeConEmail(String correo) ;
    public Estudiantes buscarPorId(Integer id);

}
