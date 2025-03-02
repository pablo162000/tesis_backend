package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Archivo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;


@Repository
@Transactional
public class ArchivoRepositoryImpl implements IArchivoRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Archivo crear(Archivo archivo) {
        this.entityManager.persist(archivo);
        return archivo;
    }

    @Override
    public Archivo buscarPorId(Integer id) {
        return null;
    }


}
