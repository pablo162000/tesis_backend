package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Archivo;


public interface IArchivoRepository {

    public Archivo crear(Archivo archivo);
    public Archivo buscarPorId(Integer id);

}
