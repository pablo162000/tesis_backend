package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.VistaSecretaria;
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
public class VistaSecretariaRepositoryImpl implements IVistaSecretariaRepository {

    private static final Logger logger = LogManager.getLogger(VistaSecretariaRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public VistaSecretaria findByIdUsuario(Integer idUsuario) {
        try {
            VistaSecretaria vistaSecretaria = this.entityManager.find(VistaSecretaria.class, idUsuario);

            if (vistaSecretaria != null) {
                logger.info("Secretaria encontrado en VistaSecretaria con IDUSUARIO: {}", idUsuario);
            } else {
                logger.warn("No se encontró secretaria en VistaSecretaria con IDUSUARIO: {}", idUsuario);
            }

            return vistaSecretaria;
        } catch (Exception e) {
            logger.error("Error al buscar secretaria en VistaSecretaria con IDUSUARIO {}: {}", idUsuario, e.getMessage(), e);
            throw new RuntimeException("Error al buscar secretaria en VistaSecretaria con IDUSUARIO: " + idUsuario, e);
        }
    }

    @Override
    public VistaSecretaria findByIdSecretaria(Integer idSecretaria) {
        try {
            TypedQuery<VistaSecretaria> query = this.entityManager.createQuery(
                    "SELECT s FROM VistaSecretaria s WHERE s.idSecretaria = :idSecretaria",
                    VistaSecretaria.class
            );
            query.setParameter("idSecretaria", idSecretaria);

            VistaSecretaria vistaSecretaria = query.getSingleResult();
            logger.info("Secretaria encontrado en VistaSecretaria con IDSECRETARIA: {}", idSecretaria);
            return vistaSecretaria;
        } catch (NoResultException e) {
            logger.warn("No se encontró secretaria en VistaSecretaria con IDSECRETARIA: {}", idSecretaria);
            return null;
        } catch (Exception e) {
            logger.error("Error al buscar secretaria en VistaSecretaria con IDSECRETARIA {}: {}", idSecretaria, e.getMessage(), e);
            throw new RuntimeException("Error al buscar secretaria en VistaSecretaria con IDSECRETARIA: " + idSecretaria, e);
        }
    }

    @Override
    public List<VistaSecretaria> findAll() {
        try {
            TypedQuery<VistaSecretaria> query = this.entityManager.createQuery(
                    "SELECT s FROM VistaSecretaria s",
                    VistaSecretaria.class
            );

            List<VistaSecretaria> vistaSecretaria = query.getResultList();
            if (vistaSecretaria.isEmpty()) {
                logger.warn("No se encontraron secreatarias en VistaSecretaria.");
            } else {
                logger.info("Se encontraron {} secretarias en VistaSecretaria .", vistaSecretaria.size());
            }
            return vistaSecretaria;
        } catch (NoResultException e) {
            logger.warn("No se encontraron secretarias en VistaSecretaria.");
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error al buscar secretarias en VistaSecretaria: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaSecretaria> findByEstado(Boolean estado) {
        try {
            TypedQuery<VistaSecretaria> query = this.entityManager.createQuery(
                    "SELECT s FROM VistaSecretaria s WHERE s.activo = :estado",
                    VistaSecretaria.class
            );
            query.setParameter("estado", estado);
            List<VistaSecretaria> vistaSecretaria = query.getResultList();

            if (vistaSecretaria.isEmpty()) {
                logger.debug("No se encontraron secretarias en VistaSecretaria con estado {}.", estado);
            } else {
                logger.debug("Se encontraron {} secretarias en VistaSecretaria con estado {}.", vistaSecretaria.size(), estado);
            }

            return vistaSecretaria;
        } catch (Exception e) {
            logger.error("Error al buscar secretarias en VistaSecretaria con estado: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaSecretaria> findByEstadoCarrera(Boolean estado, String carrera) {

        try {
            TypedQuery<VistaSecretaria> query = this.entityManager.createQuery(
                    "SELECT s FROM VistaSecretaria s WHERE s.activo = :estado AND s.carrera = :carrera",
                    VistaSecretaria.class
            );
            query.setParameter("estado", estado);
            query.setParameter("carrera", carrera);
            List<VistaSecretaria> vistaSecretaria = query.getResultList();



            if (vistaSecretaria.isEmpty()) {
                logger.debug("No se encontraron secretarias en VistaSecretaria con estado {} y carrera {}.", estado, carrera);
            } else {
                logger.debug("Se encontraron {} secretarias en VistaSecretaria con estado {} y carrera {}.", vistaSecretaria.size(), estado, carrera);
            }

            return vistaSecretaria;
        } catch (Exception e) {
            logger.error("Error al buscar secretarias en VistaSecretaria con estado: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaSecretaria> findByEstadoFacultad(Boolean estado, String facultad) {
        try {
            TypedQuery<VistaSecretaria> query = this.entityManager.createQuery(
                    "SELECT s FROM VistaSecretaria s WHERE s.activo = :estado AND s.facultad = :facultad",
                    VistaSecretaria.class
            );
            query.setParameter("estado", estado);
            query.setParameter("facultad", facultad);
            List<VistaSecretaria> vistaSecretaria = query.getResultList();



            if (vistaSecretaria.isEmpty()) {
                logger.debug("No se encontraron secretarias en VistaSecretaria con estado {} y carrera {}.", estado, facultad);
            } else {
                logger.debug("Se encontraron {} secretarias en VistaSecretaria con estado {} y carrera {}.", vistaSecretaria.size(), estado, facultad);
            }

            return vistaSecretaria;
        } catch (Exception e) {
            logger.error("Error al buscar secretarias en VistaSecretaria con estado: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }


}
