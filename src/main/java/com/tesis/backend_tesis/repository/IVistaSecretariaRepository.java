package com.tesis.backend_tesis.repository;


import com.tesis.backend_tesis.repository.modelo.VistaSecretaria;

import java.util.List;

public interface IVistaSecretariaRepository {

    public VistaSecretaria findByIdUsuario(Integer idUsuario);
    public VistaSecretaria findByIdSecretaria(Integer idSecretaria);
    public List<VistaSecretaria> findAll();
    public List<VistaSecretaria> findByEstado(Boolean activo);
    public List<VistaSecretaria> findByEstadoCarrera(Boolean activo, String carrera);
    public List<VistaSecretaria> findByEstadoFacultad(Boolean activo, String facultad);


}
