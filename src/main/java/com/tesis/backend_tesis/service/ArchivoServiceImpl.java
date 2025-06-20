package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.domain.document.BucketObject;
import com.tesis.backend_tesis.domain.document.IBucket;
import com.tesis.backend_tesis.repository.IArchivoRepository;
import com.tesis.backend_tesis.repository.IUsuarioRepository;
import com.tesis.backend_tesis.repository.IUsuariosRepository;
import com.tesis.backend_tesis.repository.modelo.Archivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.io.IOException;

@Service
public class ArchivoServiceImpl implements IArchivoService{


    @Autowired
    private IArchivoRepository archivoRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    IBucket bucketDataSource;

    @Override
    public Archivo guardar(MultipartFile file, Integer idUsuario, String nombre)throws IOException {

        BucketObject bucketObject =  bucketDataSource.uploadFile(file, nombre);

        Archivo archivo = new Archivo(bucketObject.getFileName(), bucketObject.getFileUrl(), this.usuarioRepository.findById(idUsuario));

        return this.archivoRepository.crear(archivo);
    }



}
