package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Usuario;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@Transactional
public class UsuarioRepositoryImpl implements IUsuarioRepository{

    private static final Logger logger = LogManager.getLogger(UsuarioRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Usuario insert(Usuario usuario) {
        try {
            if (usuario == null) {
                throw new IllegalArgumentException("El usuario no puede ser null");
            }
            this.entityManager.persist(usuario);
            return usuario;
        } catch (Exception e) {
            logger.error("Error al insertar el usuario: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Boolean existeUsuarioConEmail(String email) {

        try {

            TypedQuery<Long> query = this.entityManager.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.correo = :email", Long.class);
            query.setParameter("email", email);
            Long existe = query.getSingleResult();
            if (existe  == 0) {
                logger.warn("No se existe un usuario con correo: {}", email);
                return false;
            } else {
                logger.info("Existe usuario con email: {}", email);
                return true;
            }
        } catch (Exception e) {
            logger.error("Error al buscar el usuario con correo{}: {}", email, e.getMessage(), e);
            throw new RuntimeException("Error al buscar el usuario con correo: " + email, e);
        }


    }

    @Override
    public Boolean existeUsuarioConCedula(String cedula) {
        try {

            TypedQuery<Long> query = this.entityManager.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.cedula = :cedula", Long.class);
            query.setParameter("cedula", cedula);
            Long existe = query.getSingleResult();
            if (existe  == 0) {
                logger.warn("No existe un usuario con cedula: {}", cedula);
                return false;
            } else {
                logger.info("Existe usuario con cedula: {}", cedula);
                return true;
            }
        } catch (Exception e) {
            logger.error("Error al buscar el usuario con cedula{}: {}", cedula, e.getMessage(), e);
            throw new RuntimeException("Error al buscar el usuario con cedula: " + cedula, e);
        }
    }

    @Override
    public Usuario update(Usuario usuario) {
        this.entityManager.merge(usuario);
        return usuario;
    }

    @Override
    public Usuario buscarPorEmail(String email) {

        try {

            TypedQuery<Usuario> myQuery = this.entityManager.createQuery("SELECT u FROM Usuario u WHERE u.correo=:email",
                    Usuario.class);
            myQuery.setParameter("email", email).getSingleResult();
            Usuario usuario = myQuery.getSingleResult();
            logger.info("usuario encontrado con email de usuario: {}", email);
            return usuario;
        } catch (NoResultException e) {
            logger.warn("No se encontró un usuario con correo: {}", email);
            return null;
        } catch (Exception e) {
            logger.error("Error al buscar usuario con correo {}: {}", email, e.getMessage(), e);
            throw new RuntimeException("Error al buscar al buscar usuario con email: " + email, e);
        }

    }

    @Override
    public Usuario findById(Integer id) {

        try {
            Usuario usuario = this.entityManager.find(Usuario.class, id);

            if (usuario != null) {
                logger.info("Usuario encontrado con ID: {}", id);
            } else {
                logger.warn("No se encontró un usuario con ID: {}", id);
            }
            return usuario;
        } catch (Exception e) {
            logger.error("Error al buscar el usaurio con ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al buscar el usuario con ID: " + id, e);
        }
    }

    @Override
    public Boolean activarUsuario(Integer id) {
        try {
            Usuario usuarioExistente = this.entityManager.find(Usuario.class, id);

            if (usuarioExistente != null) {
                usuarioExistente.setActivo(true);
                this.entityManager.merge(usuarioExistente);
                logger.info("Usuario activado con éxito. ID: {}", id);
                return true;
            } else {
                logger.warn("No se encontró el usuario con ID: {}", id);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error al activar usuario con ID {}: {}", id, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Usuario actualizar(Usuario usuario) {
        this.entityManager.merge(usuario);
        return usuario;
    }

    @Override
    public List<Usuario> findAllWithRol(String rol) {
        return List.of();
    }

    @Override
    public List<Usuario> findEstudianteByEstado(Boolean activo, String rol) {
        return List.of();
    }

    @Override
    public Boolean activarDesactivarUsuario(Integer id, Boolean accion) {
        try {
            Usuario usuarioExistente = this.entityManager.find(Usuario.class, id);

            if (usuarioExistente != null) {
                usuarioExistente.setActivo(accion);
                this.entityManager.merge(usuarioExistente);
                logger.info("Usuario con ID {} ha sido {}.", id, accion ? "activado" : "desactivado");
                return true;
            } else {
                logger.warn("No se encontró el usuario con ID: {}", id);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error al actualizar estado del usuario con ID {}: {}", id, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean deleteUsuario(Integer id) {
        try {
            String query = "DELETE FROM Usuario u WHERE u.id = :id";
            int deletedCount = this.entityManager.createQuery(query)
                    .setParameter("id", id)
                    .executeUpdate();

            if (deletedCount > 0) {
                logger.info("Usuario con ID {} eliminado por falta de verificación.", id);
                return true;
            } else {
                logger.warn("No se encontró el usuario con ID {} para eliminar.", id);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error al eliminar usuario con ID {}: {}", id, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<Usuario> findUsuariosNoVerificadosAntesDe() {
        try {
            String query = "SELECT u FROM Usuario u WHERE u.correoValido = false";
            TypedQuery<Usuario> q = this.entityManager.createQuery(query, Usuario.class);

            List<Usuario> usuariosNoVerificados = q.getResultList();

            if (usuariosNoVerificados.isEmpty()) {
                logger.warn("No se encontraron usuarios no verificados.");
            } else {
                logger.info("Se encontraron {} usuarios no verificados.", usuariosNoVerificados.size());
            }

            return usuariosNoVerificados;
        } catch (NoResultException e) {
            logger.warn("No se encontraron usuarios no verificados.");
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error al buscar usuarios no verificados: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
