package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.repository.modelo.*;

import java.util.List;

public interface IVistasEntidadesService {

    //------------------Estudianttes--------------------------------------
    public VistaEstudiante buscarEstudiantePorIdUsuario(Integer idUsuario);
    public VistaEstudiante buscarEstudiantePorIdEstudiante (Integer idEstudiante);
    public List<VistaEstudiante> buscarTodosEstudiantes();
    public List<VistaEstudiante> buscarEstudiantesPorEstadoActivacion(Boolean estado);
    public VistaEstudiante buscarPorCorreoEstudainte(String correoEstudiante);

    //------------------Docentes--------------------------------------

    public VistaDocente buscarDocentePorIdUsuario(Integer idUsuario);
    public VistaDocente buscarDocentePorIdDocente(Integer idDocente);
    public List<VistaDocente> buscarTodosDocente();
    public List<VistaDocente> buscarDocentesPorEstado(Boolean activo);
    public VistaDocente buscarPorCorreoDocente(String correoDocente);

    //------------------Secretaerias----------------------------------
    public VistaSecretaria buscarSecretariaPorIdUsuario(Integer idUsuario);
    public VistaSecretaria buscarSecretariaPorIdSecretaria(Integer idDocente);
    public List<VistaSecretaria> buscarTodosSecretarias();
    public List<VistaSecretaria> buscarSecretariasPorEstado(Boolean activo);


    //------------------Carrera--------------------------------------

    public VistaCarrera buscarCarreraPorIdCarrera(Integer idCarrera);
    public List<VistaCarrera> buscarCarreraIdFacultad(Integer idFacultad);
    public List<VistaCarrera> buscarTodasCarreras();
    public VistaCarrera buscarCarreraPorNombreCarrera(String nombreCarrera);



    //------------------UsuarioRol--------------------------------------


    public VistaUsuarioRol buscarUsuarioRolPorIdUsuarioRol(Integer idUsuarioRol);
    public VistaUsuarioRol buscarUsuarioRolPorCorreo(String correo);
    public List<VistaUsuarioRol> buscarUsuarioRolPorIdRol(Integer idRol);
    public List<VistaUsuarioRol> buscarUsuarioRolPorNombreRol(String nombreRol);
    public List<VistaUsuarioRol> buscarUsuarioRolPorIdUsuario(Integer idUsuario);
    public List<VistaUsuarioRol> buscarUsuarioRolPorApellidos(String apellidos);
    public List<VistaUsuarioRol> buscarUsuarioRolPorNombres(String nombres);
    public List<VistaUsuarioRol> buscarTodosUsuarioRol();

}
