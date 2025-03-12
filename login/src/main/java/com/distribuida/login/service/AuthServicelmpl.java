package com.distribuida.login.service;

import com.distribuida.login.clients.AdministrativoRestClient;
import com.distribuida.login.clients.CorreoRestClient;
import com.distribuida.login.clients.EstudianteRestClient;
import com.distribuida.login.repository.ICarreraRepository;
import com.distribuida.login.repository.IUsuarioRepository;
import com.distribuida.login.repository.modelo.*;
import com.distribuida.login.security.JwUtil;
import com.distribuida.login.service.dto.AdministrativoDTO;
import com.distribuida.login.service.dto.DocenteDTO;
import com.distribuida.login.service.dto.EstudianteDTO;

import com.distribuida.login.service.dto.utils.Converter;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import feign.FeignException;
import feign.FeignException.Conflict;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.transaction.annotation.Transactional;


@Service
public class AuthServicelmpl implements IAuthService {

    @Autowired
    private EstudianteRestClient estudianteRestClient;

    @Autowired
    private AdministrativoRestClient administrativoRestClient;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IEncriptionService encriptionService;

    @Autowired
    private ICarreraRepository carreraRepository;

    @Autowired
    private CorreoRestClient correoRestClient;

    @Autowired
    private Converter converter;


    @Transactional
    @Override
    public Boolean registroEstudiante(RegistroRequest registroRequest) {

        // Validaciones básicas
        if (Objects.isNull(registroRequest) ||
                Objects.isNull(registroRequest.getCorreo()) || registroRequest.getCorreo().isEmpty() ||
                Objects.isNull(registroRequest.getPassword()) || registroRequest.getPassword().isEmpty() ||
                Objects.isNull(registroRequest.getIdCarrera())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Los datos del registro son inválidos. Verifique correo, contraseña e ID de carrera.");
        }

        if (!esCorreoValido(registroRequest.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo no es válido.");
        }

        if (this.usuarioRepository.existeUsuarioConEmail(registroRequest.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado.");
        }

        // Validar si la cédula ya está registrada como estudiante o docente
        try {
            if (Boolean.TRUE.equals(this.estudianteRestClient.existencia(registroRequest.getCedula()))) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "La cédula ya está registrada en estudiante.");
            }
        } catch (FeignException.Conflict ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La cédula ya está registrada en estudiante.");
        }

        try {
            if (Boolean.TRUE.equals(this.administrativoRestClient.existencia(registroRequest.getCedula()))) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "La cédula ya está registrada en docente.");
            }
        } catch (FeignException.Conflict ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La cédula ya está registrada en docente.");
        }

        // Obtener carrera sin usar .orElseThrow()
        Carrera carrera = this.carreraRepository.findById(registroRequest.getIdCarrera());
        if (carrera == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La carrera seleccionada no existe.");
        }


        // Crear usuario
        Usuario usuario = Usuario.builder()
                .correo(registroRequest.getCorreo())
                .password(this.encriptionService.encriptPass(registroRequest.getPassword()))
                .fechaCreacion(LocalDateTime.now())
                .rol("estudiante")
                .carrera(carrera)
                .correoValido(Boolean.FALSE)
                .activo(Boolean.FALSE)
                .build();

        Usuario usuarioGuardado = this.usuarioRepository.insertar(usuario);
        if (usuarioGuardado == null || usuarioGuardado.getId() == null) {
            throw new RuntimeException("Error al guardar el usuario");
        }

        // Crear estudiante con el ID del usuario guardado
        EstudianteDTO estudianteDTO = EstudianteDTO.builder()
                .primerNombre(registroRequest.getPrimerNombre())
                .segundoNombre(registroRequest.getSegundoNombre())
                .primerApellido(registroRequest.getPrimerApellido())
                .segundoApellido(registroRequest.getSegundoApellido())
                .cedula(registroRequest.getCedula())
                .celular(registroRequest.getCelular())
                .idUsuario(usuarioGuardado.getId()) // Se asocia el usuario con el estudiante
                .build();

        System.out.println("Datos enviados al servicio REST: " + estudianteDTO);

        // Llamar al servicio REST para registrar al estudiante
        Boolean estudianteCreado = this.estudianteRestClient.crearEstudiante(estudianteDTO);
        if (Boolean.FALSE.equals(estudianteCreado)) {
            throw new RuntimeException("Error al crear el estudiante en el microservicio.");
        }

        // Generar token y enlace de verificación
        String usuarioCreado = registroRequest.getPrimerNombre() + " " + registroRequest.getPrimerApellido();
        String correo = registroRequest.getCorreo();
        String token = JwUtil.generateToken(correo);
        String enlace = "http://localhost:4200/vista-verificacion-correo/" + token;

        // Enviar correo con control de errores (fuera de la transacción)
        try {

            this.correoRestClient.registrarUsuario(usuarioCreado, correo, enlace,
                    "fing.direccion.computacion@uce.edu.ec", "estudiante");

        } catch (Exception e) {
            // Elimina usuario local
            EstudianteDTO porBorrar= this.estudianteRestClient.obtenerEstudiantePorCedula(estudianteDTO.getCedula());
            this.estudianteRestClient.eliminarRegistro(porBorrar.getIdUsuario());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el correo de validación.");
        }

        return true; // Éxito en todo el proceso
    }


    @Override
    public AuthResponse loginUsuario(LoginRequest loginRequest) {
        // Buscar al usuario por correo
        Usuario usua = this.usuarioRepository.buscarPorEmail(loginRequest.getCorreo());

        // Validar si el usuario existe
        if (usua == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no está registrado.");
        }

        // Verificar la contraseña encriptada
        if (!this.encriptionService.verificarEncriptedText(usua.getPassword(), loginRequest.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas.");
        }

        // Si el usuario es válido, determinar el rol y obtener la información correspondiente
        String rol = usua.getRol();

        if ("estudiante".equals(rol)) {

            if (usua.getActivo() != null && usua.getActivo().equals(Boolean.TRUE)) {

                if (usua.getCorreoValido() != null && usua.getCorreoValido().equals(Boolean.TRUE)) {
                    // Verificar si el estudiante está asociado correctamente
                    EstudianteDTO estu = this.estudianteRestClient.obtenerEstudiantePorIdUsuario(usua.getId());
                    if (estu == null) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado.");
                    }

                    return AuthResponse.builder()
                            .id(estu.getId())
                            .primerNombre(estu.getPrimerNombre())
                            .segundoNombre(estu.getSegundoNombre())
                            .primerApellido(estu.getPrimerApellido())
                            .segundoApellido(estu.getSegundoApellido())
                            .correo(usua.getCorreo())
                            .rol(usua.getRol())
                            .idUsuario(usua.getId())
                            .nombreCarrera(usua.getCarrera().getNombre())
                            .activo(usua.getActivo())
                            .build();

                }

                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El correo del estudiante no esta validado.");

            }

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Estudiante no activado.");

        } else if ("docente".equals(rol)) {

            if (usua.getActivo() != null && usua.getActivo().equals(Boolean.TRUE)) {

                if (usua.getCorreoValido() != null && usua.getCorreoValido().equals(Boolean.TRUE)) {

                    DocenteDTO docente = this.administrativoRestClient.obtenerDocentePorIdUsuario(usua.getId());
                    if (docente == null) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Docente no encontrado.");
                    }

                    return AuthResponse.builder()
                            .id(docente.getId())
                            .primerNombre(docente.getPrimerNombre())
                            .segundoNombre(docente.getSegundoNombre())
                            .primerApellido(docente.getPrimerApellido())
                            .segundoApellido(docente.getSegundoApellido())
                            .correo(usua.getCorreo())
                            .rol(usua.getRol())
                            .idUsuario(usua.getId())
                            .nombreCarrera(usua.getCarrera().getNombre())
                            .activo(usua.getActivo())
                            .build();

                }
                // Verificar si el estudiante está asociado correctamente

                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Correo Docente no validado.");


            }

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Docente activado.");

        } else if ("direccion".equals(rol) || "secretaria".equals(rol)) {


            if (usua.getActivo() != null && usua.getActivo().equals(Boolean.TRUE)) {

                if (usua.getCorreoValido() != null && usua.getCorreoValido().equals(Boolean.TRUE)) {

                    AdministrativoDTO administrativoDTO = this.administrativoRestClient.obtenerAdministrativoPorIdUsuario(usua.getId());
                    if (administrativoDTO == null) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado.");
                    }

                    return AuthResponse.builder()
                            .id(administrativoDTO.getId())
                            .primerNombre(administrativoDTO.getPrimerNombre())
                            .segundoNombre(administrativoDTO.getSegundoNombre())
                            .primerApellido(administrativoDTO.getPrimerApellido())
                            .segundoApellido(administrativoDTO.getSegundoApellido())
                            .rol(usua.getRol())
                            .correo(usua.getCorreo())
                            .idUsuario(usua.getId())
                            .nombreCarrera(usua.getCarrera().getNombre())
                            .activo(usua.getActivo())
                            .build();

                }
                // Verificar si el estudiante está asociado correctamente

                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Correo Direccion no validado.");


            }

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Direccion activado.");



            // Lógica de manejo de administrativos si está disponible
        /*
        Administrativos admin = this.administrativosRepository.findByIdUsuario(usua.getId());
        return AuthResponse.builder()
                .id(admin.getId())
                .primerNombre(admin.getPrimerNombre())
                .segundoNombre(admin.getSegundoNombre())
                .primerApellido(admin.getPrimerApellido())
                .segundoApellido(admin.getSegundoApellido())
                .rol("administrativo")
                .activo(true)
                .build();
         */

        } else if ("secretaria".equals(rol)) {

        }

        // Si el rol no es reconocido, lanzar excepción
        throw new RuntimeException("Rol de usuario desconocido.");
    }

    @Override
    public boolean esCorreoValido(String correo) {
        return correo != null && correo.toLowerCase().endsWith("@uce.edu.ec");
    }

    @Override
    @Transactional
    public DocenteDTO registroDocente(RegistroRequest registroRequest) {
        // Verificación de datos nulos o vacíos
        if (registroRequest == null ||
                registroRequest.getCorreo() == null || registroRequest.getCorreo().isEmpty() ||
                registroRequest.getPassword() == null || registroRequest.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los datos del registro son inválidos.");
        }

        // Validación del correo electrónico
        if (!esCorreoValido(registroRequest.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo proporcionado no es válido.");
        }

        // Verificación si ya existe un usuario con el correo
        if (this.usuarioRepository.existeUsuarioConEmail(registroRequest.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario registrado con este correo.");
        }

        try {


            // Crear usuario docente
            Usuario usuario = Usuario.builder()
                    .correo(registroRequest.getCorreo())
                    .password(this.encriptionService.encriptPass(registroRequest.getPassword()))
                    .fechaCreacion(LocalDateTime.now())
                    .rol("docente")
                    .carrera(this.carreraRepository.findById(registroRequest.getIdCarrera()))
                    .activo(Boolean.FALSE)
                    .build();

            // Guardar el usuario
            Usuario usuarioGuardado = this.usuarioRepository.insertar(usuario);
            if (usuarioGuardado == null || usuarioGuardado.getId() == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el usuario Docente.");
            }

            String usuarioCreado = registroRequest.getPrimerNombre() + " " + registroRequest.getPrimerApellido();
            String correo = registroRequest.getCorreo();

            String token = JwUtil.generateToken(correo);

            String enlace = "http://localhost:4200/password-docente/" + token;

            try {
                this.correoRestClient.registrarUsuario(usuarioCreado, correo, enlace, " fing.direccion.computacion@uce.edu.ec", "docente");
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el correo de validación.");
            }


            // Crear y devolver el DTO del docente
            return DocenteDTO.builder()
                    .primerNombre(registroRequest.getPrimerNombre())
                    .segundoNombre(registroRequest.getSegundoNombre())
                    .primerApellido(registroRequest.getPrimerApellido())
                    .segundoApellido(registroRequest.getSegundoApellido())
                    .cedula(registroRequest.getCedula())
                    .celular(registroRequest.getCelular())
                    .idUsuario(usuarioGuardado.getId())
                    .build();

        } catch (Exception ex) {
            // Manejar cualquier otro error inesperado
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el proceso de registro del docente", ex);
        }
    }


    @Override
    @Transactional
    public AdministrativoDTO registroAdministrativo(RegistroAdministrativoRequest registroAdministrativoRequest) {
        // Verificación de datos nulos o vacíos
        if (registroAdministrativoRequest == null ||
                registroAdministrativoRequest.getCorreo() == null || registroAdministrativoRequest.getCorreo().isEmpty() ||
                registroAdministrativoRequest.getPassword() == null || registroAdministrativoRequest.getPassword().isEmpty() ||
                registroAdministrativoRequest.getRol() == null || registroAdministrativoRequest.getRol().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los datos del registro son inválidos.");
        }

        // Validación del correo electrónico
        if (!esCorreoValido(registroAdministrativoRequest.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo proporcionado no es válido.");
        }

        // Verificación si ya existe un usuario con el correo
        if (this.usuarioRepository.existeUsuarioConEmail(registroAdministrativoRequest.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario registrado con este correo.");
        }

        try {
            // Crear usuario administrativo
            Usuario usuario = Usuario.builder()
                    .correo(registroAdministrativoRequest.getCorreo())
                    .password("") // Se encripta la contraseña
                    .fechaCreacion(LocalDateTime.now())
                    .rol(registroAdministrativoRequest.getRol())
                    .carrera(this.carreraRepository.findById(registroAdministrativoRequest.getIdCarrera()))
                    .activo(Boolean.FALSE)
                    .correoValido(Boolean.FALSE)
                    .build();

            // Guardar el usuario
            Usuario usuarioGuardado = this.usuarioRepository.insertar(usuario);
            if (usuarioGuardado == null || usuarioGuardado.getId() == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el usuario administrativo.");
            }

            // Preparar datos para el envío del correo
            String usuarioCreado = registroAdministrativoRequest.getPrimerNombre() + " " + registroAdministrativoRequest.getPrimerApellido();
            String correo = registroAdministrativoRequest.getCorreo();

            String token = JwUtil.generateToken(correo);
            String enlace = "http://localhost:4200/password-docente/" + token;

            System.out.println(enlace);

            System.out.println(correo);

            System.out.println(token);
            // Intentar enviar el correo
            try {
                this.correoRestClient.registrarUsuario(usuarioCreado, correo, enlace, " fing.direccion.computacion@uce.edu.ec", "administrativo");
            } catch (Exception e) {
                // Registrar el error sin detener el flujo

            }

            // Crear y devolver el DTO del administrativo
            return AdministrativoDTO.builder()
                    .primerNombre(registroAdministrativoRequest.getPrimerNombre())
                    .segundoNombre(registroAdministrativoRequest.getSegundoNombre())
                    .primerApellido(registroAdministrativoRequest.getPrimerApellido())
                    .segundoApellido(registroAdministrativoRequest.getSegundoApellido())
                    .cedula(registroAdministrativoRequest.getCedula())
                    .celular(registroAdministrativoRequest.getCelular())
                    .idUsuario(usuarioGuardado.getId())
                    .build();

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el proceso de registro del administrativo", ex);
        }
    }


    @Override
    @Transactional
    public Boolean validarCorreo(String correo) {

        Usuario usuario = this.usuarioRepository.buscarPorEmail(correo);

        if (usuario != null) {

            usuario.setCorreoValido(Boolean.TRUE);

            this.usuarioRepository.actualizar(usuario);

            return Boolean.TRUE;
        }

        return Boolean.FALSE;

    }

    @Override
    @Transactional
    public Boolean validarCorreoDocente(String correo, String password) {

        Usuario usuario = this.usuarioRepository.buscarPorEmail(correo);
        String passwordEncrypt = null;

        if (password != null) {

            passwordEncrypt = this.encriptionService.encriptPass(password);
        }

        if (usuario != null) {

            usuario.setCorreoValido(Boolean.TRUE);
            usuario.setActivo(Boolean.TRUE);
            usuario.setPassword(passwordEncrypt);

            this.usuarioRepository.actualizar(usuario);

            return Boolean.TRUE;
        }

        return Boolean.FALSE;

    }
}