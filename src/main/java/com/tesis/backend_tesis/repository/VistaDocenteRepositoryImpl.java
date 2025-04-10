package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.VistaDocente;

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
public class VistaDocenteRepositoryImpl implements IVistaDocenteRepository {

    private static final Logger logger = LogManager.getLogger(VistaDocenteRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public VistaDocente findByIdUsuario(Integer idUsuario) {
        try {
            VistaDocente vistaDocente = this.entityManager.find(VistaDocente.class, idUsuario);

            if (vistaDocente != null) {
                logger.info("Docente encontrado en VistaDocentes con IDUSUARIO: {}", idUsuario);
            } else {
                logger.warn("No se encontró un Docente en VistaDocentes con IDUSUARIO: {}", idUsuario);
            }

            return vistaDocente;
        } catch (Exception e) {
            logger.error("Error al buscar el Docente en VistaDocentes con IDUSUARIO {}: {}", idUsuario, e.getMessage(), e);
            throw new RuntimeException("Error al buscar el Docente en VistaDocentes con IDUSUARIO: " + idUsuario, e);
        }
    }

    @Override
    public VistaDocente findByIdDocente(Integer idDocente) {
        try {
            TypedQuery<VistaDocente> query = this.entityManager.createQuery(
                    "SELECT d FROM VistaDocente d WHERE d.idDocente = :idDocente",
                    VistaDocente.class
            );
            query.setParameter("idDocente", idDocente);

            VistaDocente vistaDocente = query.getSingleResult();
            logger.info("Docente encontrado en VistaDocentes con IDDOCENTE: {}", idDocente);
            return vistaDocente;
        } catch (NoResultException e) {
            logger.warn("No se encontró un Docente en VistaDocentes con IDDOCENTE: {}", idDocente);
            return null;
        } catch (Exception e) {
            logger.error("Error al buscar el Docente en VistaDocentes con IDDOCENTE {}: {}", idDocente, e.getMessage(), e);
            throw new RuntimeException("Error al buscar el estudiante en VistaDocentes con IDDOCENTE: " + idDocente, e);
        }
    }

    @Override
    public List<VistaDocente> findAll() {
        try {
            TypedQuery<VistaDocente> query = this.entityManager.createQuery(
                    "SELECT d FROM VistaDocente d",
                    VistaDocente.class
            );

            List<VistaDocente> vistaDocentes = query.getResultList();
            if (vistaDocentes.isEmpty()) {
                logger.warn("No se encontraron docentes en VistaDocentes.");
            } else {
                logger.info("Se encontraron {} docentes en VistaDocentes.", vistaDocentes.size());
            }
            return vistaDocentes;
        } catch (NoResultException e) {
            logger.warn("No se encontraron docentes en VistaDocentes.");
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error al buscar docentes en VistaDocentes: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaDocente> findByEstado(Boolean estado) {
        try {
            TypedQuery<VistaDocente> query = this.entityManager.createQuery(
                    "SELECT e FROM VistaDocente e WHERE e.activo = :estado",
                    VistaDocente.class
            );
            query.setParameter("estado", estado);
            List<VistaDocente> vistaDocentes = query.getResultList();

            if (vistaDocentes.isEmpty()) {
                logger.debug("No se encontraron docentes en VistaDocentes con estado {}.", estado);
            } else {
                logger.debug("Se encontraron {} docentes en VistaDocentes con estado {}.", vistaDocentes.size(), estado);
            }

            return vistaDocentes;
        } catch (Exception e) {
            logger.error("Error al buscar docentes en VistaDocentes con estado: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public VistaDocente findByCorreo(String correo) {
        try {
            TypedQuery<VistaDocente> query = this.entityManager.createQuery(
                    "SELECT d FROM VistaDocente d WHERE d.correo = :correo",
                    VistaDocente.class
            );
            query.setParameter("correo", correo);

            VistaDocente vistaDocente = query.getSingleResult();
            logger.info("Docente encontrado en VistaDocente con correo: {}", correo);
            return vistaDocente;
        } catch (NoResultException e) {
            logger.warn("No se encontró un Docente en VistaDocente con correo: {}", correo);
            return null;
        } catch (Exception e) {
            logger.error("Error al buscar el Docente en VistaDocente con correo {}: {}", correo, e.getMessage(), e);
            throw new RuntimeException("Error al buscar el Docente en VistaDocente con correo: " + correo, e);
        }
    }

    @Override
    public List<VistaDocente> findByNomBreFacultad(String nomBreFacultad) {
        try {
            TypedQuery<VistaDocente> query = this.entityManager.createQuery(
                    "SELECT d FROM VistaDocente d WHERE d.facultad = :nomBreFacultad",
                    VistaDocente.class
            );
            query.setParameter("nomBreFacultad", nomBreFacultad);
            List<VistaDocente> vistaDocentes = query.getResultList();

            if (vistaDocentes.isEmpty()) {
                logger.debug("No se encontraron docentes en VistaDocentes con Facultad {}.", nomBreFacultad);
            } else {
                logger.debug("Se encontraron {} docentes en VistaDocentes con Facultad {}.", vistaDocentes.size(), nomBreFacultad);
            }

            return vistaDocentes;
        } catch (Exception e) {
            logger.error("Error al buscar docentes en VistaDocentes con Facultad: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }


}
