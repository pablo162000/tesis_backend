package com.distribuida.administrativos.repository;

import com.distribuida.administrativos.repository.modelo.VistaEstudiante;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@Transactional
public class VistaEstudianteRepositoryImpl implements IVistaEstudianteRepository{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public VistaEstudiante findById(Integer id) {
        try {
            return entityManager.find(VistaEstudiante.class, id);
        } catch (Exception e) {
            // Registra el error y lanza una excepción personalizada
            System.err.println("Error al buscar el estudiante con ID: " + id);
            e.printStackTrace();
            throw new RuntimeException("Error al buscar el estudiante con ID: " + id, e);
        }
    }

    @Override
    public List<VistaEstudiante> findAll() {
        try {
            TypedQuery<VistaEstudiante> myQuery = this.entityManager.createQuery("SELECT v FROM VistaEstudiante v", VistaEstudiante.class);
            List<VistaEstudiante> resultList = myQuery.getResultList();
            return resultList.isEmpty() ? Collections.emptyList() : resultList; // Retorna lista vacía si no hay resultados
        } catch (NoResultException e) {
            return Collections.emptyList(); // Devuelve lista vacía en caso de no haber resultados
        } catch (Exception e) {
            System.err.println("Error al buscar todos los estudiantes");
            e.printStackTrace();
            throw new RuntimeException("Error al buscar todos los estudiantes", e);
        }
    }

    @Override
    public List<VistaEstudiante> findByEstado(Boolean activo) {
        try {
            TypedQuery<VistaEstudiante> myQuery = entityManager.createQuery(
                    "SELECT v FROM VistaEstudiante v WHERE v.usuaActivo = :activo", // Asegúrate de que 'usuaActivo' sea el nombre correcto de la propiedad
                    VistaEstudiante.class);
            myQuery.setParameter("activo", activo);

            List<VistaEstudiante> resultList = myQuery.getResultList();
            return resultList.isEmpty() ? Collections.emptyList() : resultList; // Retorna lista vacía si no hay resultados
        } catch (NoResultException e) {
            return Collections.emptyList(); // Devuelve lista vacía si no hay resultados
        } catch (Exception e) {
            System.err.println("Error al buscar estudiantes por estado");
            e.printStackTrace();
            throw new RuntimeException("Error al buscar estudiantes por estado", e);
        }
    }
}