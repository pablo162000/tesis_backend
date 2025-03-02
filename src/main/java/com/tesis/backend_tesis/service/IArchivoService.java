package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.repository.modelo.Archivo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface IArchivoService {

    public Archivo guardar(MultipartFile file, Integer idUsuario)throws IOException;

}
