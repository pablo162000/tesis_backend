package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.EstadoAprobacion;
import com.tesis.backend_tesis.repository.modelo.EstadoValidacion;
import com.tesis.backend_tesis.repository.modelo.VistaEstudiante;
import com.tesis.backend_tesis.repository.modelo.VistaPropuesta;
import jakarta.persistence.EntityManager;
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
public class VistaPropuestaRepositoryImpl implements IVistaPropuestaRepository {

    private static final Logger logger = LogManager.getLogger(VistaPropuestaRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<VistaPropuesta> findById(Integer idPropuesta) {
        try {
            TypedQuery<VistaPropuesta> query = this.entityManager.createQuery(
                    "SELECT p FROM VistaPropuesta p WHERE p.id = :idPropuesta",
                    VistaPropuesta.class
            );
            query.setParameter("idPropuesta", idPropuesta);
            List<VistaPropuesta> vistaPropuestas = query.getResultList();

            if (vistaPropuestas.isEmpty()) {
                logger.debug("No se encontraron propuestas en VistaPropuesta con idPropuesta {}.", idPropuesta);
            } else {
                logger.debug("Se encontraron {} propuestas en VistaPropuesta con idPropuesta {}.", vistaPropuestas.size(), idPropuesta);
            }

            return vistaPropuestas;
        } catch (Exception e) {
            logger.error("Error al buscar propuestas en VistaPropuesta con idPropuesta: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaPropuesta> findByCarrera(String carrera) {
        try {
            TypedQuery<VistaPropuesta> query = this.entityManager.createQuery(
                    "SELECT p FROM VistaPropuesta p WHERE p.carrera = :carrera",
                    VistaPropuesta.class
            );
            query.setParameter("carrera", carrera);
            List<VistaPropuesta> vistaPropuestas = query.getResultList();

            if (vistaPropuestas.isEmpty()) {
                logger.debug("No se encontraron propuestas en VistaPropuesta con carrera {}.", carrera);
            } else {
                logger.debug("Se encontraron {} propuestas en VistaPropuesta con carrera {}.", vistaPropuestas.size(), carrera);
            }

            return vistaPropuestas;
        } catch (Exception e) {
            logger.error("Error al buscar propuestas en VistaPropuesta con carrera: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaPropuesta> findByEstadoValidacion(EstadoValidacion estadoValidacion, String carrera) {
        try {
            TypedQuery<VistaPropuesta> query = this.entityManager.createQuery(
                    "SELECT p FROM VistaPropuesta p WHERE p.carrera = :carrera AND  p.estadoValidacion = :estadoValidacion",
                    VistaPropuesta.class
            );
            query.setParameter("carrera", carrera);
            query.setParameter("estadoValidacion", estadoValidacion);
            List<VistaPropuesta> vistaPropuestas = query.getResultList();

            if (vistaPropuestas.isEmpty()) {
                logger.debug("No se encontraron propuestas en VistaPropuesta con carrera {} y estadoValidacion {}.", carrera, estadoValidacion);
            } else {
                logger.debug("Se encontraron {} propuestas en VistaPropuesta con carrera {} y estadoValidacion {}.", vistaPropuestas.size(), carrera, estadoValidacion);
            }

            return vistaPropuestas;
        } catch (Exception e) {
            logger.error("Error al buscar propuestas en VistaPropuesta con carrera y estadoValidacion: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaPropuesta> findByEstadoAprobacion(EstadoAprobacion estadoAprobacion, String carrera) {
        try {
            TypedQuery<VistaPropuesta> query = this.entityManager.createQuery(
                    "SELECT p FROM VistaPropuesta p WHERE p.carrera = :carrera  AND  p.estadoAprobacion = :estadoAprobacion",
                    VistaPropuesta.class
            );
            query.setParameter("carrera", carrera);
            query.setParameter("estadoAprobacion", estadoAprobacion);
            List<VistaPropuesta> vistaPropuestas = query.getResultList();

            if (vistaPropuestas.isEmpty()) {
                logger.debug("No se encontraron propuestas en VistaPropuesta con carrera {} y  estadoAprobacion {}.", carrera, estadoAprobacion);
            } else {
                logger.debug("Se encontraron {} propuestas en VistaPropuesta con carrera {} y  estadoAprobacion {}.", vistaPropuestas.size(), carrera, estadoAprobacion);
            }

            return vistaPropuestas;
        } catch (Exception e) {
            logger.error("Error al buscar propuestas en VistaPropuesta con carrera y estadoaprobacion: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaPropuesta> findByPeriodo(String periodo, String carrera) {
        try {
            TypedQuery<VistaPropuesta> query = this.entityManager.createQuery(
                    "SELECT p FROM VistaPropuesta p WHERE p.carrera = :carrera  AND  p.periodo = :periodo",
                    VistaPropuesta.class
            );
            query.setParameter("carrera", carrera);
            query.setParameter("periodo", periodo);
            List<VistaPropuesta> vistaPropuestas = query.getResultList();

            if (vistaPropuestas.isEmpty()) {
                logger.debug("No se encontraron propuestas en VistaPropuesta con carrera {} y  periodo {}.", carrera, periodo);
            } else {
                logger.debug("Se encontraron {} propuestas en VistaPropuesta con carrera {} y  periodo {}.", vistaPropuestas.size(), carrera, periodo);
            }

            return vistaPropuestas;
        } catch (Exception e) {
            logger.error("Error al buscar propuestas en VistaPropuesta con carrera y periodo: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaPropuesta> findByTipo(String tipo, String carrera) {
        try {
            TypedQuery<VistaPropuesta> query = this.entityManager.createQuery(
                    "SELECT p FROM VistaPropuesta p WHERE p.carrera = :carrera  AND  p.tipo = :tipo",
                    VistaPropuesta.class
            );
            query.setParameter("carrera", carrera);
            query.setParameter("tipo", tipo);
            List<VistaPropuesta> vistaPropuestas = query.getResultList();

            if (vistaPropuestas.isEmpty()) {
                logger.debug("No se encontraron propuestas en VistaPropuesta con carrera {} y  tipo {}.", carrera, tipo);
            } else {
                logger.debug("Se encontraron {} propuestas en VistaPropuesta con carrera {} y  tipo {}.", vistaPropuestas.size(), carrera, tipo);
            }

            return vistaPropuestas;
        } catch (Exception e) {
            logger.error("Error al buscar propuestas en VistaPropuesta con carrera y tipo: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaPropuesta> findByCategoria(String categoria, String carrera) {
        try {
            TypedQuery<VistaPropuesta> query = this.entityManager.createQuery(
                    "SELECT p FROM VistaPropuesta p WHERE p.carrera = :carrera  AND  p.categoria = :categoria",
                    VistaPropuesta.class
            );
            query.setParameter("carrera", carrera);
            query.setParameter("categoria", categoria);
            List<VistaPropuesta> vistaPropuestas = query.getResultList();

            if (vistaPropuestas.isEmpty()) {
                logger.debug("No se encontraron propuestas en VistaPropuesta con carrera {} y  categoria {}.", carrera, categoria);
            } else {
                logger.debug("Se encontraron {} propuestas en VistaPropuesta con carrera {} y  categoria {}.", vistaPropuestas.size(), carrera, categoria);
            }

            return vistaPropuestas;
        } catch (Exception e) {
            logger.error("Error al buscar propuestas en VistaPropuesta con carrera y categoria: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaPropuesta> findByTipoCategoria(String tipo, String categoria, String carrera) {
        try {
            TypedQuery<VistaPropuesta> query = this.entityManager.createQuery(
                    "SELECT p FROM VistaPropuesta p WHERE p.carrera = :carrera  AND  p.categoria = :categoria AND p.tipo = :tipo",
                    VistaPropuesta.class
            );
            query.setParameter("carrera", carrera);
            query.setParameter("categoria", categoria);
            query.setParameter("tipo", tipo);
            List<VistaPropuesta> vistaPropuestas = query.getResultList();

            if (vistaPropuestas.isEmpty()) {
                logger.debug("No se encontraron propuestas en VistaPropuesta con carrera {}, tipo {} y categoria {}.", carrera, tipo,categoria);
            } else {
                logger.debug("Se encontraron {} propuestas en VistaPropuesta con carrera {}, tipo {} y  categoria {}.", vistaPropuestas.size(), carrera, tipo,categoria);
            }

            return vistaPropuestas;
        } catch (Exception e) {
            logger.error("Error al buscar propuestas en VistaPropuesta con carrera, tipo y categoria: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaPropuesta> findByTema(String tema, String carrera) {
        try {
            TypedQuery<VistaPropuesta> query = this.entityManager.createQuery(
                    "SELECT p FROM VistaPropuesta p WHERE p.carrera = :carrera  AND  p.tema = :tema",
                    VistaPropuesta.class
            );
            query.setParameter("carrera", carrera);
            query.setParameter("tema", tema);
            List<VistaPropuesta> vistaPropuestas = query.getResultList();

            if (vistaPropuestas.isEmpty()) {
                logger.debug("No se encontraron propuestas en VistaPropuesta con carrera {} y  tema {}.", carrera, tema);
            } else {
                logger.debug("Se encontraron {} propuestas en VistaPropuesta con carrera {} y  tema {}.", vistaPropuestas.size(), carrera, tema);
            }

            return vistaPropuestas;
        } catch (Exception e) {
            logger.error("Error al buscar propuestas en VistaPropuesta con carrera y tema: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaPropuesta> findByTutor(Integer idUsuario, String facultad) {
        try {
            TypedQuery<VistaPropuesta> query = this.entityManager.createQuery(
                    "SELECT p FROM VistaPropuesta p WHERE p.tutorFacultad = :facultad  AND p.tutor = :idUsuario",
                    VistaPropuesta.class
            );
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("facultad", facultad);
            List<VistaPropuesta> vistaPropuestas = query.getResultList();

            if (vistaPropuestas.isEmpty()) {
                logger.debug("No se encontraron propuestas en VistaPropuesta con tutor idUsuario {} y  facultad {}.", idUsuario, facultad);
            } else {
                logger.debug("Se encontraron {} propuestas en VistaPropuesta con tutor idUsuario {} y  facultad {}.", vistaPropuestas.size(), idUsuario, facultad);
            }

            return vistaPropuestas;
        } catch (Exception e) {
            logger.error("Error al buscar propuestas en VistaPropuesta con tutor idUsuario  y facultad: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaPropuesta> findByRevisor(Integer idUsuario,  String facultad) {
        try {
            TypedQuery<VistaPropuesta> query = this.entityManager.createQuery(
                    "SELECT p FROM VistaPropuesta p WHERE p.tutorFacultad = :facultad  AND ( p.revisor1UsuaId = :idUsuario OR p.revisor2UsuaId = :idUsuario)",
                    VistaPropuesta.class
            );
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("facultad", facultad);
            List<VistaPropuesta> vistaPropuestas = query.getResultList();

            if (vistaPropuestas.isEmpty()) {
                logger.debug("No se encontraron propuestas en VistaPropuesta con revisor idUsuario {} y  facultad {}.", idUsuario, facultad);
            } else {
                logger.debug("Se encontraron {} propuestas en VistaPropuesta con revisor idUsuario {} y  facultad {}.", vistaPropuestas.size(), idUsuario, facultad);
            }

            return vistaPropuestas;
        } catch (Exception e) {
            logger.error("Error al buscar propuestas en VistaPropuesta con revisor idUsuario  y facultad: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<VistaPropuesta> findAll() {
        return List.of();
    }
}
