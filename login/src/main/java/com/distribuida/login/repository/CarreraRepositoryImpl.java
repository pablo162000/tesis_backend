package com.distribuida.login.repository;

import com.distribuida.login.repository.modelo.Carrera;
import com.distribuida.login.repository.modelo.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class CarreraRepositoryImpl implements ICarreraRepository{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Carrera insertar(Carrera carrera) {
        this.entityManager.persist(carrera);
        return carrera;
    }

    @Override
    public Carrera buscarPorId(int id) {
        try {
            return this.entityManager.find(Carrera.class, id);
        } catch (Exception e) {
            // Registra el error y devuelve null o lanza una excepción personalizada
            System.err.println("Error al buscar el carrera con ID: " + id);
            e.printStackTrace();
            return null; // O puedes lanzar una excepción personalizada
        }
    }
}
