package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.repository.*;
import com.tesis.backend_tesis.repository.modelo.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;


@Service
public class VistasEntidadesServiceImpl implements IVistasEntidadesService {

    private static final Logger logger = LogManager.getLogger(VistasEntidadesServiceImpl.class);

    @Autowired
    private IVistaEstudianteRepository vistaEstudianteRepository;

    @Autowired
    private IVistaDocenteRepository vistaDocenteRepository;

    @Autowired
    private IVistaSecretariaRepository vistaSecretariaRepository;

    @Autowired
    private IVistaCarreraRepository vistaCarreraRepository;

    @Autowired
    private IVistaUsuarioRolRepository vistaUsuarioRolRepository;

    @Autowired
    private IVistaPropuestaRepository vistaPropuestaRepository;


    @Override
    public VistaEstudiante buscarEstudiantePorIdUsuario(Integer idUsuario) {

        if (idUsuario == null || idUsuario <= 0) {
            logger.warn("El IDUSUARIO no puede ser nulo, 0 o negativo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El IDUSUARIO no puede ser nulo, 0 o negativo.");
        }

        VistaEstudiante vistaEstudiante = this.vistaEstudianteRepository.findByIdUsuario(idUsuario);

        if (vistaEstudiante == null) {
            logger.warn("No se encontró un VistaEstudiante con IDUSUARIO {}.", idUsuario);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró un estudiante con IDUSUARIO: " + idUsuario);
        }

        logger.info("Vista estudiante recuperado con IDUSUARIO {} correctamente.", vistaEstudiante.getIdUsuario());
        return vistaEstudiante;

    }

    @Override
    public VistaEstudiante buscarEstudiantePorIdEstudiante(Integer idEstudiante) {

        if (idEstudiante == null || idEstudiante <= 0) {
            logger.warn("El IDESTUDIANTE no puede ser nulo, 0 o negativo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El IDESTUDIANTE no puede ser nulo, 0 o negativo.");
        }

        VistaEstudiante vistaEstudiante = this.vistaEstudianteRepository.findByIdEstudiante(idEstudiante);

        if (vistaEstudiante == null) {
            logger.warn("No se encontró un VistaEstudiante con IDESTUDIANTE {}.", idEstudiante);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró un estudiante con IDESTUDIANTE: " + idEstudiante);
        }

        logger.info("Vista estudiante recuperado con IDESTUDIANTE {} correctamente.", vistaEstudiante.getIdUsuario());
        return vistaEstudiante;

    }

    @Override
    public List<VistaEstudiante> buscarTodosEstudiantes() {

        List<VistaEstudiante> vistaEstudiantes = this.vistaEstudianteRepository.findAll();

        if (vistaEstudiantes.isEmpty()) {
            logger.warn("No se encontraron registros en VistaEstudiante.");
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay estudiantes registrados.");
        }

        logger.info("Se encontraron {} registros en VistaEstudiante.", vistaEstudiantes.size());
        return vistaEstudiantes;


    }

    @Override
    public List<VistaEstudiante> buscarEstudiantesPorEstadoActivacion(Boolean estado) {

        if (estado == null) {
            logger.warn("El ESTADO no puede ser nulo");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado no puede ser nulo.");
        }

        List<VistaEstudiante> vistaEstudiantes = this.vistaEstudianteRepository.findByEstado(estado);

        if (vistaEstudiantes.isEmpty()) {
            logger.warn("No se encontraron registros en VistaEstudiante con estado {}.", estado);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron estudiantes con estado: " + estado);
        }

        logger.info("Se encontraron {} registros en VistaEstudiante con estado {}.", vistaEstudiantes.size(), estado);
        return vistaEstudiantes;

    }

    @Override
    public VistaEstudiante buscarPorCorreoEstudainte(String correoEstudiante) {
        if (correoEstudiante == null || correoEstudiante.isEmpty()) {
            logger.warn("El correo estudiante no puede ser nulo o vacío.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo no puede ser nulo o vacio.");
        }

        VistaEstudiante vistaEstudiante = this.vistaEstudianteRepository.findByCorreo(correoEstudiante);

        if (vistaEstudiante == null) {
            logger.warn("No se encontró un VistaEstudiante con correo {}.", correoEstudiante);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró un estudiante con correo: " + correoEstudiante);
        }

        logger.info("Vista estudiante recuperado con correo {} correctamente.", vistaEstudiante.getIdUsuario());
        return vistaEstudiante;
    }

    @Override
    public VistaDocente buscarDocentePorIdUsuario(Integer idUsuario) {

        if (idUsuario == null || idUsuario <= 0) {
            logger.warn("El IDUSUARIO no puede ser nulo, 0 o negativo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El IDUSUARIO no puede ser nulo, 0 o negativo.");
        }

        VistaDocente vistaDocente = this.vistaDocenteRepository.findByIdUsuario(idUsuario);

        if (vistaDocente == null) {
            logger.warn("No se encontró un VistaDocente con IDUSUARIO: {}", idUsuario);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró un VistaDocente con IDUSUARIO: " + idUsuario);
        }

        logger.info("VistaDocente recuperado con IDUSUARIO: {}", idUsuario);
        return vistaDocente;

    }

    @Override
    public VistaDocente buscarDocentePorIdDocente(Integer idDocente) {
        if (idDocente == null || idDocente <= 0) {
            logger.warn("El IDDOCENTE no puede ser nulo, 0 o negativo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El IDDOCENTE no puede ser nulo, 0 o negativo.");
        }

        VistaDocente vistaDocente = this.vistaDocenteRepository.findByIdDocente(idDocente);

        if (vistaDocente == null) {
            logger.warn("No se encontró un VistaDocente con IDDOCENTE: {}", idDocente);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró un VistaDocente con IDDOCENTE: " + idDocente);
        }

        logger.info("VistaDocente recuperado con IDDOCENTE: {}", idDocente);
        return vistaDocente;

    }

    @Override
    public List<VistaDocente> buscarTodosDocente() {

        List<VistaDocente> vistaDocentes = this.vistaDocenteRepository.findAll();

        if (vistaDocentes.isEmpty()) {
            logger.warn("No se encontraron registros en VistaDocente.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros en VistaDocente.");
        }

        logger.info("Se encontraron {} registros en VistaDocente.", vistaDocentes.size());
        return vistaDocentes;

    }

    @Override
    public List<VistaDocente> buscarDocentesPorEstado(Boolean activo) {

        if (activo == null) {
            logger.warn("El ESTADO no puede ser nulo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ESTADO no puede ser nulo.");
        }

        List<VistaDocente> vistaDocentes = this.vistaDocenteRepository.findByEstado(activo);

        if (vistaDocentes.isEmpty()) {
            logger.warn("No se encontraron registros en VistaDocente con estado: {}", activo);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros en VistaDocente con estado: " + activo);
        }

        logger.info("Se encontraron {} registros en VistaDocente con estado: {}", vistaDocentes.size(), activo);
        return vistaDocentes;

    }

    @Override
    public VistaDocente buscarPorCorreoDocente(String correoDocente) {
        if (correoDocente == null || correoDocente.isEmpty()) {
            logger.warn("El correo docente no puede ser nulo o vacío.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo no puede ser nulo o vacio.");
        }

        VistaDocente vistaDocente = this.vistaDocenteRepository.findByCorreo(correoDocente);

        if (vistaDocente == null) {
            logger.warn("No se encontró un Vistadocente con correo {}.", correoDocente);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró un docente con correo: " + correoDocente);
        }

        logger.info("Vista docente recuperado con correo {} correctamente.", vistaDocente.getIdUsuario());
        return vistaDocente;
    }


    @Override
    public VistaSecretaria buscarSecretariaPorIdUsuario(Integer idUsuario) {

        if (idUsuario == null || idUsuario <= 0) {
            logger.warn("El IDUSUARIO no puede ser nulo, 0 o negativo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El IDUSUARIO no puede ser nulo, 0 o negativo.");
        }

        VistaSecretaria vistaSecretaria = this.vistaSecretariaRepository.findByIdUsuario(idUsuario);

        if (vistaSecretaria == null) {
            logger.warn("No se encontró una VistaSecretaria con IDUSUARIO {}.", idUsuario);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró una VistaSecretaria con IDUSUARIO.");
        }

        logger.info("VistaSecretaria recuperada con IDUSUARIO {} correctamente.", vistaSecretaria.getIdUsuario());
        return vistaSecretaria;

    }

    @Override
    public VistaSecretaria buscarSecretariaPorIdSecretaria(Integer idSecretaria) {

        if (idSecretaria == null || idSecretaria <= 0) {
            logger.warn("El IDSECRETARIA no puede ser nulo, 0 o negativo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El IDSECRETARIA no puede ser nulo, 0 o negativo.");
        }

        VistaSecretaria vistaSecretaria = this.vistaSecretariaRepository.findByIdSecretaria(idSecretaria);

        if (vistaSecretaria == null) {
            logger.warn("No se encontró una VistaSecretaria con IDSECRETARIA: {}", idSecretaria);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró una VistaSecretaria con IDSECRETARIA: " + idSecretaria);
        }

        logger.info("VistaSecretaria recuperada con IDSECRETARIA: {}", idSecretaria);
        return vistaSecretaria;

    }

    @Override
    public List<VistaSecretaria> buscarTodosSecretarias() {

        List<VistaSecretaria> vistaSecretarias = this.vistaSecretariaRepository.findAll();

        if (vistaSecretarias.isEmpty()) {
            logger.warn("No se encontraron registros en VistaSecretaria.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros en VistaSecretaria.");
        } else {
            logger.info("Se encontraron {} registros en VistaSecretaria.", vistaSecretarias.size());
        }

        return vistaSecretarias;

    }

    @Override
    public List<VistaSecretaria> buscarSecretariasPorEstado(Boolean activo) {

        if (activo == null) {
            logger.warn("El ESTADO no puede ser nulo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ESTADO no puede ser nulo.");
        }

        List<VistaSecretaria> vistaSecretarias = this.vistaSecretariaRepository.findByEstado(activo);

        if (vistaSecretarias.isEmpty()) {
            logger.warn("No se encontraron registros en VistaSecretaria con estado {}.", activo);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros en VistaSecretaria con estado " + activo + ".");
        } else {
            logger.info("Se encontraron {} registros en VistaSecretaria con estado {}.", vistaSecretarias.size(), activo);
        }

        return vistaSecretarias;

    }

    @Override
    public VistaCarrera buscarCarreraPorIdCarrera(Integer idCarrera) {

        if (idCarrera == null || idCarrera <= 0) {
            logger.warn("El IDCARRERA no puede ser nulo, 0 o negativo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El IDCARRERA no puede ser nulo, 0 o negativo.");
        }

        VistaCarrera vistaCarrera = this.vistaCarreraRepository.findByIdCarrera(idCarrera);

        if (vistaCarrera == null) {
            logger.warn("No se encontró una VistaCarrera con IDCARRERA {}.", idCarrera);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró una VistaCarrera con IDCARRERA: " + idCarrera);
        }

        logger.info("VistaCarrera recuperada con IDCARRERA {} correctamente.", vistaCarrera.getIdCarrera());
        return vistaCarrera;


    }

    @Override
    public List<VistaCarrera> buscarCarreraIdFacultad(Integer idFacultad) {

        List<VistaCarrera> vistaCarreras = this.vistaCarreraRepository.findByIdFacultad(idFacultad);

        if (vistaCarreras.isEmpty()) {
            logger.warn("No se encontraron registros en VistaCarrera para la facultad con ID {}.", idFacultad);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros en VistaCarrera para la facultad con ID: " + idFacultad);
        } else {
            logger.info("Se encontraron {} registros en VistaCarrera para la facultad con ID {}.", vistaCarreras.size(), idFacultad);
        }

        return vistaCarreras;

    }

    @Override
    public List<VistaCarrera> buscarTodasCarreras() {

        List<VistaCarrera> vistaCarreras = this.vistaCarreraRepository.findAllCarreras();

        if (vistaCarreras.isEmpty()) {
            logger.warn("No se encontraron registros en VistaCarrera.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros en VistaCarrera.");
        } else {
            logger.info("Se encontraron {} registros en VistaCarrera.", vistaCarreras.size());
        }

        return vistaCarreras;

    }

    @Override
    public VistaCarrera buscarCarreraPorNombreCarrera(String nombreCarrera) {

        if (nombreCarrera == null || nombreCarrera.trim().isEmpty()) {
            logger.warn("El nombre de la carrera no puede ser nulo o vacío.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la carrera no puede ser nulo o vacío.");
        }

        VistaCarrera vistaCarrera = this.vistaCarreraRepository.findByNombreCarrera(nombreCarrera.trim());

        if (vistaCarrera == null) {
            logger.warn("No se encontró una VistaCarrera con el nombre '{}'.", nombreCarrera);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró una VistaCarrera con el nombre: " + nombreCarrera);
        }

        logger.info("VistaCarrera recuperada correctamente con el nombre '{}'.", nombreCarrera);
        return vistaCarrera;


    }

    @Override
    public VistaUsuarioRol buscarUsuarioRolPorIdUsuarioRol(Integer idUsuarioRol) {
        if (idUsuarioRol == null || idUsuarioRol <= 0) {
            logger.warn("El IDUSUARIOROL no puede ser nulo, 0 o negativo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El IDUSUARIOROL no puede ser nulo, 0 o negativo.");
        }

        VistaUsuarioRol vistaUsuarioRol = this.vistaUsuarioRolRepository.findByIdUsuarioRol(idUsuarioRol);

        if (vistaUsuarioRol == null) {
            logger.warn("No se encontró un VistaUsuarioRol con IDUSUARIOROL {}.", idUsuarioRol);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró un VistaUsuarioRol con el IDUSUARIOROL proporcionado.");
        }

        logger.info("VistaUsuarioRol recuperado con IDUSUARIOROL {} correctamente.", vistaUsuarioRol.getIdUsuarioRol());
        return vistaUsuarioRol;
    }

    @Override
    public VistaUsuarioRol buscarUsuarioRolPorCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            logger.warn("El correo no puede ser nulo o vacío.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo no puede ser nulo o vacío.");
        }

        VistaUsuarioRol vistaUsuarioRol = this.vistaUsuarioRolRepository.findByCorreo(correo.trim());

        if (vistaUsuarioRol == null) {
            logger.warn("No se encontró un VistaUsuarioRol con el correo '{}'.", correo);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró un VistaUsuarioRol con el correo proporcionado.");
        }

        logger.info("VistaUsuarioRol recuperado correctamente con el correo '{}'.", correo);
        return vistaUsuarioRol;
    }

    @Override
    public List<VistaUsuarioRol> buscarUsuarioRolPorIdRol(Integer idRol) {
        if (idRol == null || idRol <= 0) {
            logger.warn("El IDROL no puede ser nulo, 0 o negativo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El IDROL no puede ser nulo, 0 o negativo.");
        }

        List<VistaUsuarioRol> usuariosRol = this.vistaUsuarioRolRepository.findByIdRol(idRol);

        if (usuariosRol.isEmpty()) {
            logger.warn("No se encontraron registros de VistaUsuarioRol con IDROL {}.", idRol);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros con el IDROL proporcionado.");
        }

        logger.info("Se encontraron {} registros de VistaUsuarioRol con IDROL {}.", usuariosRol.size(), idRol);
        return usuariosRol;
    }

    @Override
    public List<VistaUsuarioRol> buscarUsuarioRolPorNombreRol(String nombreRol) {
        if (nombreRol == null || nombreRol.trim().isEmpty()) {
            logger.warn("El nombre del rol no puede ser nulo o vacío.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del rol no puede ser nulo o vacío.");
        }

        List<VistaUsuarioRol> usuariosRol = this.vistaUsuarioRolRepository.findByNombreRol(nombreRol.trim());

        if (usuariosRol.isEmpty()) {
            logger.warn("No se encontraron registros de VistaUsuarioRol con el nombre de rol '{}'.", nombreRol);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros con el nombre del rol proporcionado.");
        }

        logger.info("Se encontraron {} registros de VistaUsuarioRol con el nombre de rol '{}'.", usuariosRol.size(), nombreRol);
        return usuariosRol;
    }

    @Override
    public List<VistaUsuarioRol> buscarUsuarioRolPorIdUsuario(Integer idUsuario) {
        if (idUsuario == null || idUsuario <= 0) {
            logger.warn("El IDUSUARIO no puede ser nulo, 0 o negativo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El IDUSUARIO no puede ser nulo, 0 o negativo.");
        }

        List<VistaUsuarioRol> usuariosRol = this.vistaUsuarioRolRepository.findByIdUsuario(idUsuario);

        if (usuariosRol.isEmpty()) {
            logger.warn("No se encontraron registros de VistaUsuarioRol con IDUSUARIO {}.", idUsuario);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros con el IDUSUARIO proporcionado.");
        }

        logger.info("Se encontraron {} registros de VistaUsuarioRol con IDUSUARIO {}.", usuariosRol.size(), idUsuario);
        return usuariosRol;
    }

    @Override
    public List<VistaUsuarioRol> buscarUsuarioRolPorApellidos(String apellidos) {
        if (apellidos == null || apellidos.trim().isEmpty()) {
            logger.warn("Los apellidos no pueden ser nulos o vacíos.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los apellidos no pueden ser nulos o vacíos.");
        }

        List<VistaUsuarioRol> usuariosRol = this.vistaUsuarioRolRepository.findByApellidos(apellidos.trim());

        if (usuariosRol.isEmpty()) {
            logger.warn("No se encontraron registros de VistaUsuarioRol con apellidos '{}'.", apellidos);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros con los apellidos proporcionados.");
        }

        logger.info("Se encontraron {} registros de VistaUsuarioRol con apellidos '{}'.", usuariosRol.size(), apellidos);
        return usuariosRol;
    }

    @Override
    public List<VistaUsuarioRol> buscarUsuarioRolPorNombres(String nombres) {
        if (nombres == null || nombres.trim().isEmpty()) {
            logger.warn("Los nombres no pueden ser nulos o vacíos.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los nombres no pueden ser nulos o vacíos.");
        }

        List<VistaUsuarioRol> usuariosRol = this.vistaUsuarioRolRepository.findByNombres(nombres.trim());

        if (usuariosRol.isEmpty()) {
            logger.warn("No se encontraron registros de VistaUsuarioRol con nombres '{}'.", nombres);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros con los nombres proporcionados.");
        }

        logger.info("Se encontraron {} registros de VistaUsuarioRol con nombres '{}'.", usuariosRol.size(), nombres);
        return usuariosRol;
    }

    @Override
    public List<VistaUsuarioRol> buscarTodosUsuarioRol() {
        List<VistaUsuarioRol> vistaUsuarioRols = this.vistaUsuarioRolRepository.findAll();

        if (vistaUsuarioRols.isEmpty()) {
            logger.warn("No se encontraron registros en VistaUsuarioRol.");
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay usuarios con roles registrados.");
        }

        logger.info("Se encontraron {} registros en VistaUsuarioRol.", vistaUsuarioRols.size());
        return vistaUsuarioRols;
    }

    @Override
    public List<VistaPropuesta> buscarPropuestaPorIdPropuesta(Integer idPropuesta) {
        if (idPropuesta == null || idPropuesta <= 0) {
            logger.warn("El idPropuesta no puede ser nulo, 0 o negativo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El idPropuesta no puede ser nulo, 0 o negativo.");
        }

        List<VistaPropuesta> vistaPropuestas = this.vistaPropuestaRepository.findById(idPropuesta);

        if (vistaPropuestas.isEmpty()) {
            logger.warn("No se encontraron registros de VistaPropuesta con idPropuesta '{}'.", idPropuesta);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros con  idPropuesta proporcionados.");
        }

        logger.info("Se encontraron {} registros de VistaPropuesta con idPropuesta '{}'.", vistaPropuestas.size(), idPropuesta);
        return vistaPropuestas;
    }

}
