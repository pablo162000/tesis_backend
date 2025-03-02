package com.distribuida.alumno.repository;


import com.distribuida.alumno.repository.modelo.Archivo;

public interface IArchivoRepository {

    public Archivo crear(Archivo archivo);
    public Archivo buscarPorId(Integer id);
   // public String descargaArchivo(String nombreArchivo);
}
