package com.distribuida.alumno.service;


import com.distribuida.alumno.bucket.BucketObject;
import com.distribuida.alumno.bucket.IBucket;
import com.distribuida.alumno.repository.IArchivoRepository;
import com.distribuida.alumno.repository.IEstudianteRepository;
import com.distribuida.alumno.repository.modelo.Archivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ArchivoServiceImpl implements IArchivoService{


    @Autowired
    private IArchivoRepository archivoRepository;

    @Autowired
    private IEstudianteRepository estudianteRepository;

    @Autowired
    private IBucket bucketDataSource;

    @Override
    public Archivo guardar(MultipartFile file, Integer idUsuario)throws IOException {

        BucketObject bucketObject =  bucketDataSource.uploadFile(file);


        Archivo archivo = new Archivo(bucketObject.getFileName(), bucketObject.getFileUrl(), this.estudianteRepository.findById(idUsuario).getIdUsuario());

         return this.archivoRepository.crear(archivo);
    }
}
