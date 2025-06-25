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

        VistaEstudiante vistaEstudiante = this.vistaEstudianteRepository.findByCorreo(correoEstudiante.trim());

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
    public List<VistaDocente> buscarDocentesPorFacultad(String facultad) {
        if (facultad == null || facultad.isEmpty()) {
            logger.warn("La facultad no puede ser nulo");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La facultad no puede ser nulo.");
        }

        List<VistaDocente> vistaDocentes = this.vistaDocenteRepository.findByNomBreFacultad(facultad);

        if (vistaDocentes.isEmpty()) {
            logger.warn("No se encontraron registros en VistaDocentes con facultad {}.", facultad);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron estudiantes con facultad: " + facultad);
        }

        logger.info("Se encontraron {} registros en VistaDocentes con estado {}.", vistaDocentes.size(), facultad);
        return vistaDocentes;
    }

    @Override
    public VistaDocente buscarPorCorreoDocente(String correoDocente) {
        if (correoDocente == null || correoDocente.isEmpty()) {
            logger.warn("El correo docente no puede ser nulo o vacío.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo no puede ser nulo o vacio.");
        }

        VistaDocente vistaDocente = this.vistaDocenteRepository.findByCorreo(correoDocente.trim());

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
    public List<VistaSecretaria> buscarSecretariasPorEstadoCarrera(Boolean activo, String carrera) {
        if (activo == null) {
            logger.warn("El ESTADO no puede ser nulo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ESTADO no puede ser nulo.");
        }

        List<VistaSecretaria> vistaSecretarias = this.vistaSecretariaRepository.findByEstadoCarrera(activo, carrera);

        if (vistaSecretarias.isEmpty()) {
            logger.warn("No se encontraron registros en VistaSecretaria con estado {} y carrea {}.", activo, carrera);
            //throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros en VistaSecretaria con estado " + activo + " y carrera "+ carrera);
        } else {
            logger.info("Se encontraron {} registros en VistaSecretaria con estado {} y carrera.", vistaSecretarias.size(), activo, carrera);
        }

        return vistaSecretarias;
    }

    @Override
    public List<VistaSecretaria> buscarSecretariasPorEstadoFacultad(Boolean activo, String facultad) {
        if (activo == null) {
            logger.warn("El ESTADO no puede ser nulo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ESTADO no puede ser nulo.");
        }

        List<VistaSecretaria> vistaSecretarias = this.vistaSecretariaRepository.findByEstadoFacultad(activo, facultad);

        if (vistaSecretarias.isEmpty()) {
            logger.warn("No se encontraron registros en VistaSecretaria con estado {} y carrea {}.", activo, facultad);
            //throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros en VistaSecretaria con estado " + activo + " y carrera "+ facultad);
        } else {
            logger.info("Se encontraron {} registros en VistaSecretaria con estado {} y facultad.", vistaSecretarias.size(), activo, facultad);
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
    public VistaCarrera buscarPorIdUsuarioCoordiandor(Integer idUsuarioCarrera) {
        if (idUsuarioCarrera == null || idUsuarioCarrera <= 0) {
            logger.warn("El IDUSUARIOCOORDIANDOR no puede ser nulo, 0 o negativo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El IDUSUARIOCOORDIANDOR no puede ser nulo, 0 o negativo.");
        }

        VistaCarrera vistaCarrera = this.vistaCarreraRepository.findByIdUsuarioCoordiandor(idUsuarioCarrera);

        if (vistaCarrera == null) {
            logger.warn("No se encontró una VistaCarrera con IDUSUARIOCOORDIANDOR {}.", idUsuarioCarrera);
            //throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró una VistaCarrera con IDUSUARIOCARRERA: " + idCarrera);
        }

        logger.info("VistaCarrera recuperada con IDCARRERA {} correctamente.", vistaCarrera.getIdCarrera());
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
    public List<VistaUsuarioRol> buscarUsuarioRolPorNombreRolYEstado(String nombreRol, Boolean estado) {
        if (nombreRol == null || nombreRol.trim().isEmpty()) {
            logger.warn("El nombre del rol no puede ser nulo o vacío.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del rol no puede ser nulo o vacío.");
        }
        if (estado == null ) {
            logger.warn("El estado del rol no puede ser nulo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado del rol no puede ser nulo.");
        }


        List<VistaUsuarioRol> usuariosRol = this.vistaUsuarioRolRepository.findByNombreRolAndEstado(nombreRol.trim(), estado);

        if (usuariosRol.isEmpty()) {
            logger.warn("No se encontraron registros de VistaUsuarioRol con el nombre de rol '{}' y estado '{}'.", nombreRol, estado);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros con el nombre del rol y estado proporcionado.");
        }

        logger.info("Se encontraron {} registros de VistaUsuarioRol con el nombre de rol '{}' y estado '{}'.", usuariosRol.size(), nombreRol, estado);
        return usuariosRol;
    }

    @Override
    public List<VistaPropuesta> buscarPropuestaPorIdPropuesta(Integer idPropuesta) {
        if (idPropuesta == null || idPropuesta <= 0) {
            logger.warn("El idPropuesta no puede ser nulo, 0 o negativo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El idPropuesta no puede ser nulo, 0 o negativo.");
        }

        List<VistaPropuesta> vistaPropuestas = this.vistaPropuestaRepository.findById(idPropuesta);

        System.out.println(vistaPropuestas);

        if (vistaPropuestas.isEmpty()) {
            logger.warn("No se encontraron registros de VistaPropuesta con idPropuesta '{}'.", idPropuesta);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros con  idPropuesta proporcionados.");
        }

        logger.info("Se encontraron {} registros de VistaPropuesta con idPropuesta '{}'.", vistaPropuestas.size(), idPropuesta);
        return vistaPropuestas;
    }

    @Override
    public List<VistaPropuesta> buscarPropuestaPorCarrera(String carrera) {
        if (carrera == null || carrera.isEmpty()) {
            logger.warn("La carrea no puede ser nulo o vacío.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La carrea no puede ser nulo o vacío.");
        }

        List<VistaPropuesta> vistaPropuestas = this.vistaPropuestaRepository.findByCarrera(carrera);

        if (vistaPropuestas.isEmpty()) {
            logger.warn("No se encontraron registros de VistaPropuesta con carrera '{}'.", carrera);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros con carrera proporcionados.");
        }

        logger.info("Se encontraron {} registros de VistaPropuesta con carrera '{}'.", vistaPropuestas.size(), carrera);
        return vistaPropuestas;
    }

    @Override
    public List<VistaPropuesta> buscarPropuestaPorEstadoValidacion(Integer estadoValidacion, String carrera) {
        if (estadoValidacion < 0  || estadoValidacion >3 || carrera == null || carrera.isEmpty()) {
            logger.warn("La carrea no puede ser nulo o vacío y el estado de validacion es 0, 1 o 2.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La carrea no puede ser nulo o vacío y el estado de validacion es 0, 1 o 2.");
        }
        EstadoValidacion validacion = null;

        if(estadoValidacion == 0){
            validacion = EstadoValidacion.NO_VALIDADO;
        }
        if(estadoValidacion == 1){
            validacion = EstadoValidacion.NO_REVISADO;
        }

        if(estadoValidacion == 2){
            validacion = EstadoValidacion.VALIDADO;
        }

        List<VistaPropuesta> vistaPropuestas = this.vistaPropuestaRepository.findByEstadoValidacion(validacion,carrera);

        if (vistaPropuestas.isEmpty()) {
            logger.warn("No se encontraron registros de VistaPropuesta con carrera {} y el estado de validacion '{}'.", carrera, validacion);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros con carrera y estado de validacion proporcionados.");
        }

        logger.info("Se encontraron {} registros de VistaPropuesta con carrera '{}' y estado validacion {} .", vistaPropuestas.size(), carrera, validacion);
        return vistaPropuestas;
    }

    @Override
    public List<VistaPropuesta> buscarPropuestaPorEstadoAprobacion(Integer estadoAprobacion, String carrera) {
        if (estadoAprobacion < 0  || estadoAprobacion >3 || carrera == null || carrera.isEmpty()) {
            logger.warn("La carrea no puede ser nulo o vacío y el estado de validacion es 0, 1 o 2.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La carrea no puede ser nulo o vacío y el estado de aprobacion es 0, 1 o 2.");
        }
        EstadoAprobacion aprobacion = null;

        if(estadoAprobacion == 0){
            aprobacion = EstadoAprobacion.NO_APROBADO;
        }
        if(estadoAprobacion == 1){
            aprobacion = EstadoAprobacion.EN_REVISON;
        }

        if(estadoAprobacion == 2){
            aprobacion = EstadoAprobacion.APROBADO;
        }

        List<VistaPropuesta> vistaPropuestas = this.vistaPropuestaRepository.findByEstadoAprobacion(aprobacion,carrera);

        if (vistaPropuestas.isEmpty()) {
            logger.warn("No se encontraron registros de VistaPropuesta con carrera {} y estado de aprobación'{}'.", carrera, aprobacion);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros con carrera y estado de aprobación proporcionados.");
        }

        logger.info("Se encontraron {} registros de VistaPropuesta con carrera '{}' y estado aprobacion {} .", vistaPropuestas.size(), carrera, aprobacion);
        return vistaPropuestas;
    }

    @Override
    public List<VistaPropuesta> buscarPropuestaPorPeriodo(String periodo, String carrera) {
        if (periodo ==null || periodo.isEmpty()|| carrera == null || carrera.isEmpty()) {
            logger.warn("La carrea o periodo no puede ser nulo o vacío.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La carrea o periodo no puede ser nulo o vacío.");
        }

        List<VistaPropuesta> vistaPropuestas = this.vistaPropuestaRepository.findByPeriodo(periodo,carrera);

        if (vistaPropuestas.isEmpty()) {
            logger.warn("No se encontraron registros de VistaPropuesta con carrera {} y periodo'{}'.", carrera, periodo);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros con carrera y periodo proporcionados.");
        }

        logger.info("Se encontraron {} registros de VistaPropuesta con carrera '{}' y periodo {} .", vistaPropuestas.size(), carrera, periodo);
        return vistaPropuestas;
    }

    @Override
    public List<VistaPropuesta> buscarPropuestaPorTipo(String tipo, String carrera) {
        if (tipo ==null || tipo.isEmpty()|| carrera == null || carrera.isEmpty()) {
            logger.warn("La carrea o tipo no puede ser nulo o vacío.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La carrea o tipo no puede ser nulo o vacío.");
        }

        List<VistaPropuesta> vistaPropuestas = this.vistaPropuestaRepository.findByTipo(tipo,carrera);

        if (vistaPropuestas.isEmpty()) {
            logger.warn("No se encontraron registros de VistaPropuesta con carrera {} y tipo'{}'.", carrera, tipo);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros con carrera y tipo proporcionados.");
        }

        logger.info("Se encontraron {} registros de VistaPropuesta con carrera '{}' y tipo {} .", vistaPropuestas.size(), carrera, tipo);
        return vistaPropuestas;
    }

    @Override
    public List<VistaPropuesta> buscarPropuestaPorCategoria(String categoria, String carrera) {
        if (categoria ==null || categoria.isEmpty()|| carrera == null || carrera.isEmpty()) {
            logger.warn("La carrea o categoria no puede ser nulo o vacío.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La carrea o categoria no puede ser nulo o vacío.");
        }

        List<VistaPropuesta> vistaPropuestas = this.vistaPropuestaRepository.findByCategoria(categoria,carrera);

        if (vistaPropuestas.isEmpty()) {
            logger.warn("No se encontraron registros de VistaPropuesta con carrera {} y categoria'{}'.", carrera, categoria);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros con carrera y categoria proporcionados.");
        }

        logger.info("Se encontraron {} registros de VistaPropuesta con carrera '{}' y categoria {} .", vistaPropuestas.size(), carrera, categoria);
        return vistaPropuestas;
    }

    @Override
    public List<VistaPropuesta> buscarPropuestaPorTipoCategoria(String tipo, String categoria, String carrera) {
        if (tipo ==null || tipo.isEmpty() || categoria ==null || categoria.isEmpty()|| carrera == null || carrera.isEmpty()) {
            logger.warn("La carrea, tipo o categoria no puede ser nulo o vacío.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La carrea, tipo o categoria no puede ser nulo o vacío.");
        }

        List<VistaPropuesta> vistaPropuestas = this.vistaPropuestaRepository.findByTipoCategoria(tipo,categoria,carrera);

        if (vistaPropuestas.isEmpty()) {
            logger.warn("No se encontraron registros de VistaPropuesta con carrera {}, tipo {} y categoria'{}'.", carrera, tipo,categoria);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros con carrera, tipo y categoria proporcionados.");
        }

        logger.info("Se encontraron {} registros de VistaPropuesta con carrera '{}'. tipo {} y categoria {} .", vistaPropuestas.size(), carrera, tipo,categoria);
        return vistaPropuestas;
    }

    @Override
    public List<VistaPropuesta> buscarPropuestaPorTema(String tema, String carrera) {
        if (tema ==null || tema.isEmpty()|| carrera == null || carrera.isEmpty()) {
            logger.warn("La carrea o tema no puede ser nulo o vacío.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La carrea o tema no puede ser nulo o vacío.");
        }

        List<VistaPropuesta> vistaPropuestas = this.vistaPropuestaRepository.findByTema(tema,carrera);

        if (vistaPropuestas.isEmpty()) {
            logger.warn("No se encontraron registros de VistaPropuesta con carrera {} y tema'{}'.", carrera, tema);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros con carrera y categoria proporcionados.");
        }

        logger.info("Se encontraron {} registros de VistaPropuesta con carrera '{}' y tema {} .", vistaPropuestas.size(), carrera, tema);
        return vistaPropuestas;
    }

    @Override
    public List<VistaPropuesta> buscarPropuestaPorTutor(Integer idUsuario, String facultad) {
        if (idUsuario == null || idUsuario <= 0 || facultad == null || facultad.isEmpty()) {
            logger.warn("La facultad no puede ser nulo o vacío y el idUsuario para tutor no puede ser nullo o negativo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La facultad no puede ser nulo o vacío y el idUsuario para tutor no puede ser nullo o negativo.");
        }

        List<VistaPropuesta> vistaPropuestas = this.vistaPropuestaRepository.findByTutor(idUsuario,facultad);

        if (vistaPropuestas.isEmpty()) {
            logger.warn("No se encontraron registros de VistaPropuesta con facultad {} e idUsuario para tutor'{}'.", facultad, idUsuario);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros con carrera e idTutor proporcionados.");
        }

        logger.info("Se encontraron {} registros de VistaPropuesta con facultad '{}' e idUsuario para tutor{} .", vistaPropuestas.size(), facultad, idUsuario);
        return vistaPropuestas;
    }

    @Override
    public List<VistaPropuesta> buscarPropuestaPorTutorEstadoAprobacion(Integer idUsuario, Integer estadoAprobacion) {
        if (idUsuario == null || idUsuario <= 0 ) {
            logger.warn("La facultad no puede ser nulo o vacío y el idUsuario para tutor no puede ser nullo o negativo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La facultad no puede ser nulo o vacío y el idUsuario para tutor no puede ser nullo o negativo.");
        }
        if (estadoAprobacion < 0  || estadoAprobacion >=3) {
            logger.warn("La carrea no puede ser nulo o vacío y el estado de validacion es 0, 1 o 2.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La carrea no puede ser nulo o vacío y el estado de aprobacion es 0, 1 o 2.");
        }
        EstadoAprobacion aprobacion = null;

        if(estadoAprobacion == 0){
            aprobacion = EstadoAprobacion.NO_APROBADO;
        }
        if(estadoAprobacion == 1){
            aprobacion = EstadoAprobacion.EN_REVISON;
        }

        if(estadoAprobacion == 2){
            aprobacion = EstadoAprobacion.APROBADO;
        }

        List<VistaPropuesta> vistaPropuestas = this.vistaPropuestaRepository.findByTutorEstadoAprobacion(idUsuario, aprobacion);

        if (vistaPropuestas.isEmpty()) {
            logger.warn("No se encontraron registros de VistaPropuesta con  idUsuario para tutor {} y estado aprobación {}'.", idUsuario, aprobacion);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron registros con carrera e idTutor proporcionados.");
        }

        logger.info("Se encontraron {} registros de VistaPropuesta con idUsuario para tutor {} y estado aprobación {}.", vistaPropuestas.size(),  idUsuario, aprobacion);
        return vistaPropuestas;
    }

    @Override
    public List<VistaPropuesta> buscarPropuestaPorRevisor(Integer idUsuario, String facultad) {
        if (idUsuario == null || idUsuario <= 0 || facultad == null || facultad.isEmpty()) {
            logger.warn("La facultad no puede ser nulo o vacío y el idUsuario para revisor no puede ser nullo o negativo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La facultad no puede ser nulo o vacío y el idUsuario para revisor no puede ser nullo o negativo.");
        }

        List<VistaPropuesta> vistaPropuestas = this.vistaPropuestaRepository.findByRevisor(idUsuario,facultad);

        if (vistaPropuestas.isEmpty()) {
            logger.warn("No se encontraron registros de VistaPropuesta con facultad {} e idUsuario para revisor'{}'.", facultad, idUsuario);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se encontraron registros con carrera e idUsuario para revisor proporcionados.");
        }

        logger.info("Se encontraron {} registros de VistaPropuesta con facultad '{}' e idUsuario para revisor {} .", vistaPropuestas.size(), facultad, idUsuario);
        return vistaPropuestas;
    }

    @Override
    public List<VistaPropuesta> buscarPropuestaPorEstudiante(Integer idUsuario) {
        if (idUsuario == null || idUsuario <= 0) {
            logger.warn("El idUsuario para revisor no puede ser nullo o negativo.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El idUsuario para revisor no puede ser nullo o negativo.");
        }

        List<VistaPropuesta> vistaPropuestas = this.vistaPropuestaRepository.findByEstudiante(idUsuario);

        if (vistaPropuestas.isEmpty()) {
            logger.warn("No se encontraron registros de VistaPropuesta idUsuario para revisor'{}'.", idUsuario);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se encontraron registros con carrera e idUsuario para revisor proporcionados.");
        }

        logger.info("Se encontraron {} registros de VistaPropuesta e idUsuario para revisor {} .", vistaPropuestas.size(), idUsuario);
        return vistaPropuestas;
    }

}
