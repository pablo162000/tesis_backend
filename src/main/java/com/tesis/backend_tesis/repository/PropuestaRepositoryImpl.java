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
            System.out.println("ingresa propuesta  en repository");
            TypedQuery<Propuesta> myQuery = this.entityManager.createQuery(
                    "SELECT p FROM Propuesta p WHERE p.id = :id",
                    Propuesta.class
            );
            prop = myQuery.setParameter("id", id).getSingleResult();

            System.out.println(prop);
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

    @Override
    public List<Propuesta> buscarPorApellidoEstudiante(String apellido) {
        try {

            TypedQuery<Propuesta> myQuery = this.entityManager.createQuery(
                    "SELECT p FROM Propuesta p " +
                            "WHERE p.estudiante1.usuario.primerApellido = :apellido " +
                            "OR p.estudiante3.usuario.primerApellido = :apellido " +
                            "OR p.estudiante3.usuario.primerApellido = :apellido",
                    Propuesta.class
            );


            return myQuery.setParameter("apellido", apellido).getResultList();

        } catch (NoResultException e) {
            return null; // Si no hay resultados, retornar null
        }
    }

    @Override
    public List<Propuesta> buscarPorPeriodo(String periodo) {
        try {

            TypedQuery<Propuesta> myQuery = this.entityManager.createQuery(
                    "SELECT p FROM Propuesta p WHERE p.periodo = :periodo",
                    Propuesta.class
            );

            return myQuery.setParameter("periodo", periodo).getResultList();

        } catch (NoResultException e) {
            return null; // Si no hay resultados, retornar null
        }
    }

    @Override
    public List<Propuesta> buscarPorEstado(String estado) {
        try {

            TypedQuery<Propuesta> myQuery = this.entityManager.createQuery(
                    "SELECT p FROM Propuesta p WHERE p.estadoValidacion = :periodo",
                    Propuesta.class
            );

            return myQuery.setParameter("periodo", estado).getResultList();

        } catch (NoResultException e) {
            return null; // Si no hay resultados, retornar null
        }
    }

    @Override
    public Propuesta actualizarEstado(Propuesta propuesta) {
        try {
            Propuesta propuestaExistente = this.entityManager.find(Propuesta.class, propuesta.getId());

            if (propuestaExistente != null) {
                propuestaExistente.setEstadoValidacion(propuesta.getEstadoValidacion()); // Actualiza el estado
                this.entityManager.merge(propuestaExistente); // Guarda los cambios
                return propuestaExistente;
            } else {
                return null; // No se encontró la propuesta con el ID dado
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null; // En caso de error, se retorna null
        }
    }


    /*

@Override
public List<Propuesta> buscarPropuestas(String apellido, String periodo, String estado) {
    try {
        // Construcción dinámica de la consulta
        StringBuilder jpql = new StringBuilder("SELECT p FROM Propuesta p WHERE 1=1");

        if (apellido != null && !apellido.trim().isEmpty()) {
            jpql.append(" AND (p.estudiantePrimero.primer_apellido = :apellido " +
                        "OR p.estudianteSegundo.primer_apellido = :apellido " +
                        "OR p.estudianteTercero.primer_apellido = :apellido)");
        }

        if (periodo != null && !periodo.trim().isEmpty()) {
            jpql.append(" AND p.periodo = :periodo");
        }

        if (estado != null && !estado.trim().isEmpty()) {
            jpql.append(" AND p.validacion = :estado");
        }

        TypedQuery<Propuesta> myQuery = this.entityManager.createQuery(jpql.toString(), Propuesta.class);

        // Asignación de parámetros dinámicos
        if (apellido != null && !apellido.trim().isEmpty()) {
            myQuery.setParameter("apellido", apellido);
        }
        if (periodo != null && !periodo.trim().isEmpty()) {
            myQuery.setParameter("periodo", periodo);
        }
        if (estado != null && !estado.trim().isEmpty()) {
            myQuery.setParameter("estado", estado);
        }

        return myQuery.getResultList();

    } catch (NoResultException e) {
        return new ArrayList<>(); // Retornar lista vacía si no hay resultados
    }
}

     */


}
