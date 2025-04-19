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

/*
    @Override
    public Integer registroEstudiante(RegistroRequest registroRequest) {
        var flag = 0;
        if (registroRequest.getCorreo() != null && !registroRequest.getCorreo().isEmpty()
                && registroRequest.getPassword() != null && !registroRequest.getPassword().isEmpty()) {
            if (!this.usuariosRepository.existeUsuarioConEmail(registroRequest.getCorreo())) {
                try {

                    Usuarios usua = Usuarios.builder()
                            .username(registroRequest.getPrimerNombre() + ' ' + registroRequest.getPrimerApellido())
                            .correo(registroRequest.getCorreo())
                            .password(this.encriptionService.encriptPass(registroRequest.getPassword()))
                            .fechaCreacion(LocalDateTime.now())//cambie del original
                            .rol("estudiante")
                            .activo(false)
                            .build();
                    Usuarios usuarioGuardado = this.usuariosRepository.insertar(usua);

                    if (usuarioGuardado == null || usuarioGuardado.getId() == null) {
                        throw new RuntimeException("Error al guardar el usuario");
                    }

                    Estudiantes estu = Estudiantes.builder()
                            .primer_nombre(registroRequest.getPrimerNombre())
                            .segundo_nombre(registroRequest.getSegundoNombre())
                            .primer_apellido(registroRequest.getPrimerApellido())
                            .segundo_apellido(registroRequest.getSegundoApellido())
                            .cedula(registroRequest.getCedula())
                            //.activo(registroRequest.getActivo())
                            .usuario(usuarioGuardado)  // ASIGNAR ID DEL USUARIO
                            .build();


                    this.estudiantesRepository.insertar(estu);
                    flag = usua.getId();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return flag;
    }





    public AuthResponse loginUsuario(LoginRequest loginRequest) {
        Usuarios usua = this.usuariosRepository.buscarPorEmail(loginRequest.getCorreo());

        // Validar si el usuario existe
        if (usua == null) {
            throw new RuntimeException("El usuario no está registrado.");
        }

        // Verificar la contraseña encriptada
        if (!this.encriptionService.verificarEncriptedText(usua.getPassword(), loginRequest.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas.");
        }

        // Determinar el rol y obtener la información correspondiente
        String rol = usua.getRol();
        if ("estudiante".equals(rol)) {
            Estudiantes estu = this.estudiantesRepository.findByIdUsuario(usua.getId());

            return AuthResponse.builder()
                    .id(estu.getId())
                    .primer_nombre(estu.getPrimer_nombre())
                    .segundo_nombre(estu.getSegundo_nombre())
                    .primer_apellido(estu.getPrimer_apellido())
                    .segundo_apellido(estu.getSegundo_apellido())
                    .rol("estudiante")
                    .activo(true)
                    .build();

        } else if ("docente".equals(rol)) {
            /*
            Docentes doc = this.docentesRepository.findByIdUsuario(usua.getId());

            return AuthResponse.builder()
                    .id(doc.getId())
                    .primer_nombre(doc.getPrimer_nombre())
                    .segundo_nombre(doc.getSegundo_nombre())
                    .primer_apellido(doc.getPrimer_apellido())
                    .segundo_apellido(doc.getSegundo_apellido())
                    .rol("docente")
                    .activo(true)
                    .build();



        } else if ("administrativo".equals(rol)) {


            Administrativos admin = this.administrativosRepository.findByIdUsuario(usua.getId());

            return AuthResponse.builder()
                    .id(admin.getId())
                    .primer_nombre(admin.getPrimer_nombre())
                    .segundo_nombre(admin.getSegundo_nombre())
                    .primer_apellido(admin.getPrimer_apellido())
                    .segundo_apellido(admin.getSegundo_apellido())
                    .rol("administrativo")
                    .activo(true)
                    .build();


        }

        // Si el rol no es reconocido, lanzar excepción
        throw new RuntimeException("Rol de usuario desconocido.");
    }

    */

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

        if(registroRequest.getIdCarrera()==null || registroRequest.getIdCarrera().describeConstable().isEmpty()){

            direccion = this.facultadService.buscarFacultadPorId(registroRequest.getIdFacultad()).getCorreo();
        }

        if (registroRequest.getIdFacultad()==null || registroRequest.getIdFacultad().describeConstable().isEmpty()){
            direccion = this.carreraRepository.findById(idCarrera).getUsuario().getCorreo();

        }

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

    /*
        @Override
        @Transactional
        public AuthResponse login(LoginRequest loginRequest) {

            if (loginRequest == null ||
                    loginRequest.getCorreo() == null || loginRequest.getCorreo().isEmpty() ||
                    loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                logger.error("Login inválido. Datos faltantes: {}", loginRequest);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Los datos del login son inválidos. Verifique correo, contraseña.");
            }

            Usuario usua = this.usuarioRepository.buscarPorEmail(loginRequest.getCorreo());

            // Validar si el usuario existe
            if (usua == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no está registrado.");
            }
            // Verificar la contraseña encriptada
            if (!this.encriptionService.verificarEncriptedText(usua.getPassword(), loginRequest.getPassword())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas.");
            }

            if (!Boolean.TRUE.equals(usua.getActivo())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario no activado.");
            }

            if (!Boolean.TRUE.equals(usua.getCorreoValido())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Correo no validado.");
            }

            String usuarioRol = this.usuarioRolRepository.findByIdUsuario(usua.getId()).getRol().getNombre();

            String nombreFacultad= null;

            CarreraDTO carreraDTO =null;


            switch (usuarioRol) {
                case "estudiante":

                    EstudianteDTO estudianteRecuperadoDTO = this.estudianteService.buscarPorIdUsuario(usua.getId());
                    if (estudianteRecuperadoDTO == null) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado.");
                    }

                    carreraDTO = this.carreraService.buscarCarreraPorId(estudianteRecuperadoDTO.getIdCarrera());
                    nombreFacultad =  this.facultadService.buscarFacultadPorId(carreraDTO.getIdFacultad()).getNombre();

                    return construirAuthResponse(usua, usuarioRol, carreraDTO.getNombre(), nombreFacultad);


                case "docente":
                    DocenteDTO docenteRecuperadoDTO = this.docenteService.buscarPorIdUsuario(usua.getId());
                    if (docenteRecuperadoDTO == null) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Docente no encontrado.");
                    }

                    nombreFacultad =  this.facultadService.buscarFacultadPorId(docenteRecuperadoDTO.getIdFacultad()).getNombre();

                    return construirAuthResponse(usua, usuarioRol, "MultiCarrera", nombreFacultad);

                case "dirección":

                    return null;

                case "secretaria":

                    SecretariaDTO secretariaRecuperadoDTO = this.secretariaService.buscarPorIdUsuario(usua.getId());
                    if (secretariaRecuperadoDTO == null) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Secretaria no encontrado.");
                    }

                    carreraDTO = this.carreraService.buscarCarreraPorId(secretariaRecuperadoDTO.getIdCarrera());
                    nombreFacultad =  this.facultadService.buscarFacultadPorId(carreraDTO.getIdFacultad()).getNombre();

                    return construirAuthResponse(usua, usuarioRol, carreraDTO.getNombre(), nombreFacultad);

                default:
                    throw new RuntimeException("Rol de usuario desconocido.");
            }

        }

        private AuthResponse construirAuthResponse(Usuario usuario, String rol, String carrera, String facultad) {
            return AuthResponse.builder()
                    .primerNombre(usuario.getPrimerNombre())
                    .segundoNombre(usuario.getSegundoNombre())
                    .primerApellido(usuario.getPrimerApellido())
                    .segundoApellido(usuario.getSegundoApellido())
                    .correo(usuario.getCorreo())
                    .rol(rol)
                    .idUsuario(usuario.getId())
                    .nombreCarrera(carrera)
                    .nombrefacultad(facultad)
                    .activo(usuario.getActivo())
                    .validdo(usuario.getCorreoValido())
                    .build();
        }


     */
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

            case "dirección":
                CarreraDTO carreraDTO = this.carreraService.buscarPorIDUsuario(usua.getId());
                if (carreraDTO == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario dirección no encontrado.");
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

            default:
                throw new RuntimeException("Rol desconocido: " + rolUsuario);
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
                .build();
    }

    /*

    @Override
    @Transactional
    public AuthResponse seleccionarRol(Integer idUsuario, String rolSeleccionado) {
        Usuario usua = this.usuarioRepository.findById(idUsuario);

        if (usua == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.");
        }

        List<UsuarioRol> rolesUsuario = this.usuarioRolRepository.findByIdUsuario(usua.getId());

        boolean tieneRol = rolesUsuario.stream()
                .anyMatch(usuarioRol -> usuarioRol.getRol().getNombre().equalsIgnoreCase(rolSeleccionado));

        if (!tieneRol) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no tiene este rol asignado.");
        }

        String nombreFacultad = null;
        String nombreCarrera = null;

        switch (rolSeleccionado.toLowerCase()) {
            case "estudiante":
                EstudianteDTO estudianteDTO = this.estudianteService.buscarPorIdUsuario(usua.getId());
                if (estudianteDTO == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado.");
                }

                CarreraDTO carreraDTO = this.carreraService.buscarCarreraPorId(estudianteDTO.getIdCarrera());
                nombreFacultad = this.facultadService.buscarFacultadPorId(carreraDTO.getIdFacultad()).getNombre();
                nombreCarrera = carreraDTO.getNombre();
                break;

            case "docente":
                DocenteDTO docenteDTO = this.docenteService.buscarPorIdUsuario(usua.getId());
                if (docenteDTO == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Docente no encontrado.");
                }

                nombreFacultad = this.facultadService.buscarFacultadPorId(docenteDTO.getIdFacultad()).getNombre();
                nombreCarrera = "MultiCarrera";
                break;

            case "secretaria":
                SecretariaDTO secretariaDTO = this.secretariaService.buscarPorIdUsuario(usua.getId());
                if (secretariaDTO == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Secretaria no encontrada.");
                }

                CarreraDTO carreraSec = this.carreraService.buscarCarreraPorId(secretariaDTO.getIdCarrera());
                nombreFacultad = this.facultadService.buscarFacultadPorId(carreraSec.getIdFacultad()).getNombre();
                nombreCarrera = carreraSec.getNombre();
                break;

            case "direccion":
                nombreFacultad = "Administración General";
                nombreCarrera = "Administrativo";
                break;

            default:
                throw new RuntimeException("Rol desconocido: " + rolSeleccionado);
        }

        return AuthResponse.builder()
                .primerNombre(usua.getPrimerNombre())
                .segundoNombre(usua.getSegundoNombre())
                .primerApellido(usua.getPrimerApellido())
                .segundoApellido(usua.getSegundoApellido())
                .correo(usua.getCorreo())
                .rolSeleccionado(rolSeleccionado)
                .idUsuario(usua.getId())
                .nombreCarrera(nombreCarrera)
                .nombreFacultad(nombreFacultad)
                .activo(usua.getActivo())
                .validdo(usua.getCorreoValido())
                .build();
    }

     */

}