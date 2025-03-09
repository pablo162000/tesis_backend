package com.distribuida.administrativos.repository;

import com.distribuida.administrativos.repository.modelo.VistaDocente;

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
public class VistaDocenteRepositoryImpl implements IVistaDocenteRepository{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public VistaDocente findById(Integer id) {
        try {
            return entityManager.find(VistaDocente.class, id);
        } catch (Exception e) {
            // Registra el error y lanza una excepción personalizada
            System.err.println("Error al buscar el estudiante con ID: " + id);
            e.printStackTrace();
            throw new RuntimeException("Error al buscar el estudiante con ID: " + id, e);
        }
    }

    @Override
    public List<VistaDocente> findAll() {
        try {
            TypedQuery<VistaDocente> myQuery = this.entityManager.createQuery("SELECT v FROM VistaDocente v", VistaDocente.class);
            List<VistaDocente> resultList = myQuery.getResultList();
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
    public List<VistaDocente> findByEstado(Boolean activo) {
        try {
            TypedQuery<VistaDocente> myQuery = entityManager.createQuery(
                    "SELECT v FROM VistaDocente v WHERE v.usuaActivo = :activo", // Asegúrate de que 'usuaActivo' sea el nombre correcto de la propiedad
                    VistaDocente.class);
            myQuery.setParameter("activo", activo);

            List<VistaDocente> resultList = myQuery.getResultList();
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