package com.tesis.backend_tesis.repository;


import com.tesis.backend_tesis.repository.modelo.Estudiante;
import com.tesis.backend_tesis.repository.modelo.Secretaria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class SecretariaRepositoryImpl implements ISecretariaRepository {

    private static final Logger logger = LogManager.getLogger(SecretariaRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Secretaria insert(Secretaria secretaria) {
        try {
            if (secretaria == null) {
                throw new IllegalArgumentException("secretaria no puede ser null");
            }
            this.entityManager.persist(secretaria);
            return secretaria;
        } catch (Exception e) {
            logger.error("Error al insertar secretaria: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Secretaria findById(Integer id) {
        try {
            Secretaria secretaria = this.entityManager.find(Secretaria.class, id);

            if (secretaria != null) {
                logger.info("Secretaria encontrado con ID: {}", id);
            } else {
                logger.warn("No se encontró secretaria con ID: {}", id);
            }

            return secretaria;
        } catch (Exception e) {
            logger.error("Error al buscar secretaria con ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al buscar secretaria con ID: " + id, e);
        }
    }

    @Override
    public Boolean deleteSecretariaByIdUsuario(Integer idUsuario) {
        try {
            String query = "DELETE FROM Secretaria s WHERE s.usuario.id = :idUsuario";
            int deletedCount = this.entityManager.createQuery(query)
                    .setParameter("idUsuario", idUsuario)
                    .executeUpdate();

            if (deletedCount > 0) {
                logger.info("Secretaria con IDUSUARIO {} eliminado por falta de verificación.", idUsuario);
                return true;
            } else {
                logger.warn("No se encontró  Secretaria con IDUSUARIO {} para eliminar.", idUsuario);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error al eliminar Secretaria con IDUSUARIO {}: {}", idUsuario, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Secretaria findByIdUsuario(Integer idUsuario) {
        try {
            TypedQuery<Secretaria> query = this.entityManager.createQuery(
                    "SELECT s FROM Secretaria s WHERE s.usuario.id = :idUsuario",
                    Secretaria.class
            );
            query.setParameter("idUsuario", idUsuario);

            Secretaria secretaria = query.getSingleResult();
            logger.info("Secretaria encontrado con ID de usuario: {}", idUsuario);
            return secretaria;
        } catch (NoResultException e) {
            logger.warn("No se encontró Secretaria con ID de usuario: {}", idUsuario);
            return null;
        } catch (Exception e) {
            logger.error("Error al buscar Secretaria con ID Usuario {}: {}", idUsuario, e.getMessage(), e);
            throw new RuntimeException("Error al buscar Secretaria con ID usuario: " + idUsuario, e);
        }
    }
}
