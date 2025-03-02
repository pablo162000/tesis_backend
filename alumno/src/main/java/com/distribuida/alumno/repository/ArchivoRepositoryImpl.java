package com.distribuida.alumno.repository;

import com.distribuida.alumno.repository.modelo.Archivo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;


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
