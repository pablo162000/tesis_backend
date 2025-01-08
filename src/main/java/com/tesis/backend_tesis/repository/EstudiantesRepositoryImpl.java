package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Estudiantes;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.NoResultException;

@Repository
@Transactional
public class EstudiantesRepositoryImpl implements IEstudiantesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Estudiantes insertar(Estudiantes estudiantes) {
        this.entityManager.persist(estudiantes);
        return estudiantes;
    }

}
