package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.VistaCarrera;


import java.util.List;

public interface IVistaCarreraRepository {


    public VistaCarrera findByIdCarrera(Integer idCarrera);
    public List<VistaCarrera> findByIdFacultad(Integer idFacultad);
    public List<VistaCarrera> findAllCarreras();
    public VistaCarrera findByNombreCarrera(String nombreCarrera);
    public VistaCarrera findByIdUsuarioCoordiandor(Integer idUsuarioCarrera);

}
