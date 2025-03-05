package com.distribuida.alumno.service;

import com.distribuida.alumno.repository.modelo.Estudiante;

public interface IEstudianteService {

    public Estudiante insertar(Estudiante estudiante);
    //public Estudiante existeConEmail(String correo) ;
    public Estudiante buscarPorId(Integer id);
    public Estudiante buscarPorIdUsuario(Integer iDUsuario);
    public Boolean esCorreoValido(String correo);

    public Boolean existeEstudiante(String ceduula);
}
