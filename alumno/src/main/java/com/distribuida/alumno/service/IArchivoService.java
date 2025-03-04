package com.distribuida.alumno.service;

import com.distribuida.alumno.repository.modelo.Archivo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface IArchivoService {

    public Archivo guardar(MultipartFile file, Integer idUsuario, String rol)throws IOException;

}
