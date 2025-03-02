package com.distribuida.alumno.repository;

import com.distribuida.alumno.repository.modelo.EstadoValidacion;
import com.distribuida.alumno.repository.modelo.Propuesta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
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
                            "WHERE p.primerEstudiante.id = :idEstudiante " +
                            "OR p.segundoEstudiante.id = :idEstudiante " +
                            "OR p.tercerEstudiante.id = :idEstudiante",
                    Propuesta.class
            );


            return myQuery.setParameter("idEstudiante", idEstudiante).getResultList();

        } catch (NoResultException e) {
            return null; // Si no hay resultados, retornar null
        }
    }

    @Override
    public Boolean update(Propuesta propuesta) {
        try {
            // Intentamos encontrar la propuesta en la base de datos usando el id de la propuesta
            Propuesta propuestaExistente = this.entityManager.find(Propuesta.class, propuesta.getId());

            if (propuestaExistente != null) {
                // Si la propuesta existe, solo guardamos los cambios sin necesidad de modificar el estado de validación nuevamente
                this.entityManager.merge(propuestaExistente); // Guarda los cambios en la base de datos

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
                            "WHERE p.primerEstudiante.primerApellido = :apellido " +
                            "OR p.segundoEstudiante.primerApellido = :apellido " +
                            "OR p.tercerEstudiante.primerApellido = :apellido",
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
    public Propuesta actualizarEstadoValidacion(Integer idPropuesta, Integer nuevoEstadoValidacion) {
        try {
            // Verificar que el valor es válido antes de persistirlo
            EstadoValidacion estado = EstadoValidacion.fromInt(nuevoEstadoValidacion);

            // Buscar la propuesta por el ID
            Propuesta propuestaExistente = this.entityManager.find(Propuesta.class, idPropuesta);

            if (propuestaExistente != null) {
                // Actualizar el estado de validación
                propuestaExistente.setValidacion(estado);

                // Guardar los cambios
                this.entityManager.merge(propuestaExistente);

                return propuestaExistente;
            } else {
                return null; // No se encontró la propuesta con el ID dado
            }
        } catch (IllegalArgumentException e) {
            // Si el valor de validacion es incorrecto
            e.printStackTrace();
            return null; // En caso de error, se retorna null
        }

    }
}
