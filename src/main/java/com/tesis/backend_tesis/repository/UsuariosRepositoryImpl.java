package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Usuarios;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class UsuariosRepositoryImpl implements IUsuariosRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Usuarios insertar(Usuarios usuarios) {
        this.entityManager.persist(usuarios);
        return usuarios;
    }

    @Override
    public boolean existeUsuarioConEmail(String email) {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(u) FROM Usuarios u WHERE u.correo = :email", Long.class);
        query.setParameter("email", email);
        try {
            Long count = query.getSingleResult();
            return count > 0;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Usuarios buscarPorEmail(String email) {
        Usuarios usua = null;
        try {
            TypedQuery<Usuarios> myQuery = this.entityManager.createQuery("SELECT u FROM Usuarios u WHERE u.correo=:email",
                    Usuarios.class);
            usua = myQuery.setParameter("email", email).getSingleResult();
            return  usua;
        } catch (NoResultException e) {

            return null;

        }
    }

    @Override
    public Usuarios buscarPorId(int id) {
        try {
            return this.entityManager.find(Usuarios.class, id);
        } catch (Exception e) {
            // Registra el error y devuelve null o lanza una excepción personalizada
            System.err.println("Error al buscar el usuario con ID: " + id);
            e.printStackTrace();
            return null; // O puedes lanzar una excepción personalizada
        }
    }

    @Override
    public boolean esCorreoValido(String correo) {
        return correo != null && correo.toLowerCase().endsWith("@uce.edu.ec");
    }

    @Override
    public Boolean activarUsuario(Usuarios usuarios) {
        try {
            Usuarios usuarioExistente = this.entityManager.find(Usuarios.class, usuarios.getId());

            if (usuarioExistente != null) {
                usuarioExistente.setActivo(true); // Actualiza el estado
                this.entityManager.merge(usuarioExistente); // Guarda los cambios
                return true;
            } else {
                return false; // No se encontró la propuesta con el ID dado
            }
        } catch (Exception e) {
            //logger.error("Error al activar usuario con ID " + usuarios.getId(), e);
            e.printStackTrace();
            return false; // En caso de error, se retorna false
        }
    }
}
