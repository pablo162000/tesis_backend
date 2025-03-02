package com.distribuida.administrativos.repository;


import com.distribuida.administrativos.repository.modelo.Administrativo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class AdministrativoRepositoryImpl implements IAdministrativoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Administrativo insertar(Administrativo administrativo) {
        this.entityManager.persist(administrativo);
        return administrativo;
    }

    @Override
    public Administrativo findByIdUsuario(Integer idUsuario) {
        Administrativo estu = null;
        try {
            System.out.println("ingresa en repository");
            TypedQuery<Administrativo> myQuery = this.entityManager.createQuery(
                    "SELECT a FROM Administrativo a WHERE a.idUsuario = :idUsuario",
                    Administrativo.class
            );
            estu = myQuery.setParameter("idUsuario", idUsuario).getSingleResult();

            System.out.println(estu);
            return  estu;
        } catch (NoResultException e) {

            return null;

        }
    }

    @Override
    public Administrativo existeEstudiante(String correo) {
        return null;
    }

    @Override
    public Administrativo findById(Integer id) {
        try {
            return entityManager.find(Administrativo.class, id);
        } catch (Exception e) {
            System.err.println("Error al buscar el estudiante con ID: " + id);
            e.printStackTrace();
            return null;
        }
    }
}
