package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.clients.AutenticacionRestClient;
import com.tesis.backend_tesis.clients.CorreoRestClient;
import com.tesis.backend_tesis.repository.*;
import com.tesis.backend_tesis.repository.modelo.*;
import com.tesis.backend_tesis.service.dto.*;
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
import java.util.ArrayList;
import java.util.List;
import static com.tesis.backend_tesis.utilitarios.Validaciones.esCorreoValido;


@Service
public class AuthServiceImpl implements IAuthService {

    private static final Logger logger = LogManager.getLogger(AuthServiceImpl.class);


    @Autowired
    private IEncriptionService encriptionService;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private ICarreraRepository carreraRepository;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private CorreoRestClient correoRestClient;

    @Autowired
    private AutenticacionRestClient autenticacionRestClient;

    @Autowired
    private IUsuarioRolRepository usuarioRolRepository;

    @Autowired
    private IEstudianteService estudianteService;

    @Autowired
    private IDocenteService docenteService;

    @Autowired
    private IRolRepository rolRepository;

    @Autowired
    private ISecretariaService secretariaService;

    @Autowired
    private ICarreraService carreraService;

    @Autowired
    private IFacultadService facultadService;

    @Autowired
    private IVistasEntidadesService vistasEntidadesService;



    @Autowired
    private Converter converter;



    @Transactional
    @Override
    public Boolean registroNuevoEstudiante(RegistroRequest registroRequest) {
        if (registroRequest == null ||
                registroRequest.getCorreo() == null || registroRequest.getCorreo().isEmpty() ||
                registroRequest.getPassword() == null || registroRequest.getPassword().isEmpty() ||
                registroRequest.getIdCarrera() == null) {
            logger.error("Registro inválido. Datos faltantes: {}", registroRequest);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Los datos del registro son inválidos. Verifique correo, contraseña e ID de carrera.");
        }

        if (!esCorreoValido(registroRequest.getCorreo())) {
            logger.error("Correo no válido: {}", registroRequest.getCorreo());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo no es válido.");
        }

        if (this.usuarioRepository.existeUsuarioConEmail(registroRequest.getCorreo())) {
            logger.error("El correo ya está registrado: {}", registroRequest.getCorreo());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado.");
        }

        if (this.usuarioRepository.existeUsuarioConCedula(registroRequest.getCedula())) {
            logger.error("La cédula ya está registrada: {}", registroRequest.getCedula());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La cédula ya está registrada en otro usuario.");
        }

        CarreraDTO carreraDTO = this.converter.toDTO(this.carreraRepository.findById(registroRequest.getIdCarrera()));
        if (carreraDTO == null) {
            logger.error("Carrera no encontrada: {}", registroRequest.getIdCarrera());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La carrera seleccionada no existe.");
        }

        // Crear usuario
        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .primerNombre(registroRequest.getPrimerNombre())
                .segundoNombre(registroRequest.getSegundoNombre())
                .primerApellido(registroRequest.getPrimerApellido())
                .segundoApellido(registroRequest.getSegundoApellido())
                .cedula(registroRequest.getCedula())
                .correo(registroRequest.getCorreo())
                .password(this.encriptionService.encriptPass(registroRequest.getPassword()))
                .fechaCreacion(LocalDateTime.now())
                .correoValido(Boolean.FALSE)
                .activo(Boolean.FALSE)
                .build();


        UsuarioDTO usuarioGuardado = this.usuarioService.insertar(usuarioDTO);


        if (usuarioGuardado == null || usuarioGuardado.getId() == null) {
            logger.error("Error al guardar el usuario con correo: {}", registroRequest.getCorreo());
            throw new RuntimeException("Error al guardar el usuario");
        }

        Rol rol = this.rolRepository.findByNombre("estudiante");

        UsuarioRol usuarioRolEstudiante = new UsuarioRol().builder()
                .usuario(this.converter.toEntity(usuarioGuardado))
                .rol(rol)
                .build();
        UsuarioRol usuarioRolGuaradado = this.usuarioRolRepository.insert(usuarioRolEstudiante);

        if (usuarioRolGuaradado == null || usuarioRolGuaradado.getId() == null) {
            logger.error("Error al guardar el usuario guaradado: {}", usuarioGuardado);
            throw new RuntimeException("Error al guardar la relacion UsuarioRol");
        }


        // Crear estudiante
        EstudianteDTO estudianteDTO = EstudianteDTO.builder()
                .idUsuario(usuarioGuardado.getId())
                .idCarrera(registroRequest.getIdCarrera())
                .build();

        EstudianteDTO estudianteGuardado = this.estudianteService.insertar(estudianteDTO);

        if (estudianteGuardado == null || estudianteGuardado.getId() == null) {
            logger.error("Error al guardar el estudiante con ID de usuario: {}", usuarioDTO.getId());
            throw new RuntimeException("Error al guardar el estudiante.");
        }

        String token = null;
        try {
            token = this.autenticacionRestClient.crearToken(usuarioGuardado.getCorreo()).getBody();
        } catch (FeignException.Conflict ex) {
            logger.error("Error al generar token para validar correo: {}", usuarioGuardado.getCorreo(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al generar token para validar correo.");
        }

        String usuarioCreado = registroRequest.getPrimerNombre() + " " + registroRequest.getPrimerApellido();
        String enlace = "http://localhost:4200/vista-verificacion-correo/" + token;

        try {
            this.correoRestClient.registrarUsuario(usuarioCreado, usuarioGuardado.getCorreo(), enlace,
                    "fing.direccion.computacion@uce.edu.ec", "estudiante");
        } catch (FeignException.Conflict ex) {
            logger.error("Error al enviar el correo de validación al correo: {}", usuarioGuardado.getCorreo(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el correo de validación.");
        }

        logger.info("Estudiante registrado exitosamente: {}", registroRequest.getCorreo());
        return Boolean.TRUE;
    }


    @Transactional
    @Override
    public Boolean registroNuevoUsuario(RegistroRequest registroRequest) {
        if (registroRequest == null ||
                registroRequest.getCorreo() == null || registroRequest.getCorreo().isEmpty() ||
                registroRequest.getPassword() == null || registroRequest.getPassword().isEmpty() ||
                registroRequest.getTipoUsuario() == null) {
            logger.error("Registro inválido. Datos faltantes: {}", registroRequest);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Los datos del registro son inválidos. Verifique correo, contraseña y tipo de usuario.");
        }
/*
        if (!esCorreoValido(registroRequest.getCorreo().trim())) {
            logger.error("Correo no válido: {}", registroRequest.getCorreo());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo no es válido.");
        }
 */

        if (this.usuarioRepository.existeUsuarioConEmail(registroRequest.getCorreo().trim())) {
            logger.error("El correo ya está registrado: {}", registroRequest.getCorreo());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado.");
        }

        if (this.usuarioRepository.existeUsuarioConCedula(registroRequest.getCedula().trim())) {
            logger.error("La cédula ya está registrada: {}", registroRequest.getCedula());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La cédula ya está registrada en otro usuario.");
        }

        // Verificar datos adicionales según el tipo de usuario
        if (registroRequest.getTipoUsuario().equalsIgnoreCase("estudiante") && registroRequest.getIdCarrera() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID de carrera es obligatorio para estudiantes.");
        }

        if (registroRequest.getTipoUsuario().equalsIgnoreCase("secretaria") && registroRequest.getIdCarrera() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID de carrera es obligatorio para secretaria.");
        }

        if (registroRequest.getTipoUsuario().equalsIgnoreCase("docente") && registroRequest.getIdFacultad() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID de facultad es obligatorio para docentes.");
        }

        // Crear usuario base
        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .primerNombre(registroRequest.getPrimerNombre().trim())
                .segundoNombre(registroRequest.getSegundoNombre().trim())
                .primerApellido(registroRequest.getPrimerApellido().trim())
                .segundoApellido(registroRequest.getSegundoApellido().trim())
                .cedula(registroRequest.getCedula().trim())
                .celular(registroRequest.getCelular().trim())
                .correo(registroRequest.getCorreo().trim())
                .password(this.encriptionService.encriptPass(registroRequest.getPassword()))
                .fechaCreacion(LocalDateTime.now())
                .correoValido(Boolean.FALSE)
                .activo(Boolean.FALSE)
                .build();

        UsuarioDTO usuarioGuardado = this.usuarioService.insertar(usuarioDTO);

        if (usuarioGuardado == null || usuarioGuardado.getId() == null) {
            logger.error("Error al guardar el usuario con correo: {}", registroRequest.getCorreo());
            throw new RuntimeException("Error al guardar el usuario");
        }

        // Asignar rol según tipo de usuario
        String nombreRol = registroRequest.getTipoUsuario().toLowerCase(); // estudiante, docente, secretaria, directiva
        Rol rol = this.rolRepository.findByNombre(nombreRol);

        UsuarioRol usuarioRol = UsuarioRol.builder()
                .usuario(this.converter.toEntity(usuarioGuardado))
                .rol(rol)
                .build();

        UsuarioRol usuarioRolGuardado = this.usuarioRolRepository.insert(usuarioRol);

        if (usuarioRolGuardado == null || usuarioRolGuardado.getId() == null) {
            logger.error("Error al guardar el rol para el usuario: {}", usuarioGuardado.getCorreo());
            throw new RuntimeException("Error al guardar la relación UsuarioRol");
        }

        Integer idCarrera = registroRequest.getIdCarrera();

        if (idCarrera!=null &&this.carreraService.buscarCarreraPorId(idCarrera)==null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No existe carrera con id: " + idCarrera);

        }


        // Creación de entidades específicas según el tipo de usuario
        switch (registroRequest.getTipoUsuario().toLowerCase()) {
            case "estudiante":
                EstudianteDTO estudianteDTO = EstudianteDTO.builder()
                        .idUsuario(usuarioGuardado.getId())
                        .idCarrera(idCarrera)
                        .build();
                if (this.estudianteService.insertar(estudianteDTO) == null) {
                    throw new RuntimeException("Error al guardar el estudiante.");
                }
                break;

            case "docente":

                DocenteDTO docenteDTO = DocenteDTO.builder()
                        .idUsuario(usuarioGuardado.getId())
                        .idFacultad(registroRequest.getIdFacultad())
                        .build();
                if (this.docenteService.insertar(docenteDTO) == null) {
                    throw new RuntimeException("Error al guardar el docente.");
                }
                break;

            case "secretaria":
                SecretariaDTO secretariaDTO = SecretariaDTO.builder()
                        .idUsuario(usuarioGuardado.getId())
                        .idCarrera(idCarrera)
                        .build();
                if (this.secretariaService.insertar(secretariaDTO) == null) {
                    throw new RuntimeException("Error al guardar la secretaria.");
                }
                break;

            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de usuario no válido.");
        }

        // Generación y envío del correo de verificación
        String token;
        try {
            token = this.autenticacionRestClient.crearToken(usuarioGuardado.getCorreo()).getBody();
        } catch (FeignException.Conflict ex) {
            logger.error("Error al generar token para validar correo: {}", usuarioGuardado.getCorreo(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al generar token para validar correo.");
        }

        String usuarioCreado = registroRequest.getPrimerNombre() + " " + registroRequest.getPrimerApellido();
        //String enlace = "http://localhost:4200/vista-verificacion-correo/" + token;
        String enlace = "http://localhost:8080/API/tesis/usuario/activar/"+ nombreRol+ "/"+token;

        String direccion= null;

        if(registroRequest.getIdCarrera()!=null && registroRequest.getIdCarrera()>=1){

            direccion = this.facultadService.buscarFacultadPorId(registroRequest.getIdFacultad()).getCorreo();
        }

        if (registroRequest.getIdFacultad()!=null && registroRequest.getIdFacultad()>=1){
            direccion = this.carreraRepository.findById(idCarrera).getUsuario().getCorreo();

        }

        System.out.print( direccion );

        try {
            this.correoRestClient.registrarUsuariov2(usuarioGuardado.getCorreo(), usuarioCreado,enlace,
                   direccion ,registroRequest.getTipoUsuario().toLowerCase());
        } catch (FeignException.Conflict ex) {
            logger.error("Error al enviar el correo de validación al correo: {}", usuarioGuardado.getCorreo(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el correo de validación.");
        }

        logger.info("{} registrado exitosamente: {}", registroRequest.getTipoUsuario(), registroRequest.getCorreo());
        return Boolean.TRUE;
    }

  
    @Override
    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        if (loginRequest == null ||
                loginRequest.getCorreo() == null || loginRequest.getCorreo().isEmpty() ||
                loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            logger.error("Login inválido. Datos faltantes: {}", loginRequest);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Los datos del login son inválidos. Verifique correo y contraseña.");
        }



        if (!esCorreoValido(loginRequest.getCorreo())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El correo no es institucional.");
        }

        Usuario usua = this.usuarioRepository.buscarPorEmail(loginRequest.getCorreo().trim());

        if (usua == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no está registrado.");
        }

        if (!this.encriptionService.verificarEncriptedText(usua.getPassword(), loginRequest.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas.");
        }

        if (!Boolean.TRUE.equals(usua.getActivo())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario no activado.");
        }

        if (!Boolean.TRUE.equals(usua.getCorreoValido())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Correo no validado.");
        }

        // Recuperar los roles del usuario
        UsuarioRol rolUsuario = this.usuarioRolRepository.findByIdUsuario(usua.getId()).getFirst();

        if (rolUsuario == null) {
            throw new RuntimeException("El usuario no tiene rol asignados.");
        }

        String nombreFacultad = null;
        String nombreCarrera = null;
        Integer idCarrera = null;
        Integer idFacultad = null;


        switch (rolUsuario.getRol().getNombre()) {
            case "estudiante":
                EstudianteDTO estudianteDTO = this.estudianteService.buscarPorIdUsuario(usua.getId());
                if (estudianteDTO == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado.");
                }

                CarreraDTO carreraEstudiante = this.carreraService.buscarCarreraPorId(estudianteDTO.getIdCarrera());
                nombreFacultad = this.facultadService.buscarFacultadPorId(carreraEstudiante.getIdFacultad()).getNombre();
                nombreCarrera = carreraEstudiante.getNombre();
                idCarrera=carreraEstudiante.getId();
                idFacultad=carreraEstudiante.getIdFacultad();
                break;

            case "docente":
                DocenteDTO docenteDTO = this.docenteService.buscarPorIdUsuario(usua.getId());
                if (docenteDTO == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Docente no encontrado.");
                }

                nombreFacultad = this.facultadService.buscarFacultadPorId(docenteDTO.getIdFacultad()).getNombre();
                nombreCarrera = "MultiCarrera";
                idFacultad=docenteDTO.getIdFacultad();
                break;

            case "secretaria":
                SecretariaDTO secretariaDTO = this.secretariaService.buscarPorIdUsuario(usua.getId());
                if (secretariaDTO == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Secretaria no encontrada.");
                }

                CarreraDTO carreraSec = this.carreraService.buscarCarreraPorId(secretariaDTO.getIdCarrera());
                nombreFacultad = this.facultadService.buscarFacultadPorId(carreraSec.getIdFacultad()).getNombre();
                nombreCarrera = carreraSec.getNombre();
                idCarrera=carreraSec.getId();
                idFacultad=carreraSec.getIdFacultad();
                break;

            case "direccion":
                CarreraDTO carreraDTO = this.carreraService.buscarPorIDUsuario(usua.getId());
                if (carreraDTO == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario direccion no encontrado.");
                }

                nombreFacultad = this.facultadService.buscarFacultadPorId(carreraDTO.getIdFacultad()).getNombre();
                nombreCarrera = carreraDTO.getNombre();
                idCarrera=carreraDTO.getId();
                idFacultad=carreraDTO.getIdFacultad();
                break;

            case "coordinador":
                DocenteDTO coordinadorDTO = this.docenteService.buscarPorIdUsuario(usua.getId());
                if (coordinadorDTO == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Docente no encontrado.");
                }

                nombreFacultad = this.facultadService.buscarFacultadPorId(coordinadorDTO.getIdFacultad()).getNombre();
                nombreCarrera = "MultiCarrera";
                idCarrera=coordinadorDTO.getId();
                idFacultad=coordinadorDTO.getIdFacultad();
                break;


            case "ADMIN":
                break;


            default:
                throw new RuntimeException("Rol desconocido: " + rolUsuario);
        }

        List<String> r = new ArrayList<>();
        r.add(rolUsuario.getRol().getNombre());

        String tokenSesion = null;

        try {
            tokenSesion=this.autenticacionRestClient.crearTokenSesion(usua.getCorreo(), r).getBody();

            System.out.println("token seseion:----" + tokenSesion);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al generar el token de sesion.");
        }


        return AuthResponse.builder()
                .primerNombre(usua.getPrimerNombre())
                .segundoNombre(usua.getSegundoNombre())
                .primerApellido(usua.getPrimerApellido())
                .segundoApellido(usua.getSegundoApellido())
                .correo(usua.getCorreo())
                .rol(rolUsuario.getRol().getNombre())
                .idUsuario(usua.getId())
                .nombreCarrera(nombreCarrera)
                .idCarrera(idCarrera)
                .nombreFacultad(nombreFacultad)
                .idFacultad(idFacultad)
                .activo(usua.getActivo())
                .validdo(usua.getCorreoValido())
                .token(tokenSesion)
                .build();
    }

    

}