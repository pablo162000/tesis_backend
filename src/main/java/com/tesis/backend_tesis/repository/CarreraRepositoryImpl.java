package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Carrera;
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
public class CarreraRepositoryImpl implements ICarreraRepository{

    private static final Logger logger = LogManager.getLogger(CarreraRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Carrera insert(Carrera carrera) {
        try {
            if (carrera == null) {
                throw new IllegalArgumentException("El usuario no puede ser null");
            }
            this.entityManager.persist(carrera);
            return carrera;
        } catch (Exception e) {
            logger.error("Error al insertar el usuario: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Boolean existeCarreraPorNombre(String nombre) {
        try {

            TypedQuery<Long> query = this.entityManager.createQuery("SELECT COUNT(c) FROM Carrera c WHERE c.nombre = :nombre", Long.class);
            query.setParameter("nombre", nombre);
            Long existe = query.getSingleResult();
            if (existe  == 0) {
                logger.warn("No existe un carrera con nombre: {}", nombre);
                return false;
            } else {
                logger.info("Existe carrera con nombre: {}", nombre);
                return true;
            }
        } catch (Exception e) {
            logger.error("Error al buscar carrera con nombre{}: {}", nombre, e.getMessage(), e);
            throw new RuntimeException("Error al buscar carrera con nombre: " + nombre, e);
        }

    }

    @Override
    public Carrera findById(Integer id) {
        try {
            Carrera carrera = this.entityManager.find(Carrera.class, id);

            if (carrera != null) {
                logger.info("Carrera encontrado con ID: {}", id);
            } else {
                logger.warn("No se encontró una carrera con ID: {}", id);
            }
            return carrera;
        } catch (Exception e) {
            logger.error("Error al buscar la carrera con ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al buscar la carrera con ID: " + id, e);
        }
    }

    @Override
    public Carrera update(Carrera carrera) {
        try {
            Carrera existente = this.entityManager.find(Carrera.class, carrera.getId());

            if (existente != null) {
                Carrera actualizada = this.entityManager.merge(carrera);
                logger.info("Carrera con ID {} actualizada correctamente.", carrera.getId());
                return actualizada;
            } else {
                logger.warn("No se encontró la carrera con ID {} para actualizar.", carrera.getId());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error al actualizar la carrera con ID {}: {}", carrera.getId(), e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void actualizar(Integer idUsuario) {
        try {
            String query = "UPDATE Carrera c SET c.usuario = :nuevoValor WHERE c.usuario = :idUsuario";
            this.entityManager.createQuery(query)
                    .setParameter("nuevoValor", null)
                    .setParameter("idUsuario", idUsuario)
                    .executeUpdate();


        } catch (Exception e) {
            logger.error("Error al actualizar carrera con ID {}: {}", idUsuario, e.getMessage(), e);

        }
    }

    @Override
    public Carrera findByIdDireccion(Integer idDireccion) {
        try {
            TypedQuery<Carrera> query = this.entityManager.createQuery(
                    "SELECT c FROM Carrera c WHERE c.usuario.id = :idDireccion",
                    Carrera.class
            );
            query.setParameter("idDireccion", idDireccion);

            Carrera carrera = query.getSingleResult();
            logger.info("Carrera encontrado con ID de usuario: {}", idDireccion);
            return carrera;
        } catch (NoResultException e) {
            logger.warn("No se encontró Carrera con ID de usuario: {}", idDireccion);
            return null;
        } catch (Exception e) {
            logger.error("Error al buscar Carrera con ID Usuario {}: {}", idDireccion, e.getMessage(), e);
            throw new RuntimeException("Error al buscar Carrera con ID usuario: " + idDireccion, e);
        }
    }


    @Override
    public List<Carrera> findByFacultad(Integer idFacultad) {
        try {
            TypedQuery<Carrera> myQuery = this.entityManager.createQuery(
                    "SELECT c FROM Carrera c WHERE c.facultad.id = :idFacultad",
                    Carrera.class
            );
            myQuery.setParameter("idFacultad", idFacultad);

            List<Carrera> carreras = myQuery.getResultList();

            if (carreras.isEmpty()) {
                logger.warn("No se encontraron carreras con ID de facultad: {}", idFacultad);
            } else {
                logger.info("Se encontraron {} carreras con ID de facultad: {}", carreras.size(), idFacultad);
            }

            return carreras;
        } catch (Exception e) {
            logger.error("Error al buscar carreras con ID de facultad {}: {}", idFacultad, e.getMessage(), e);
            throw new RuntimeException("Error al buscar carreras con ID de facultad: " + idFacultad, e);
        }
    }

    @Override
    public List<Carrera> findAll() {
        return List.of();
    }
}
