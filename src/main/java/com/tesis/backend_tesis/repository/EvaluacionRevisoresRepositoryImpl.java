package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.EvaluacionRevisor;
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
public class EvaluacionRevisoresRepositoryImpl implements IEvaluacionRevisoresRepository {

    private static final Logger logger = LogManager.getLogger(UsuarioRolRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public EvaluacionRevisor insert(EvaluacionRevisor evaluacionRevisor) {
        try {
            if (evaluacionRevisor == null) {
                throw new IllegalArgumentException("La evaluacion del revisor no puede ser null");
            }
            this.entityManager.persist(evaluacionRevisor);
            return evaluacionRevisor;
        } catch (Exception e) {
            logger.error("Error al insertar evaluacion del revisor: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<EvaluacionRevisor> findByIdDocente(Integer idDocente) {
        try {
            TypedQuery<EvaluacionRevisor> query = this.entityManager.createQuery(
                    "SELECT e FROM EvaluacionRevisor e WHERE e.revisor = :idDocente",
                    EvaluacionRevisor.class
            );
            query.setParameter("idDocente", idDocente);

            List<EvaluacionRevisor> evaluacionRevisors = query.getResultList();
            if (evaluacionRevisors.isEmpty()) {
                logger.warn("No se encontraron evaluaciones de revisores para el docente.");
            } else {
                logger.info("Se encontraron {} evaluaciones de revision para el docente.", evaluacionRevisors.size());
            }
            return evaluacionRevisors;
        } catch (NoResultException e) {
            logger.warn("No se encontraron evaluaciones de revision para el docente.");
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error al buscar evaluaciones de revision para el docente: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public EvaluacionRevisor update(EvaluacionRevisor evaluacionRevisor) {
        this.entityManager.merge(evaluacionRevisor);
        return evaluacionRevisor;
    }

    @Override
    public List<EvaluacionRevisor> findByIdRevision(Integer idRevision) {
        try {
            TypedQuery<EvaluacionRevisor> query = this.entityManager.createQuery(
                    "SELECT e FROM EvaluacionRevisor e WHERE e.revision = :idRevision",
                    EvaluacionRevisor.class
            );
            query.setParameter("idRevision", idRevision);

            List<EvaluacionRevisor> evaluacionRevisors = query.getResultList();
            if (evaluacionRevisors.isEmpty()) {
                logger.warn("No se encontraron evaluaciones de revisores para esa revision.");
            } else {
                logger.info("Se encontraron {} evaluaciones de revision para la revision.", evaluacionRevisors.size());
            }
            return evaluacionRevisors;
        } catch (NoResultException e) {
            logger.warn("No se encontraron evaluaciones de revision.");
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error al buscar evaluaciones de revision para la revision: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
