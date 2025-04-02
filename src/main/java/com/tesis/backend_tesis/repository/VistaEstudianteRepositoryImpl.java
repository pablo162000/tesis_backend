package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.VistaEstudiante;
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
public class VistaEstudianteRepositoryImpl implements IVistaEstudianteRepository {

    private static final Logger logger = LogManager.getLogger(VistaEstudianteRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public VistaEstudiante findByIdUsuario(Integer idUsuario) {
        try {
            VistaEstudiante vistaEstudiante = this.entityManager.find(VistaEstudiante.class, idUsuario);

            if (vistaEstudiante != null) {
                logger.info("Estudiante encontrado en VistaEstudiante con IDUSUARIO: {}", idUsuario);
            } else {
                logger.warn("No se encontró un estudiante en VistaEstudiante con IDUSUARIO: {}", idUsuario);
            }

            return vistaEstudiante;
        } catch (Exception e) {
            logger.error("Error al buscar el estudiante en VistaEstudiante con IDUSUARIO {}: {}", idUsuario, e.getMessage(), e);
            throw new RuntimeException("Error al buscar el estudiante en VistaEstudiante con IDUSUARIO: " + idUsuario, e);
        }
    }

    @Override
    public VistaEstudiante findByIdEstudiante(Integer idEstudiante) {
        try {
            TypedQuery<VistaEstudiante> query = this.entityManager.createQuery(
                    "SELECT e FROM VistaEstudiante e WHERE e.idEstudiante = :idEstudiante",
                    VistaEstudiante.class
            );
            query.setParameter("idEstudiante", idEstudiante);

            VistaEstudiante vistaEstudiante = query.getSingleResult();
            logger.info("Estudiante encontrado en VistaEstudiante con IDESTUDIANTE: {}", idEstudiante);
            return vistaEstudiante;
        } catch (NoResultException e) {
            logger.warn("No se encontró un estudiante en VistaEstudiante con IDESTUDIANTE: {}", idEstudiante);
            return null;
        } catch (Exception e) {
            logger.error("Error al buscar el estudiante en VistaEstudiante con IDESTUDIANTE {}: {}", idEstudiante, e.getMessage(), e);
            throw new RuntimeException("Error al buscar el estudiante en VistaEstudiante con IDESTUDIANTE: " + idEstudiante, e);
        }
    }

    @Override
    public List<VistaEstudiante> findAll() {
        try {
            TypedQuery<VistaEstudiante> query = this.entityManager.createQuery(
                    "SELECT e FROM VistaEstudiante e",
                    VistaEstudiante.class
            );

            List<VistaEstudiante> vistaEstudiantes = query.getResultList();
            if (vistaEstudiantes.isEmpty()) {
                logger.warn("No se encontraron estudiantes en VistaEstudiantes.");
            } else {
                logger.info("Se encontraron {} estudiantes en VistaEstudiantes .", vistaEstudiantes.size());
            }
            return vistaEstudiantes;
        } catch (NoResultException e) {
            logger.warn("No se encontraron estudiantes en VistaEstudiantes.");
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error al buscar estudiantes en VistaEstudiantes: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaEstudiante> findByEstado(Boolean estado) {
        try {
            TypedQuery<VistaEstudiante> query = this.entityManager.createQuery(
                    "SELECT e FROM VistaEstudiante e WHERE e.activo = :estado",
                    VistaEstudiante.class
            );
            query.setParameter("estado", estado);
            List<VistaEstudiante> vistaEstudiantes = query.getResultList();

            if (vistaEstudiantes.isEmpty()) {
                logger.debug("No se encontraron estudiantes en VistaEstudiantes con estado {}.", estado);
            } else {
                logger.debug("Se encontraron {} estudiantes en VistaEstudiantes con estado {}.", vistaEstudiantes.size(), estado);
            }

            return vistaEstudiantes;
        } catch (Exception e) {
            logger.error("Error al buscar estudiantes en VistaEstudiantes con estado: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public VistaEstudiante findByCorreo(String correo) {
        try {
            TypedQuery<VistaEstudiante> query = this.entityManager.createQuery(
                    "SELECT e FROM VistaEstudiante e WHERE e.correo = :correo",
                    VistaEstudiante.class
            );
            query.setParameter("correo", correo);

            VistaEstudiante vistaEstudiante = query.getSingleResult();
            logger.info("Estudiante encontrado en VistaEstudiante con correo: {}", correo);
            return vistaEstudiante;
        } catch (NoResultException e) {
            logger.warn("No se encontró un estudiante en VistaEstudiante con correo: {}", correo);
            return null;
        } catch (Exception e) {
            logger.error("Error al buscar el estudiante en VistaEstudiante con correo {}: {}", correo, e.getMessage(), e);
            throw new RuntimeException("Error al buscar el estudiante en VistaEstudiante con correo: " + correo, e);
        }
    }


}
