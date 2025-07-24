package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Revision;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@Transactional
public class RevisionRepositoryImpl implements IRevisionRepository {


    private static final Logger logger = LogManager.getLogger(RevisionRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Revision insert(Revision revision) {
        try {
            if (revision == null) {
                throw new IllegalArgumentException("La revision no puede ser null");
            }
            this.entityManager.persist(revision);
            return revision;
        } catch (Exception e) {
            logger.error("Error al insertar la revision: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Revision findById(Integer id) {
        try {
            Revision revision = this.entityManager.find(Revision.class, id);

            if (revision != null) {
                logger.info("revision encontrado con ID: {}", id);
            } else {
                logger.warn("No se encontró un revision con ID: {}", id);
            }

            return revision;
        } catch (Exception e) {
            logger.error("Error al buscar el revision con ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al buscar el revision con ID: " + id, e);
        }
    }

    @Override
    public Boolean update(Revision revision) {
        try {
            // Intentamos encontrar la propuesta en la base de datos usando el id de la propuesta
            Revision revision1 = this.entityManager.find(Revision.class, revision.getId());

            if (revision1 != null) {
                // Si la propuesta existe, solo guardamos los cambios sin necesidad de modificar el estado de validación nuevamente
                this.entityManager.merge(revision); // Guarda los cambios en la base de datos

                return true; // Indicamos que la actualización fue exitosa
            } else {
                return false; // No se encontró la propuesta con el ID dado
            }
        } catch (Exception e) {
            // En caso de error, se captura y se imprime la excepción
            e.printStackTrace(); // En producción, reemplazar con un logger
            return false; // Indicamos que hubo un error en la actualización
        }
    }

    @Override
    public Boolean delete(Integer idRevision) {
        try {
            String query = "DELETE FROM Revision r WHERE r.id = :idRevision";
            int deletedCount = this.entityManager.createQuery(query)
                    .setParameter("idRevision", idRevision)
                    .executeUpdate();

            if (deletedCount > 0) {
                logger.info("Revision con ID {} eliminado por falta de verificación.", idRevision);
                return true;
            } else {
                logger.warn("No se encontró Revision con ID {} para eliminar.", idRevision);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error al eliminar Revision con ID {}: {}", idRevision, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<Revision> findByIdPropuesta(Integer idPropuesta) {
        try {
            TypedQuery<Revision> query = this.entityManager.createQuery(
                    "SELECT r FROM Revision r WHERE r.propuesta.id = :idPropuesta",
                    Revision.class
            );
            query.setParameter("idPropuesta", idPropuesta);

            List<Revision> revisions = query.getResultList();
            if (revisions.isEmpty()) {
                logger.warn("No se encontraron revisiones para la propuesta.");
            } else {
                logger.info("Se encontraron {} revisiones .", revisions.size());
            }
            return revisions;
        } catch (NoResultException e) {
            logger.warn("No se encontraron revisiones para esa propuesta.");
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error al buscar revisiones para esa propuesta: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
