package com.distribuida.alumno.repository;

import com.distribuida.alumno.repository.modelo.Estudiante;

import java.util.List;

public interface IEstudianteRepository {

    public Estudiante insertar(Estudiante estudiantes);
    public Estudiante findByIdUsuario (Integer idUsuario);
    public Estudiante existeEstudiante (String correo);
    public Estudiante findById (Integer id);
    public Estudiante findByCedula(String cedula);

    public Estudiante findByCedulaUsuario(String cedula);

    public Boolean delete(Integer idUsuario);



}
