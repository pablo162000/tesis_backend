package com.distribuida.login.repository;

import com.distribuida.login.repository.modelo.Usuario;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
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
    public Usuario buscarPorId(Integer id) {
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
    public Usuario actualizar(Usuario usuario) {

        this.entityManager.merge(usuario);
        return usuario;
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

    @Override
    public List<Usuario> findEstudianteByEstado(Boolean activo, String rol) {
        try {
            TypedQuery<Usuario> myQuery = this.entityManager.createQuery(
                    "SELECT u FROM Usuario u WHERE u.rol = :rol AND u.activo = :activo",
                    Usuario.class
            );

            return myQuery.setParameter("activo", activo)
                    .setParameter("rol", rol)
                    .getResultList();

        } catch (NoResultException e) {
            return Collections.emptyList(); // Retornar una lista vacía en lugar de null
        }
    }

    @Override
    public Boolean activarDesactivarUsuario(Integer id, Boolean accion) {
        try {
            Usuario usuarioExistente = this.entityManager.find(Usuario.class, id);

            if (usuarioExistente != null) {
                usuarioExistente.setActivo(accion); // Actualiza el estado
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
    public Boolean deleteUsuario(Integer id) {
        try {
            // Crear la consulta de eliminación
            String query = "DELETE FROM Usuario u WHERE u.id =:id";
            int deletedCount = this.entityManager.createQuery(query)
                    .setParameter("id", id)
                    .executeUpdate();

            // Verificar si se eliminó algún registro
            if (deletedCount > 0) {
                System.out.println("usuario ELIMINADO POR FALTA DE VERIFICACION");
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE; // Manejo de excepciones, por ejemplo, si hay algún error en la consulta
        }
    }

    @Override
    public List<Usuario> findUsuariosNoVerificadosAntesDe() {
        try {
            // Aquí calculas la fecha límite (puedes sumarle minutos o cualquier otra unidad)

            // Consulta para obtener los usuarios que no han verificado su correo y cuyo registro es antes de la fecha límite calculada
            String query = "SELECT u FROM Usuario u WHERE u.correoValido = false";
            Query q = entityManager.createQuery(query);
             // Pasar la fecha límite calculada como parámetro

            return q.getResultList();  // Devuelve la lista de usuarios que cumplen la condición
        } catch (NoResultException e) {
            return Collections.emptyList();  // Si no se encuentran usuarios, retorna una lista vacía
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();  // Manejo de otros errores, retorna una lista vacía
        }
    }


}


