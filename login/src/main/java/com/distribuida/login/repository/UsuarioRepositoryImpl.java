package com.distribuida.login.repository;

import com.distribuida.login.repository.modelo.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class UsuarioRepositoryImpl implements IUsuarioRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Usuario insertar(Usuario usuarios) {
        this.entityManager.persist(usuarios);
        return usuarios;
    }

    @Override
    public boolean existeUsuarioConEmail(String email) {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.correo = :email", Long.class);
        query.setParameter("email", email);
        try {
            Long count = query.getSingleResult();
            return count > 0;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        Usuario usua = null;
        try {
            TypedQuery<Usuario> myQuery = this.entityManager.createQuery("SELECT u FROM Usuario u WHERE u.correo=:email",
                    Usuario.class);
            usua = myQuery.setParameter("email", email).getSingleResult();
            return  usua;
        } catch (NoResultException e) {

            return null;

        }
    }

    @Override
    public Usuario buscarPorId(int id) {
        try {
            return this.entityManager.find(Usuario.class, id);
        } catch (Exception e) {
            // Registra el error y devuelve null o lanza una excepción personalizada
            System.err.println("Error al buscar el usuario con ID: " + id);
            e.printStackTrace();
            return null; // O puedes lanzar una excepción personalizada
        }
    }


    @Override
    public Boolean activarUsuario(Integer id) {
        try {
            Usuario usuarioExistente = this.entityManager.find(Usuario.class, id);

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

    @Override
    public List<Usuario> findAllWithRol(String rol) {
        try {

            TypedQuery<Usuario> myQuery = this.entityManager.createQuery(
                    "SELECT u FROM Usuario u WHERE u.rol=: rol",
                    Usuario.class
            );


            return myQuery.setParameter("rol", rol).getResultList();

        } catch (NoResultException e) {
            return null; // Si no hay resultados, retornar null
        }
    }
}


