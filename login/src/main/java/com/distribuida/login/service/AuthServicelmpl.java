package com.distribuida.login.service;

import com.distribuida.login.clients.AdministrativoRestClient;
import com.distribuida.login.clients.EstudianteRestClient;
import com.distribuida.login.repository.ICarreraRepository;
import com.distribuida.login.repository.IUsuarioRepository;
import com.distribuida.login.repository.modelo.AuthResponse;
import com.distribuida.login.repository.modelo.LoginRequest;
import com.distribuida.login.repository.modelo.RegistroRequest;
import com.distribuida.login.repository.modelo.Usuario;
import com.distribuida.login.service.dto.DocenteDTO;
import com.distribuida.login.service.dto.EstudianteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;



@Service
public class AuthServicelmpl implements IAuthService{

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



    public Boolean registroEstudiante(RegistroRequest registroRequest) {
        if (registroRequest == null ||
                registroRequest.getCorreo() == null || registroRequest.getCorreo().isEmpty() ||
                registroRequest.getPassword() == null || registroRequest.getPassword().isEmpty()||
                registroRequest.getIdCarrera() == null ) {
            return false; // Datos inválidos, no se procesa
        }

        if (!esCorreoValido(registroRequest.getCorreo())) {
            return false; // Correo inválido
        }

        if (this.usuarioRepository.existeUsuarioConEmail(registroRequest.getCorreo())) {
            return false; // Usuario ya registrado
        }

        try {
            // Crear usuario
            Usuario usuario = Usuario.builder()
                    .correo(registroRequest.getCorreo())
                    .password(this.encriptionService.encriptPass(registroRequest.getPassword()))
                    .fechaCreacion(LocalDateTime.now())
                    .rol("estudiante")
                    .carrera(this.carreraRepository.findById(registroRequest.getIdCarrera()))
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
            if (Boolean.TRUE.equals(estudianteCreado)) {
                return true; // Éxito en ambas inserciones
            } else {
                throw new RuntimeException("Error al crear el estudiante en el microservicio.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false; // Fallo en el proceso
        }
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

            if (usua.getActivo() != null && usua.getActivo().equals(Boolean.TRUE)){

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
                        .rol(usua.getRol())
                        .idUsuario(usua.getId())
                        .nombreCarrera(usua.getCarrera().getNombre())
                        .activo(usua.getActivo())
                        .build();
            }

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Estudiante no activado.");

        } else if ("docente".equals(rol)) {

            if (usua.getActivo() != null && usua.getActivo().equals(Boolean.TRUE)){

                // Verificar si el estudiante está asociado correctamente
                DocenteDTO docente = this.administrativoRestClient.obtenerDocente(usua.getId());
                if (docente == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado.");
                }

                return AuthResponse.builder()
                        .id(docente.getId())
                        .primerNombre(docente.getPrimerNombre())
                        .segundoNombre(docente.getSegundoNombre())
                        .primerApellido(docente.getPrimerApellido())
                        .segundoApellido(docente.getSegundoApellido())
                        .rol(usua.getRol())
                        .idUsuario(usua.getId())
                        .nombreCarrera(usua.getCarrera().getNombre())
                        .activo(usua.getActivo())
                        .build();
            }

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Estudiante no activado.");

            // Lógica de manejo de docentes si está disponible
        /*
        Docentes doc = this.docentesRepository.findByIdUsuario(usua.getId());
        return AuthResponse.builder()
                .id(doc.getId())
                .primerNombre(doc.getPrimerNombre())
                .segundoNombre(doc.getSegundoNombre())
                .primerApellido(doc.getPrimerApellido())
                .segundoApellido(doc.getSegundoApellido())
                .rol("docente")
                .activo(true)
                .build();
         */

        } else if ("administrativo".equals(rol)) {
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

        } else if ("docente-administrativo".equals(rol)) {

        }

        // Si el rol no es reconocido, lanzar excepción
        throw new RuntimeException("Rol de usuario desconocido.");
    }

    @Override
    public boolean esCorreoValido(String correo) {
        return correo != null && correo.toLowerCase().endsWith("@uce.edu.ec");
    }

    @Override
    public DocenteDTO registroDocente(RegistroRequest registroRequest) {
        if (registroRequest == null ||
                registroRequest.getCorreo() == null || registroRequest.getCorreo().isEmpty() ||
                registroRequest.getPassword() == null || registroRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Los datos del registro son inválidos");
        }

        if (!esCorreoValido(registroRequest.getCorreo())) {
            throw new IllegalArgumentException("El correo proporcionado no es válido");
        }

        if (this.usuarioRepository.existeUsuarioConEmail(registroRequest.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con este correo");
        }

        try {
            // Crear usuario
            Usuario usuario = Usuario.builder()
                    .correo(registroRequest.getCorreo())
                    .password(this.encriptionService.encriptPass(registroRequest.getPassword()))
                    .fechaCreacion(LocalDateTime.now())
                    .rol("docente")
                    .carrera(this.carreraRepository.findById(registroRequest.getIdCarrera()))
                    .activo(Boolean.FALSE)
                    .build();

            Usuario usuarioGuardado = this.usuarioRepository.insertar(usuario);
            if (usuarioGuardado == null || usuarioGuardado.getId() == null) {
                throw new RuntimeException("Error al guardar el usuario Docente");
            }

            // retornar
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
            throw new RuntimeException("Error en el proceso de registro del docente", ex);
        }
    }
}