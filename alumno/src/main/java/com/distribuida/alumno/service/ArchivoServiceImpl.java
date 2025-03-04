package com.distribuida.alumno.service;


import com.distribuida.alumno.bucket.BucketObject;
import com.distribuida.alumno.bucket.IBucket;
import com.distribuida.alumno.clients.AdministrativoRestClient;
import com.distribuida.alumno.repository.IArchivoRepository;
import com.distribuida.alumno.repository.IEstudianteRepository;
import com.distribuida.alumno.repository.modelo.Archivo;
import com.distribuida.alumno.repository.modelo.Estudiante;
import com.distribuida.alumno.service.dto.DocenteDTO;
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
    private AdministrativoRestClient administrativoRestClient;

    @Autowired
    private IBucket bucketDataSource;

    @Override
    public Archivo guardar(MultipartFile file, Integer idUsuario, String rol)throws IOException {

        BucketObject bucketObject = bucketDataSource.uploadFile(file);
        Integer propietarioId = null;

        if ("estudiante".equals(rol)) {
            Estudiante estudiante = estudianteRepository.findById(idUsuario);
            if (estudiante != null) {
                propietarioId = estudiante.getIdUsuario();
            }
        } else if ("administrativo".equals(rol)) {
            DocenteDTO docente = administrativoRestClient.obtenerDocente(idUsuario);
            if (docente != null) {
                propietarioId = docente.getIdUsuario();
            }
        }

        if (propietarioId == null) {
            throw new IllegalArgumentException("Rol no válido o usuario no encontrado");
        }

        Archivo archivo = new Archivo(bucketObject.getFileName(), bucketObject.getFileUrl(), propietarioId);
        return archivoRepository.crear(archivo);
    }


}
