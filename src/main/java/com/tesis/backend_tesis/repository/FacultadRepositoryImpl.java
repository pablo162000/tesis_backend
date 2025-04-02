package com.tesis.backend_tesis.repository;


import com.tesis.backend_tesis.repository.modelo.Facultad;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class FacultadRepositoryImpl implements IFacultadRepository {

    private static final Logger logger = LogManager.getLogger(FacultadRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Facultad findById(Integer id) {
        try {
            Facultad facultad = this.entityManager.find(Facultad.class, id);

            if (facultad != null) {
                logger.info("Facultad encontrado con ID: {}", id);
            } else {
                logger.warn("No se encontró una facultad con ID: {}", id);
            }
            return facultad;
        } catch (Exception e) {
            logger.error("Error al buscar la facultad con ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al buscar la facultad con ID: " + id, e);
        }
    }

    @Override
    public Facultad findByNombre(String nombre) {
        try {

            TypedQuery<Facultad> myQuery = this.entityManager.createQuery("SELECT f FROM Facultad f WHERE f.nombre=:nombre",
                    Facultad.class);
            myQuery.setParameter("nombre", nombre);
            Facultad facultad = myQuery.getSingleResult();
            logger.info("Facultad encontrada con nombre: {}", nombre);
            return facultad;
        } catch (NoResultException e) {
            logger.warn("No se encontró una facultad con nombre: {}", nombre);
            return null;
        } catch (Exception e) {
            logger.error("Error al buscar facultad con nombre {}: {}", nombre, e.getMessage(), e);
            throw new RuntimeException("Error al buscar la facultad con nombre: " + nombre, e);
        }
    }

    @Override
    public List<Facultad> findAll() {
        return List.of();
    }
}
