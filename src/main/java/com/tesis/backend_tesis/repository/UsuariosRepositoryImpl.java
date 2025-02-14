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
}
