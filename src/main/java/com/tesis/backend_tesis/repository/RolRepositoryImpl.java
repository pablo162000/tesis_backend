package com.tesis.backend_tesis.repository;


import com.tesis.backend_tesis.repository.modelo.Rol;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class RolRepositoryImpl implements IRolRepository{

    private static final Logger logger = LogManager.getLogger(RolRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Rol findById(Integer id) {
        try {
            Rol rol = this.entityManager.find(Rol.class, id);

            if (rol != null) {
                logger.info("Rol encontrado con ID: {}", id);
            } else {
                logger.warn("No se encontró un Rol con ID: {}", id);
            }
            return rol;
        } catch (Exception e) {
            logger.error("Error al buscar el Rol con ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al buscar el Rol con ID: " + id, e);
        }
    }

    @Override
    public Rol findByNombre(String nombre) {
        try {

            TypedQuery<Rol> myQuery = this.entityManager.createQuery("SELECT r FROM Rol r WHERE r.nombre=:nombre",
                    Rol.class);
            myQuery.setParameter("nombre", nombre);
            Rol rol = myQuery.getSingleResult();
            logger.info("Rol encontrado con nombre: {}", nombre);
            return rol;
        } catch (NoResultException e) {
            logger.warn("No se encontró un Rol con nombre: {}", nombre);
            return null;
        } catch (Exception e) {
            logger.error("Error al buscar Rol con nombre {}: {}", nombre, e.getMessage(), e);
            throw new RuntimeException("Error al buscar Rol con nombre: " + nombre, e);
        }
    }

    @Override
    public List<Rol> findAll() {
        return List.of();
    }
}
