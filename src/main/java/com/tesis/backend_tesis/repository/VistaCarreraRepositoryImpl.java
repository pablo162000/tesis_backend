package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.VistaCarrera;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Repository
@Transactional
public class VistaCarreraRepositoryImpl implements IVistaCarreraRepository {

    private static final Logger logger = LogManager.getLogger(VistaCarreraRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public VistaCarrera findByIdCarrera(Integer idCarrera) {

        try {
            VistaCarrera vistaCarrera = this.entityManager.find(VistaCarrera.class, idCarrera);

            if (vistaCarrera != null) {
                logger.info("Carrera encontrado en VistaCarrera con IDCARRERA: {}", idCarrera);
            } else {
                logger.warn("No se encontró una Carrera en VistaCarrera con IDCARRERA: {}", idCarrera);
            }

            return vistaCarrera;
        } catch (Exception e) {
            logger.error("Error al buscar el Carrera en VistaCarrera con IDCARRERA {}: {}", idCarrera, e.getMessage(), e);
            throw new RuntimeException("Error al buscar Carrera en VistaCarrera con IDCARRERA: " + idCarrera, e);
        }
    }

    @Override
    public List<VistaCarrera> findByIdFacultad(Integer idFacultad) {

        try {
            TypedQuery<VistaCarrera> query = this.entityManager.createQuery(
                    "SELECT c FROM VistaCarrera c WHERE c.idFacultad = :idFacultad",
                    VistaCarrera.class
            );
            query.setParameter("idFacultad", idFacultad);

            List<VistaCarrera> vistaCarrera = query.getResultList();
            if (vistaCarrera.isEmpty()) {
                logger.warn("No se encontraron carreras con IDFACULTAD {} en VistaCarrera.", idFacultad);
            } else {
                logger.info("Se encontraron {} carreras en VistaCarrera.", vistaCarrera.size());
            }
            return vistaCarrera;
        } catch (NoResultException e) {
            logger.warn("No se encontraron carreras en VistaCarrera.");
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error al buscar carreras en VistaCarrera: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaCarrera> findAllCarreras() {

        try {
            TypedQuery<VistaCarrera> query = this.entityManager.createQuery(
                    "SELECT c FROM VistaCarrera c",
                    VistaCarrera.class
            );

            List<VistaCarrera> vistaCarrera = query.getResultList();
            if (vistaCarrera.isEmpty()) {
                logger.warn("No se encontraron carreras en VistaCarrera.");
            } else {
                logger.info("Se encontraron {} carreras en VistaCarrera.", vistaCarrera.size());
            }
            return vistaCarrera;
        } catch (NoResultException e) {
            logger.warn("No se encontraron carreras en VistaCarrera.");
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error al buscar carreras en VistaCarrera: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public VistaCarrera findByNombreCarrera(String nombreCarrera) {

        try {
            TypedQuery<VistaCarrera> query = this.entityManager.createQuery(
                    "SELECT c FROM VistaCarrera c WHERE c.carrera = :nombreCarrera",
                    VistaCarrera.class
            );
            query.setParameter("nombreCarrera", nombreCarrera);

            VistaCarrera vistaCarrera = query.getSingleResult();
            logger.info("Carrera encontrada en VistaCarrera con NOMBRECARRERA: {}", nombreCarrera);
            return vistaCarrera;
        } catch (NoResultException e) {
            logger.warn("No se encontró una carrera en VistaCarrera con NOMBRECARRERA: {}", nombreCarrera);
            return null;
        } catch (Exception e) {
            logger.error("Error al buscar carrera en VistaCarrera con NOMBRECARRERA {}: {}", nombreCarrera, e.getMessage(), e);
            throw new RuntimeException("Error al buscar carrera en VistaCarrera con NOMBRECARRERA: " + nombreCarrera, e);
        }

    }

    @Override
    public VistaCarrera findByIdUsuarioCoordiandor(Integer idUsuarioCoordinador) {
        try {
            TypedQuery<VistaCarrera> query = this.entityManager.createQuery(
                    "SELECT c FROM VistaCarrera c WHERE c.idCoordinador = :idUsuarioCoordinador",
                    VistaCarrera.class);

            query.setParameter("idUsuarioCoordinador", idUsuarioCoordinador);

            VistaCarrera vistaCarrera = query.getSingleResult();

            logger.info("Carrera encontrado en VistaCarrera con IDUSUARIOCOORDIANDOR: {}", idUsuarioCoordinador);
            return vistaCarrera;

        } catch (NoResultException e) {
            logger.warn("No se encontró una Carrera en VistaCarrera con IDUSUARIOCOORDIANDOR: {}", idUsuarioCoordinador);
            return null; // o lanza una excepción personalizada si lo prefieres
        } catch (Exception e) {
            logger.error("Error al buscar el Carrera en VistaCarrera con IDUSUARIOCARRERA {}: {}", idUsuarioCoordinador, e.getMessage(), e);
            throw new RuntimeException("Error al buscar Carrera en VistaCarrera con IDUSUARIOCARRERA: " + idUsuarioCoordinador, e);
        }
    }

}
