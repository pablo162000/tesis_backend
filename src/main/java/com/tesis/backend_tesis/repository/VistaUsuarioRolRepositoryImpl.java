package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.VistaUsuarioRol;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Repository
@Transactional
public class VistaUsuarioRolRepositoryImpl implements IVistaUsuarioRolRepository{

    private static final Logger logger = LogManager.getLogger(VistaUsuarioRolRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public VistaUsuarioRol findByIdUsuarioRol(Integer idUsuarioRol) {
        try {
            VistaUsuarioRol vistaUsuarioRol = this.entityManager.find(VistaUsuarioRol.class, idUsuarioRol);

            if (vistaUsuarioRol != null) {
                logger.info("UsuarioRol encontrado en VistaUsuarioRol con IDUsuarioRol: {}", idUsuarioRol);
            } else {
                logger.warn("No se encontró un UsuarioRol en VistaUsuarioRol con IDUsuarioRol: {}", idUsuarioRol);
            }

            return vistaUsuarioRol;
        } catch (Exception e) {
            logger.error("Error al buscar UsuarioRol en VistaUsuarioRol con IDUsuarioRol {}: {}", idUsuarioRol, e.getMessage(), e);
            throw new RuntimeException("Error al buscar UsuarioRol en VistaUsuarioRol con IDUsuarioRol: " + idUsuarioRol, e);
        }
    }

    @Override
    public VistaUsuarioRol findByCorreo(String correo) {

        try {
            TypedQuery<VistaUsuarioRol> query = this.entityManager.createQuery(
                    "SELECT u FROM VistaUsuarioRol u WHERE u.correoUsuario = :correo",
                    VistaUsuarioRol.class
            );
            query.setParameter("correo", correo);

            VistaUsuarioRol vistaUsuarioRol = query.getSingleResult();
            logger.info("UsuarioRol encontrado en VistaUsuarioRol con correo: {}", correo);
            return vistaUsuarioRol;
        } catch (NoResultException e) {
            logger.warn("No se encontró un UsuarioRol en VistaUsuarioRol con correo: {}", correo);
            return null;
        } catch (Exception e) {
            logger.error("Error al buscar UsuarioRol en VistaUsuarioRol con correo {}: {}", correo, e.getMessage(), e);
            throw new RuntimeException("Error al buscar UsuarioRol en VistaUsuarioRol con correo: " + correo, e);
        }
    }

    @Override
    public List<VistaUsuarioRol> findByIdRol(Integer idRol) {
        try {
            TypedQuery<VistaUsuarioRol> query = this.entityManager.createQuery(
                    "SELECT u FROM VistaUsuarioRol u WHERE u.idRol = :idRol",
                    VistaUsuarioRol.class
            );
            query.setParameter("idRol", idRol);

            List<VistaUsuarioRol> vistaUsuarioRols = query.getResultList();

            if (vistaUsuarioRols.isEmpty()) {
                logger.debug("No se encontraron UsuarioRol en VistaUsuarioRol con IDROL {}.", idRol);
            } else {
                logger.debug("Se encontraron {} UsuarioRol en VistaUsuarioRol con IDROL {}.", vistaUsuarioRols.size(), idRol);
            }

            return vistaUsuarioRols;
        } catch (Exception e) {
            logger.error("Error al buscarVistaUsuarioRol con IDROL: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaUsuarioRol> findByNombreRol(String nombreRol) {
        try {
            TypedQuery<VistaUsuarioRol> query = this.entityManager.createQuery(
                    "SELECT u FROM VistaUsuarioRol u WHERE u.nombreRol = :nombreRol",
                    VistaUsuarioRol.class
            );
            query.setParameter("nombreRol", nombreRol);

            List<VistaUsuarioRol> vistaUsuarioRols = query.getResultList();

            if (vistaUsuarioRols.isEmpty()) {
                logger.debug("No se encontraron UsuarioRol en VistaUsuarioRol con nombreRol {}.", nombreRol);
            } else {
                logger.debug("Se encontraron {} UsuarioRol en VistaUsuarioRol con nombreRol {}.", vistaUsuarioRols.size(), nombreRol);
            }

            return vistaUsuarioRols;
        } catch (Exception e) {
            logger.error("Error al buscarVistaUsuarioRol con nombreRol: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaUsuarioRol> findByIdUsuario(Integer idUsuario) {

        try {
            TypedQuery<VistaUsuarioRol> query = this.entityManager.createQuery(
                    "SELECT u FROM VistaUsuarioRol u WHERE u.idUsuario = :idUsuario",
                    VistaUsuarioRol.class
            );
            query.setParameter("idUsuario", idUsuario);

            List<VistaUsuarioRol> vistaUsuarioRols = query.getResultList();

            if (vistaUsuarioRols.isEmpty()) {
                logger.debug("No se encontraron UsuarioRol en VistaUsuarioRol con IDUSUARIO {}.", idUsuario);
            } else {
                logger.debug("Se encontraron {} UsuarioRol en VistaUsuarioRol con IDUSUARIO {}.", vistaUsuarioRols.size(), idUsuario);
            }

            return vistaUsuarioRols;
        } catch (Exception e) {
            logger.error("Error al buscarVistaUsuarioRol con APELLIDOS: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaUsuarioRol> findByApellidos(String apellidos) {
        try {
            TypedQuery<VistaUsuarioRol> query = this.entityManager.createQuery(
                    "SELECT u FROM VistaUsuarioRol u WHERE u.apellidos = :apellidos",
                    VistaUsuarioRol.class
            );
            query.setParameter("apellidos", apellidos);

            List<VistaUsuarioRol> vistaUsuarioRols = query.getResultList();

            if (vistaUsuarioRols.isEmpty()) {
                logger.debug("No se encontraron UsuarioRol en VistaUsuarioRol con APELLIDOS {}.", apellidos);
            } else {
                logger.debug("Se encontraron {} UsuarioRol en VistaUsuarioRol con APELLIDOS {}.", vistaUsuarioRols.size(), apellidos);
            }

            return vistaUsuarioRols;
        } catch (Exception e) {
            logger.error("Error al buscarVistaUsuarioRol con APELLIDOS: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaUsuarioRol> findByNombres(String nombres) {
        try {
            TypedQuery<VistaUsuarioRol> query = this.entityManager.createQuery(
                    "SELECT u FROM VistaUsuarioRol u WHERE u.nombres = :nombres",
                    VistaUsuarioRol.class
            );
            query.setParameter("nombres", nombres);

            List<VistaUsuarioRol> vistaUsuarioRols = query.getResultList();

            if (vistaUsuarioRols.isEmpty()) {
                logger.debug("No se encontraron UsuarioRol en VistaUsuarioRol con NOMBRES {}.", nombres);
            } else {
                logger.debug("Se encontraron {} UsuarioRol en VistaUsuarioRol con NOMBRES {}.", vistaUsuarioRols.size(), nombres);
            }

            return vistaUsuarioRols;
        } catch (Exception e) {
            logger.error("Error al buscar VistaUsuarioRol con NOMBRES: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaUsuarioRol> findAll() {
        try {
            TypedQuery<VistaUsuarioRol> query = this.entityManager.createQuery(
                    "SELECT u FROM VistaUsuarioRol u",
                    VistaUsuarioRol.class
            );

            List<VistaUsuarioRol> vistaUsuarioRols = query.getResultList();
            if (vistaUsuarioRols.isEmpty()) {
                logger.warn("No se encontraron usuarios con roles  en VistaUsuarioRol.");
            } else {
                logger.info("Se encontraron {} usuarios con roles en VistaUsuarioRol .", vistaUsuarioRols.size());
            }
            return vistaUsuarioRols;
        } catch (NoResultException e) {
            logger.warn("No se encontraron usuarios con roles en VistaUsuarioRol.");
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error al buscar usuarios con roles en VistaUsuarioRol: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaUsuarioRol> findByNombreRolAndEstado(String nombreRol, Boolean estado) {
        try {
            TypedQuery<VistaUsuarioRol> query = this.entityManager.createQuery(
                    "SELECT u FROM VistaUsuarioRol u WHERE u.nombreRol = :nombreRol AND u.activo =: estado",
                    VistaUsuarioRol.class
            );
            query.setParameter("nombreRol", nombreRol);
            query.setParameter("estado", estado);

            List<VistaUsuarioRol> vistaUsuarioRols = query.getResultList();

            if (vistaUsuarioRols.isEmpty()) {
                logger.debug("No se encontraron UsuarioRol en VistaUsuarioRol con nombreRol {} y estado {}.", nombreRol, estado);
            } else {
                logger.debug("Se encontraron {} UsuarioRol en VistaUsuarioRol con nombreRol {} y estado{}.", vistaUsuarioRols.size(), nombreRol, estado);
            }

            return vistaUsuarioRols;
        } catch (Exception e) {
            logger.error("Error al buscarVistaUsuarioRol con nombreRol y estado: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
