package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Docente;
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
public class DocenteRepositoryImpl implements IDocenteRepository{

    private static final Logger logger = LogManager.getLogger(DocenteRepositoryImpl.class);


    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Docente insert(Docente docente) {

        try {
            if (docente == null) {
                throw new IllegalArgumentException("El docente no puede ser null");
            }
            this.entityManager.persist(docente);
            return docente;
        } catch (Exception e) {
            logger.error("Error al insertar el docente: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Docente findById(Integer id) {
        try {
            Docente docente = this.entityManager.find(Docente.class, id);

            if (docente != null) {
                logger.info("Docente encontrado con ID: {}", id);
            } else {
                logger.warn("No se encontró un docente con ID: {}", id);
            }

            return docente;
        } catch (Exception e) {
            logger.error("Error al buscar el docente con ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al buscar el docente con ID: " + id, e);
        }
    }

    @Override
    public Docente findByIdUsuario(Integer idUsuario) {

        try {
            TypedQuery<Docente> query = this.entityManager.createQuery(
                    "SELECT d FROM Docente d WHERE d.usuario.id = :idUsuario",
                    Docente.class
            );
            query.setParameter("idUsuario", idUsuario);

            Docente docente = query.getSingleResult();
            logger.info("Docente encontrado con ID de usuario: {}", idUsuario);
            return docente;
        } catch (NoResultException e) {
            logger.warn("No se encontró un docente con ID de usuario: {}", idUsuario);
            return null;
        } catch (Exception e) {
            logger.error("Error al buscar el docente con ID Usuario {}: {}", idUsuario, e.getMessage(), e);
            throw new RuntimeException("Error al buscar el docente con ID usuario: " + idUsuario, e);
        }
    }

    @Override
    public Boolean deleteDocenteByIdUsuario(Integer idUsuario) {
        try {
            String query = "DELETE FROM Docente d WHERE d.usuario.id = :idUsuario";
            int deletedCount = this.entityManager.createQuery(query)
                    .setParameter("idUsuario", idUsuario)
                    .executeUpdate();

            if (deletedCount > 0) {
                logger.info("Docente con IDUSUARIO {} eliminado por falta de verificación.", idUsuario);
                return true;
            } else {
                logger.warn("No se encontró el Docente con IDUSUARIO {} para eliminar.", idUsuario);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error al eliminar Docente con IDUSUARIO {}: {}", idUsuario, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Docente findByCedula(String cedula) {

        return null;
    }
}
