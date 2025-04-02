package com.tesis.backend_tesis.service;


import com.tesis.backend_tesis.service.dto.FacultadDTO;

public interface IFacultadService {

    public FacultadDTO buscarFacultadPorNombre(String nombre);
    public FacultadDTO buscarFacultadPorId(Integer id);

}
