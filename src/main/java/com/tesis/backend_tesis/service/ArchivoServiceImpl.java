package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.domain.document.BucketObject;
import com.tesis.backend_tesis.domain.document.IBucket;
import com.tesis.backend_tesis.repository.IArchivoRepository;
import com.tesis.backend_tesis.repository.IUsuariosRepository;
import com.tesis.backend_tesis.repository.modelo.Archivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ArchivoServiceImpl implements IArchivoService{


    @Autowired
    private IArchivoRepository archivoRepository;

    @Autowired
    private IUsuariosRepository usuariosRepository;

    @Autowired
    IBucket bucketDataSource;

    @Override
    public Archivo guardar(MultipartFile file, Integer idUsuario)throws IOException {

        BucketObject bucketObject =  bucketDataSource.uploadFile(file);

        Archivo archivo = new Archivo(bucketObject.getFileName(), bucketObject.getFileUrl(), this.usuariosRepository.buscarPorId(idUsuario));

        return this.archivoRepository.crear(archivo);
    }
}
