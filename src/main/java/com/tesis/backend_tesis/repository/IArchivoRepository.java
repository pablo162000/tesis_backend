package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Archivo;
import com.tesis.backend_tesis.repository.modelo.Propuesta;
import org.springframework.web.multipart.MultipartFile;

public interface IArchivoRepository {

    public Archivo crear(Archivo archivo);
    public Archivo buscarPorId(Integer id);
   // public String descargaArchivo(String nombreArchivo);
}
