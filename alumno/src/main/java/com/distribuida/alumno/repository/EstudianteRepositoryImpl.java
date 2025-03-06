package com.distribuida.alumno.repository;

import com.distribuida.alumno.repository.modelo.Estudiante;
import com.distribuida.alumno.repository.modelo.Propuesta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Transactional
public class EstudianteRepositoryImpl implements IEstudianteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Estudiante insertar(Estudiante estudiantes) {
        this.entityManager.persist(estudiantes);
        return estudiantes;
    }

    @Override
    public Estudiante findByIdUsuario(Integer idUsuario) {
        Estudiante estu = null;
        try {
            System.out.println("ingresa en repository");
            TypedQuery<Estudiante> myQuery = this.entityManager.createQuery(
                    "SELECT e FROM Estudiante e WHERE e.idUsuario = :idUsuario",
                    Estudiante.class
            );
            estu = myQuery.setParameter("idUsuario", idUsuario).getSingleResult();

            System.out.println(estu);
            return  estu;
        } catch (NoResultException e) {

            return null;

        }
    }


    @Override
    public Estudiante findByCedulaUsuario(String cedula) {
        Estudiante estu = null;
        try {
            System.out.println("ingresa en repository");
            TypedQuery<Estudiante> myQuery = this.entityManager.createQuery(
                    "SELECT e FROM Estudiante e WHERE e.cedula = :cedula",
                    Estudiante.class
            );
            estu = myQuery.setParameter("cedula", cedula).getSingleResult();

            System.out.println(estu);
            return  estu;
        } catch (NoResultException e) {

            return null;

        }
    }




    @Override
    public Estudiante existeEstudiante(String correo) {
        return null;
    }

    @Override
    public Estudiante findById(Integer id) {
        try {
            return entityManager.find(Estudiante.class, id);
        } catch (Exception e) {
            System.err.println("Error al buscar el estudiante con ID: " + id);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Estudiante findByCedula(String cedula) {
        Estudiante estu = null;
        try {
            System.out.println("ingresa en repository");
            TypedQuery<Estudiante> myQuery = this.entityManager.createQuery(
                    "SELECT e FROM Estudiante e WHERE e.cedula = :cedula",
                    Estudiante.class
            );
            estu = myQuery.setParameter("cedula", cedula).getSingleResult();

            System.out.println(estu);
            return  estu;
        } catch (NoResultException e) {

            return null;

        }
    }

}
