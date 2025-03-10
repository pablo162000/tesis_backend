package com.distribuida.alumno.repository;


import com.distribuida.alumno.repository.modelo.EstadoValidacion;
import com.distribuida.alumno.repository.modelo.VistaPropuesta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Repository
@Transactional
public class VistaPropuestaRepositoryImpl  implements IVistaPropuestaRepository{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public VistaPropuesta findById(Integer id) {
        try {
            return entityManager.find(VistaPropuesta.class, id);
        } catch (Exception e) {
            // Registra el error y lanza una excepción personalizada
            System.err.println("Error al buscar la propuesta con ID: " + id);
            e.printStackTrace();
            throw new RuntimeException("Error al buscar  la propuesta con ID: " + id, e);
        }
    }

    @Override
    public List<VistaPropuesta> findAll() {
        try {
            TypedQuery<VistaPropuesta> myQuery = this.entityManager.createQuery("SELECT v FROM VistaPropuesta v", VistaPropuesta.class);
            List<VistaPropuesta> resultList = myQuery.getResultList();
            return resultList.isEmpty() ? Collections.emptyList() : resultList; // Retorna lista vacía si no hay resultados
        } catch (NoResultException e) {
            return Collections.emptyList(); // Devuelve lista vacía en caso de no haber resultados
        } catch (Exception e) {
            System.err.println("Error al buscar todos las propuestas");
            e.printStackTrace();
            throw new RuntimeException("Error al buscar todas las propuestas", e);
        }
    }

    @Override
    public List<VistaPropuesta> findByEstadoValidacion(EstadoValidacion estadoValidacion) {
        try {
            TypedQuery<VistaPropuesta> myQuery = entityManager.createQuery(
                    "SELECT v FROM VistaPropuesta v WHERE v.validacion = :estadoValidacion", // Asegúrate de que 'usuaActivo' sea el nombre correcto de la propiedad
                    VistaPropuesta.class);
            myQuery.setParameter("estadoValidacion", estadoValidacion);

            List<VistaPropuesta> resultList = myQuery.getResultList();
            return resultList.isEmpty() ? Collections.emptyList() : resultList; // Retorna lista vacía si no hay resultados
        } catch (NoResultException e) {
            return Collections.emptyList(); // Devuelve lista vacía si no hay resultados
        } catch (Exception e) {
            System.err.println("Error al buscar propuestas por validacion");
            e.printStackTrace();
            throw new RuntimeException("Error al buscar propuestas por validacion", e);
        }
    }

    @Override
    public List<VistaPropuesta> findByEstadoAprobacion(Boolean activo) {
        try {
            TypedQuery<VistaPropuesta> myQuery = entityManager.createQuery(
                    "SELECT v FROM VistaPropuesta v WHERE v.estadoAprobacion = :activo", // Asegúrate de que 'usuaActivo' sea el nombre correcto de la propiedad
                    VistaPropuesta.class);
            myQuery.setParameter("activo", activo);

            List<VistaPropuesta> resultList = myQuery.getResultList();
            return resultList.isEmpty() ? Collections.emptyList() : resultList; // Retorna lista vacía si no hay resultados
        } catch (NoResultException e) {
            return Collections.emptyList(); // Devuelve lista vacía si no hay resultados
        } catch (Exception e) {
            System.err.println("Error al buscar propuestas por aprobación");
            e.printStackTrace();
            throw new RuntimeException("Error al buscar propuestas por aprobación", e);
        }
    }
}
