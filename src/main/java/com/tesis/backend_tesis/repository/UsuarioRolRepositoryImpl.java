package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Estudiante;
import com.tesis.backend_tesis.repository.modelo.UsuarioRol;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@Transactional
public class UsuarioRolRepositoryImpl implements IUsuarioRolRepository{

    private static final Logger logger = LogManager.getLogger(UsuarioRolRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UsuarioRol insert(UsuarioRol usuarioRol) {
        try {
            if (usuarioRol == null) {
                throw new IllegalArgumentException("la relacion USUARIOROL no puede ser null");
            }
            this.entityManager.persist(usuarioRol);
            return usuarioRol;
        } catch (Exception e) {
            logger.error("Error al insertar la relacion USUARIOROL: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public UsuarioRol findById(Integer id) {
        try {
            UsuarioRol usuarioRol = this.entityManager.find(UsuarioRol.class, id);

            if (usuarioRol != null) {
                logger.info("relacion USUARIOROL encontrado con ID: {}", id);
            } else {
                logger.warn("No se encontró la relacion USUARIOROL con ID: {}", id);
            }
            return usuarioRol;
        } catch (Exception e) {
            logger.error("Error al buscar la relacion USUARIOROL con ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al buscar la relacion USUARIOROL con ID: " + id, e);
        }
    }

    @Override
    public List<UsuarioRol> findByIdUsuario(Integer idUsuario) {
        try {
            TypedQuery<UsuarioRol> query = this.entityManager.createQuery(
                    "SELECT u FROM UsuarioRol u WHERE u.usuario.id = :idUsuario",
                    UsuarioRol.class
            );
            query.setParameter("idUsuario", idUsuario);

            List<UsuarioRol> usuarioRoles = query.getResultList();
            if (usuarioRoles.isEmpty()) {
                logger.warn("No se encontraron roles para el usuario.");
            } else {
                logger.info("Se encontraron {} usuarios roles.", usuarioRoles.size());
            }
            return usuarioRoles;
        } catch (NoResultException e) {
            logger.warn("No se encontraron roles para ese usuario.");
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error al buscar roles para ese usuario: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public Boolean deleteUsuarioRolbyIdUsuario(Integer idUsuario) {
        try {
            String query = "DELETE FROM UsuarioRol u WHERE u.usuario.id = :idUsuario";
            int deletedCount = this.entityManager.createQuery(query)
                    .setParameter("idUsuario", idUsuario)
                    .executeUpdate();

            if (deletedCount > 0) {
                logger.info("UsuarioRol con IDUSUARIO {} eliminado por falta de verificación.", idUsuario);
                return true;
            } else {
                logger.warn("No se encontró el UsuarioRol con IDUSUARIO {} para eliminar.", idUsuario);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error al eliminar UsuarioRol con IDUSUARIO {}: {}", idUsuario, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean deleteUsuarioRolbyIdUsuarioAndRol(Integer idUsuario, String rol) {
        try {
            String query = "DELETE FROM UsuarioRol u WHERE u.usuario.id = :idUsuario AND u.rol.nombre = :rol";
            int deletedCount = this.entityManager.createQuery(query)
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("rol", rol)
                    .executeUpdate();

            if (deletedCount > 0) {
                logger.info("UsuarioRol con IDUSUARIO {} eliminado por falta de verificación.", idUsuario);
                return true;
            } else {
                logger.warn("No se encontró el UsuarioRol con IDUSUARIO {} para eliminar.", idUsuario);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error al eliminar UsuarioRol con IDUSUARIO {}: {}", idUsuario, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<UsuarioRol> findAll() {
        return List.of();
    }
}
