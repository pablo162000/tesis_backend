package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.VistaCarrera;
import com.tesis.backend_tesis.repository.modelo.VistaUsuarioRol;

import java.util.List;

public interface IVistaUsuarioRolRepository {

    public VistaUsuarioRol findByIdUsuarioRol(Integer idUsuarioRol);
    public VistaUsuarioRol findByCorreo(String correo);
    public List<VistaUsuarioRol> findByIdRol(Integer idRol);
    public List<VistaUsuarioRol> findByNombreRol(String nombreRol);
    public List<VistaUsuarioRol> findByIdUsuario(Integer idUsuario);
    public List<VistaUsuarioRol> findByApellidos(String apellidos);
    public List<VistaUsuarioRol> findByNombres(String nombres);
    public List<VistaUsuarioRol> findAll();
    public List<VistaUsuarioRol> findByNombreRolAndEstado(String nombreRol, Boolean estado);




}
