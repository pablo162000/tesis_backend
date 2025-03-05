package com.distribuida.administrativos.repository;


import com.distribuida.administrativos.repository.modelo.Docente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class DocenteRepositoryImpl implements IDocenteRepository {

    @Autowired
    private EntityManager entityManager;


    @Override
    public Docente insert(Docente doocente) {
        this.entityManager.persist(doocente);
        return doocente;
    }

    @Override
    public Docente findById(Integer id) {
        try {
            return entityManager.find(Docente.class, id);
        } catch (Exception e) {
            System.err.println("Error al buscar el estudiante con ID: " + id);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Docente findByCedula(String cedula) {
        Docente estu = null;
        try {
            System.out.println("ingresa en repository");
            TypedQuery<Docente> myQuery = this.entityManager.createQuery(
                    "SELECT d FROM Docente d WHERE d.cedula = :cedula",
                    Docente.class
            );
            estu = myQuery.setParameter("cedula", cedula).getSingleResult();

            System.out.println(estu);
            return  estu;
        } catch (NoResultException e) {

            return null;

        }
    }


}
