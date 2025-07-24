package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Estudiante;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class EstudianteRepositoryImpl implements IEstudianteRepository{

    private static final Logger logger = LogManager.getLogger(EstudianteRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Estudiante insert(Estudiante estudiante) {
        try {
            if (estudiante == null) {
                throw new IllegalArgumentException("El estudiante no puede ser null");
            }
            this.entityManager.persist(estudiante);
            return estudiante;
        } catch (Exception e) {
            logger.error("Error al insertar el estudiante: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Estudiante findById(Integer id) {
        try {
            Estudiante estudiante = this.entityManager.find(Estudiante.class, id);

            if (estudiante != null) {
                logger.info("Estudiante encontrado con ID: {}", id);
            } else {
                logger.warn("No se encontró un estudiante con ID: {}", id);
            }

            return estudiante;
        } catch (Exception e) {
            logger.error("Error al buscar el estudiante con ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al buscar el estudiante con ID: " + id, e);
        }
    }

    @Override
    public Estudiante findByIdUsuario(Integer idUsuario) {
        try {
            TypedQuery<Estudiante> query = this.entityManager.createQuery(
                    "SELECT e FROM Estudiante e WHERE e.usuario.id = :idUsuario",
                    Estudiante.class
            );
            query.setParameter("idUsuario", idUsuario);

            Estudiante estudiante = query.getSingleResult();
            logger.info("Estudiante encontrado con ID de usuario: {}", idUsuario);
            return estudiante;
        } catch (NoResultException e) {
            logger.warn("No se encontró un estudiante con ID de usuario: {}", idUsuario);
            return null;
        } catch (Exception e) {
            logger.error("Error al buscar el estudiante con ID Usuario {}: {}", idUsuario, e.getMessage(), e);
            throw new RuntimeException("Error al buscar el estudiante con ID usuario: " + idUsuario, e);
        }
    }

    @Override
    public Boolean deleteEstudianteByIdUsuario(Integer idUsuario) {
        try {
            String query = "DELETE FROM Estudiante e WHERE e.usuario.id = :idUsuario";
            int deletedCount = this.entityManager.createQuery(query)
                    .setParameter("idUsuario", idUsuario)
                    .executeUpdate();

            if (deletedCount > 0) {
                logger.info("Estudiante con IDUSUARIO {} eliminado por falta de verificación.", idUsuario);
                return true;
            } else {
                logger.warn("No se encontró el Estudiante con IDUSUARIO {} para eliminar.", idUsuario);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error al eliminar Estudiante con IDUSUARIO {}: {}", idUsuario, e.getMessage(), e);
            return false;
        }
    }
}
