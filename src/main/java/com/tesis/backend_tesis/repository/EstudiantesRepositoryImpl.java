package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Estudiantes;
import com.tesis.backend_tesis.repository.modelo.Usuarios;
import com.tesis.backend_tesis.service.dto.EstudianteDTO;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.NoResultException;

@Repository
@Transactional
public class EstudiantesRepositoryImpl implements IEstudiantesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Estudiantes insertar(Estudiantes estudiantes) {
        this.entityManager.persist(estudiantes);
        return estudiantes;
    }

    @Override
    public Estudiantes findByIdUsuario(Integer idUsuario) {

        Estudiantes estu = null;
        try {
            System.out.println("ingresa en repository");
            TypedQuery<Estudiantes> myQuery = this.entityManager.createQuery(
                    "SELECT e FROM Estudiantes e JOIN e.usuario u WHERE u.id = :idUsuario",
                    Estudiantes.class
            );
            estu = myQuery.setParameter("idUsuario", idUsuario).getSingleResult();

            System.out.println(estu);
            return  estu;
        } catch (NoResultException e) {

            return null;

        }
    }

    @Override
    public Estudiantes existeEstudiante(String correo) {

        try {
            System.out.println("Ingresa en repository método existencia Estudiante");

            TypedQuery<Usuarios> myQuery = this.entityManager.createQuery(
                    "SELECT u FROM Usuarios u WHERE u.correo = :correo",
                    Usuarios.class
            );

            Usuarios estu = myQuery.setParameter("correo", correo).getSingleResult();

            if ("estudiante".equals(estu.getRol())) {
                return findByIdUsuario(estu.getId());
            }

            System.out.println(estu); // Borrar después de depuración

        } catch (NoResultException e) {
            System.out.println("No se encontró un usuario con el correo: " + correo);
        }

        return null;

    }

    @Override
    public Estudiantes findById(Integer id) {
        try {
            return entityManager.find(Estudiantes.class, id);
        } catch (Exception e) {
            System.err.println("Error al buscar el estudiante con ID: " + id);
            e.printStackTrace();
            return null;
        }
    }


}
