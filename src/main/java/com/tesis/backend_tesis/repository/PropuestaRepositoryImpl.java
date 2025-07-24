package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.EstadoAprobacion;
import com.tesis.backend_tesis.repository.modelo.EstadoValidacion;
import com.tesis.backend_tesis.repository.modelo.Propuesta;

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
public class PropuestaRepositoryImpl implements IPropuestaRepository {

    private static final Logger logger = LogManager.getLogger(PropuestaRepositoryImpl.class);



    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Propuesta insert(Propuesta propuesta) {

        try {
            if (propuesta == null) {
                throw new IllegalArgumentException("La propuesta no puede ser null");
            }
            this.entityManager.persist(propuesta);
            return propuesta;
        } catch (Exception e) {
            logger.error("Error al insertar la propuesta: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Propuesta buscarPorId(Integer id) {

        Propuesta prop =null;

        try {

            TypedQuery<Propuesta> myQuery = this.entityManager.createQuery(
                    "SELECT p FROM Propuesta p WHERE p.id = :id",
                    Propuesta.class
            );
            prop = myQuery.setParameter("id", id).getSingleResult();

            return  prop;
        } catch (NoResultException e) {

            return null;

        }
    }

    @Override
    public List<Propuesta> findByEstudianteId(Integer idEstudiante) {
        try {

            TypedQuery<Propuesta> myQuery = this.entityManager.createQuery(
                    "SELECT p FROM Propuesta p " +
                            "WHERE p.estudiante1.id = :idEstudiante " +
                            "OR p.estudiante2.id = :idEstudiante " +
                            "OR p.estudiante3.id = :idEstudiante",
                    Propuesta.class
            );


            return myQuery.setParameter("idEstudiante", idEstudiante).getResultList();

        } catch (NoResultException e) {
            return null; // Si no hay resultados, retornar null
        }
    }

    @Override
    public List<Propuesta> findPropuestasBy(Integer idEstudiante, String tipo) {

        EstadoValidacion estadoValidacion1 = EstadoValidacion.NO_REVISADO;
        EstadoValidacion estadoValidacion2 = EstadoValidacion.VALIDADO;
        EstadoAprobacion estadoAprobacion1 = EstadoAprobacion.EN_REVISON;
        EstadoAprobacion estadoAprobacion2 = EstadoAprobacion.APROBADO;
        try {
            TypedQuery<Propuesta> myQuery = this.entityManager.createQuery(
                    "SELECT p FROM Propuesta p " +
                            "WHERE (p.estudiante1.id = :idEstudiante " +
                            "OR p.estudiante2.id = :idEstudiante " +
                            "OR p.estudiante3.id = :idEstudiante) " +
                            "AND( p.estadoValidacion = :estadoValidacion1 OR p.estadoValidacion = :estadoValidacion2)" +
                            "AND ( p.estadoAprobacion = :estadoAprobacion1  OR p.estadoAprobacion =:estadoAprobacion2)" +
                            "AND p.tipo = :tipo ",

                    Propuesta.class
            );

            return myQuery
                    .setParameter("idEstudiante", idEstudiante)
                    .setParameter("tipo", tipo)
                    .setParameter("estadoValidacion1", estadoValidacion1)
                    .setParameter("estadoValidacion2", estadoValidacion2)
                    .setParameter("estadoAprobacion1", estadoAprobacion1)
                    .setParameter("estadoAprobacion2", estadoAprobacion2)
                    .getResultList();

        } catch (NoResultException e) {
            return Collections.emptyList(); // Retorna una lista vacía en lugar de null
        }
    }

    @Override
    public List<Propuesta> findPropuestasByCompleta(Integer idEstudiante, String tipo, String categoria) {

        EstadoValidacion estadoValidacion1 = EstadoValidacion.NO_REVISADO;
        EstadoValidacion estadoValidacion2 = EstadoValidacion.VALIDADO;
        EstadoAprobacion estadoAprobacion1 = EstadoAprobacion.EN_REVISON;
        EstadoAprobacion estadoAprobacion2 = EstadoAprobacion.APROBADO;
        try {
            TypedQuery<Propuesta> myQuery = this.entityManager.createQuery(
                    "SELECT p FROM Propuesta p " +
                            "WHERE (p.estudiante1.id = :idEstudiante " +
                            "OR p.estudiante2.id = :idEstudiante " +
                            "OR p.estudiante3.id = :idEstudiante) " +
                            "AND( p.estadoValidacion = :estadoValidacion1 OR p.estadoValidacion = :estadoValidacion2)" +
                            "AND ( p.estadoAprobacion = :estadoAprobacion1  OR p.estadoAprobacion =:estadoAprobacion2)" +
                            "AND p.categoria = :categoria " +
                            "AND p.tipo = :tipo ",

                    Propuesta.class
            );

            return myQuery
                    .setParameter("idEstudiante", idEstudiante)
                    .setParameter("tipo", tipo)
                    .setParameter("estadoValidacion1", estadoValidacion1)
                    .setParameter("estadoValidacion2", estadoValidacion2)
                    .setParameter("estadoAprobacion1", estadoAprobacion1)
                    .setParameter("estadoAprobacion2", estadoAprobacion2)
                    .setParameter("categoria", categoria)
                    .getResultList();

        } catch (NoResultException e) {
            return Collections.emptyList(); // Retorna una lista vacía en lugar de null
        }
    }

    @Override
    public Boolean update(Propuesta propuesta) {
        try {
            Propuesta existente = this.entityManager.find(Propuesta.class, propuesta.getId());

            if (existente != null) {

                this.entityManager.merge(propuesta); // Guarda los cambios

                logger.info("propuesta con ID {} actualizado correctamente.", propuesta.getId());
                return true;
            } else {
                logger.warn("No se encontró propuesta con ID {} para actualizar.", propuesta.getId());
                return false;
            }
        } catch (Exception e) {
            logger.error("Error al actualizar propuesta con ID {}: {}", propuesta.getId(), e.getMessage(), e);
            return false;
        }
    }

    //para Director de carrera y secreataria
    @Override
    public List<Propuesta> finall() {
        try {

            TypedQuery<Propuesta> myQuery = this.entityManager.createQuery(
                    "SELECT p FROM Propuesta p ",
                    Propuesta.class
            );


            return myQuery.getResultList();

        } catch (NoResultException e) {
            return null; // Si no hay resultados, retornar null
        }
    }



}
