package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.clients.AutenticacionRestClient;
import com.tesis.backend_tesis.clients.CorreoRestClient;
import com.tesis.backend_tesis.repository.ICarreraRepository;
import com.tesis.backend_tesis.repository.IRolRepository;
import com.tesis.backend_tesis.repository.IUsuarioRepository;
import com.tesis.backend_tesis.repository.IUsuarioRolRepository;
import com.tesis.backend_tesis.repository.modelo.*;
import com.tesis.backend_tesis.service.dto.CarreraDTO;
import com.tesis.backend_tesis.service.dto.DocenteDTO;
import com.tesis.backend_tesis.service.dto.UsuarioDTO;
import com.tesis.backend_tesis.service.dto.utils.Converter;
import feign.FeignException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class CarreraServiceImpl implements ICarreraService {


    private static final Logger logger = LogManager.getLogger(CarreraServiceImpl.class);

    @Autowired
    private AutenticacionRestClient autenticacionRestClient;

    @Autowired
    private ICarreraRepository carreraRepository;

    @Autowired
    private IEncriptionService encriptionService;

    @Autowired
    private IUsuarioRolRepository usuarioRolRepository;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private CorreoRestClient correoRestClient;

    @Autowired
    private Converter converter;

    @Autowired
    private IRolRepository rolRepository;

    @Autowired
    private IDocenteService docenteService;

    @Override
    public Boolean insertar(CarreraRequest carreraRequest) {
        try {
            if (carreraRequest == null) {
                logger.warn("Intento de insertar un usuario NULL.");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CarreraRequest esta vacio.");
            }

            if (carreraRequest.getNombre() == null || carreraRequest.getNombre().isEmpty() ||
                carreraRequest.getIdFacultad() == null){

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todos los datos son oblogatorios.");

            }


            // Convertir DTO a Entidad
            CarreraDTO carreraDTO = CarreraDTO.builder()
                    .nombre(carreraRequest.getNombre())
                    .idFacultad(carreraRequest.getIdFacultad())
                    .build();


            Carrera carrera = this.converter.toEntity(carreraDTO);

            // Verificar si el correo ya existe
            if (this.carreraRepository.existeCarreraPorNombre(carrera.getNombre())) {
                logger.warn("No se pudo insertar la carrera,el nombre {} ya está registrado.", carrera.getNombre());
                throw new ResponseStatusException(HttpStatus.CONFLICT, "La carrea ya está registrada con ese nombre.");
            }

            // Guardar usuario
            Carrera carreraGuardada = this.carreraRepository.insert(carrera);
            logger.info("Usuario con correo {} insertado correctamente en la facultad {}.", carrera.getNombre(),carrera.getFacultad().getNombre());
            System.out.println("service de usuario insertado correctamente."+this.carreraRepository.insert(carrera));
            CarreraDTO carreraDTOSalida = this.converter.toDTO(carrera);
            return carreraGuardada!=null;

        } catch (Exception e) {
            logger.error("Error al insertar usuario con correo {}: {}", carreraRequest.getNombre(), e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error en el registro de carrera.");
        }
    }

    @Override
    public CarreraDTO buscarCarreraPorNombre(String nombre) {
        return null;
    }

    @Override
    public CarreraDTO buscarCarreraPorId(Integer id) {
        try {
            if (id <= 0) {
                logger.warn("El id no puede ser 0 o negativo para buscar una Carrera.");
                return null;
            }

            Carrera carrera = this.carreraRepository.findById(id);
            CarreraDTO carreraDTO = this.converter.toDTO(carrera);

            logger.info("Carrera recuperad con ID {}  correctamente.", carreraDTO.getId());
            return carreraDTO;

        } catch (Exception e) {
            logger.error("Error al buscar Carrera con ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al buscar Carrera con ID: " + id, e);
        }
    }

    @Transactional
    @Override
    public Boolean insertarUsuarioCarrera(RegistroRequest registroRequest) {

            if (registroRequest == null) {
                logger.warn("Intento de insertar un usuarioDireccion en carrera NULL.");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CarreraRequest esta vacio.");
            }

            if (registroRequest.getCorreo() == null || registroRequest.getCorreo().isEmpty() ||
                    registroRequest.getIdCarrera() == null) {

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todos los datos son oblogatorios.");
            }

            Carrera carreraExistente = this.carreraRepository.findById(registroRequest.getIdCarrera());

            if (carreraExistente == null) {
                logger.warn("La carrera con ID {} no existe.", registroRequest.getIdCarrera());
                throw new ResponseStatusException(HttpStatus.CONFLICT, "La carrea ya está registrada con ese nombre.");
            }

            if (this.usuarioRepository.existeUsuarioConEmail(registroRequest.getCorreo())) {
                logger.error("El correo ya está registrado: {}", registroRequest.getCorreo());
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado.");
            }


            //--------------------------------------
            UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                    .primerNombre(carreraExistente.getNombre())
                    .segundoNombre(carreraExistente.getNombre())
                    .primerApellido(carreraExistente.getNombre())
                    .segundoApellido(carreraExistente.getNombre())
                    .correo(registroRequest.getCorreo())
                    .password(this.encriptionService.encriptPass("claveSecreta123"))
                    .fechaCreacion(LocalDateTime.now())
                    .correoValido(Boolean.FALSE)
                    .activo(Boolean.FALSE)
                    .build();

            UsuarioDTO usuarioGuardado = this.usuarioService.insertar(usuarioDTO);

            if (usuarioGuardado == null || usuarioGuardado.getId() == null) {
                logger.error("Error al guardar el usuario con correo: {}", registroRequest.getCorreo());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al guardar el usuario.");
            }


            // Asignar rol según tipo de usuario
            Rol rol = this.rolRepository.findByNombre("dirección");

            Usuario usuarioEntidadGuardado = this.converter.toEntity(usuarioGuardado);

            UsuarioRol usuarioRolEstudiante = new UsuarioRol().builder()
                    .usuario(usuarioEntidadGuardado)
                    .rol(rol)
                    .build();
            UsuarioRol usuarioRolGuaradado = this.usuarioRolRepository.insert(usuarioRolEstudiante);


            if (usuarioRolGuaradado == null || usuarioRolGuaradado.getId() == null) {
                logger.error("Error al guardar el usuario guaradado: {}", usuarioGuardado);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al guardar usuario y rol.");
            }


            //--------------------------------------

            if (carreraExistente.getUsuario() != null) {

                this.usuarioRolRepository.deleteUsuarioRolbyIdUsuario(carreraExistente.getUsuario().getId());
                carreraExistente.setUsuario(null);

            }


            carreraExistente.setUsuario(usuarioEntidadGuardado);

            Carrera actualizada = this.carreraRepository.update(carreraExistente);


        String token = null;
        try {
            token = this.autenticacionRestClient.crearToken(usuarioGuardado.getCorreo()).getBody();
        } catch (FeignException.Conflict ex) {
            logger.error("Error al generar token para validar correo: {}", usuarioGuardado.getCorreo(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al generar token para validar correo.");
        }

        String usuarioCreado = "Dirección de la carrera de " +usuarioGuardado.getPrimerNombre();
        String enlace = "http://localhost:4200/vista-verificacion-correo/" + token;

        try {
            this.correoRestClient.registrarUsuariov2(usuarioGuardado.getCorreo(), usuarioCreado,enlace,
                    carreraExistente.getFacultad().getCorreo() ,"direccion");
        } catch (FeignException.Conflict ex) {
            logger.error("Error al enviar el correo de validación al correo: {}", usuarioGuardado.getCorreo(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el correo de validación.");
        }

        logger.info("Usuario de carrea registrado exitosamente: {}", registroRequest.getCorreo());


            return actualizada != null;

        }

    @Override
    @Transactional
    public Boolean insertarAutoridadesCarrera(Integer idCarrera, Integer idUsuario, String tipo) {

        if (idCarrera == null || idUsuario == null || tipo == null || tipo.isEmpty()) {
            logger.warn("es necesario tener todos los datos.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "todos los datos son obligatorios.");
        }

        Carrera carreraExistente = this.carreraRepository.findById(idCarrera);


        if (carreraExistente == null) {
            logger.warn("La carrera con ID {} no existe.", idCarrera);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrera no existe.");
        }

        DocenteDTO docenteExistenteDTO = this.docenteService.buscarPorIdUsuario(idUsuario);

        if (docenteExistenteDTO == null) {
            logger.warn("El docente con IDUSUARIO {} no existe.", idUsuario);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Docente no existe.");
        }

        Boolean validacion = this.verificarAutoridaesUnicas(carreraExistente, docenteExistenteDTO, tipo);

        if (validacion) {

            return true;
        }


        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede insertar el autoridad.");
    }


    @Transactional
    public Boolean verificarAutoridaesUnicas (Carrera carrera, DocenteDTO docente, String tipo){


        if (tipo.equals("director") ) {


            if (carrera.getCoordinador() !=null ){

                if (carrera.getCoordinador().getId().equals(docente.getId())){
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "el docente no puede ocupar el mismo cargo");
                }

            }



            if (carrera.getDirector()!=null){

                this.usuarioRolRepository.deleteUsuarioRolbyIdUsuarioAndRol(carrera.getDirector().getUsuario().getId(), "director");

            }

            Rol rol = this.rolRepository.findByNombre("director");

            UsuarioRol usuarioRolAutoridades = new UsuarioRol().builder()
                    .usuario(this.usuarioRepository.findById(docente.getIdUsuario()))
                    .rol(rol)
                    .build();
            UsuarioRol usuarioRolGuaradado = this.usuarioRolRepository.insert(usuarioRolAutoridades);

            if (usuarioRolGuaradado == null || usuarioRolGuaradado.getId() == null) {
                logger.error("Error al guardar el rol parq el director: {}", docente.getIdUsuario());
                throw new RuntimeException("Error al guardar la relacion UsuarioRol");
            }

            carrera.setDirector(this.converter.toEntity(docente));

            return true;

        }else{


            if (carrera.getDirector() !=null ){

                if (carrera.getDirector().getId().equals(docente.getId())){
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "el docente no puede ocupar el mismo cargo");
                }

            }


            if (carrera.getCoordinador()!=null){

                this.usuarioRolRepository.deleteUsuarioRolbyIdUsuarioAndRol(carrera.getCoordinador().getUsuario().getId(), "coordinador");

            }

            Rol rol = this.rolRepository.findByNombre("coordinador");

            UsuarioRol usuarioRolAutoridades = new UsuarioRol().builder()
                    .usuario(this.usuarioRepository.findById(docente.getIdUsuario()))
                    .rol(rol)
                    .build();
            UsuarioRol usuarioRolGuaradado = this.usuarioRolRepository.insert(usuarioRolAutoridades);

            if (usuarioRolGuaradado == null || usuarioRolGuaradado.getId() == null) {
                logger.error("Error al guardar el rol parq el director: {}", docente.getIdUsuario());
                throw new RuntimeException("Error al guardar la relacion UsuarioRol");
            }


            carrera.setCoordinador(this.converter.toEntity(docente));

            return true;

        }

    }

}
