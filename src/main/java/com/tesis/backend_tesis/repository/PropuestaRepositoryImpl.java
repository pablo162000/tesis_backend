package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Propuesta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class PropuestaRepositoryImpl implements IPropuestaRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Propuesta crear(Propuesta propuesta) {

        this.entityManager.persist(propuesta);
        return propuesta;
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
                            "WHERE p.estudiantePrimero.id = :idEstudiante " +
                            "OR p.estudianteSegundo.id = :idEstudiante " +
                            "OR p.estudianteTercero.id = :idEstudiante",
                    Propuesta.class
            );


            return myQuery.setParameter("idEstudiante", idEstudiante).getResultList();

        } catch (NoResultException e) {
            return null; // Si no hay resultados, retornar null
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
                            "WHERE p.estudiantePrimero.primer_apellido = :apellido " +
                            "OR p.estudianteSegundo.primer_apellido = :apellido " +
                            "OR p.estudianteTercero.primer_apellido = :apellido",
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
                    "SELECT p FROM Propuesta p WHERE p.validacion = :periodo",
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
                propuestaExistente.setValidacion(propuesta.getValidacion()); // Actualiza el estado
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
