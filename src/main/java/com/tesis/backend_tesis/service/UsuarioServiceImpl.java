package com.tesis.backend_tesis.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tesis.backend_tesis.clients.AutenticacionRestClient;
import com.tesis.backend_tesis.clients.CorreoRestClient;
import com.tesis.backend_tesis.repository.*;
import com.tesis.backend_tesis.repository.modelo.Carrera;
import com.tesis.backend_tesis.repository.modelo.Usuario;
import com.tesis.backend_tesis.repository.modelo.UsuarioRol;
import com.tesis.backend_tesis.service.dto.*;
import com.tesis.backend_tesis.service.dto.utils.Converter;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements IUsuarioService{

    private static final Logger logger = LogManager.getLogger(UsuarioServiceImpl.class);


    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private Converter converter;

    @Autowired
    private IUsuarioRolRepository usuarioRolRepository;

    @Autowired
    private AutenticacionRestClient autenticacionRestClient;

    @Autowired
    private IEncriptionService encriptionService;

    @Autowired
    private IEstudianteRepository estudianteRepository;

    @Autowired
    private ISecretariaRepository secretariaRepository;

    @Autowired
    private IDocenteRepository docenteRepository;

    @Autowired
    private CorreoRestClient correoRestClient;

    @Autowired
    private ICarreraRepository carreraRepository;




    @Override
    public UsuarioDTO insertar(UsuarioDTO usuarioDTO) {

        try {
            if (usuarioDTO == null) {
                logger.warn("Intento de insertar un usuario NULL.");
                return null;
            }

            // Convertir DTO a Entidad
            Usuario usuario = this.converter.toEntity(usuarioDTO);



            // Verificar si el correo ya existe
            if (this.usuarioRepository.existeUsuarioConEmail(usuario.getCorreo())) {
                logger.warn("No se pudo insertar el usuario, el correo {} ya está registrado.", usuario.getCorreo());
                return null;
            }

            // Guardar usuario
            this.usuarioRepository.insert(usuario);
            logger.info("Usuario con correo {} insertado correctamente.", usuario.getCorreo());
            System.out.println("service de usuario insertado correctamente."+this.usuarioRepository.insert(usuario));
            UsuarioDTO usuarioDTOSalida = this.converter.toDTO(usuario);
            return usuarioDTOSalida;

        } catch (Exception e) {
            logger.error("Error al insertar usuario con correo {}: {}", usuarioDTO.getCorreo(), e.getMessage(), e);
            throw new RuntimeException("Error al insertar usuario con correo: " + usuarioDTO.getCorreo(), e);
        }
    }

    @Override
    @Transactional
    public Boolean activarCuenta(String token, String password) {


        String correoAutenticado =  null;
        //System.out.println(correoAutenticado);
        try {
            correoAutenticado = this.autenticacionRestClient.validarToken(token).getBody();
        } catch (FeignException ex) {
            String errorMessage = "Error al validar el token.";
            try {
                String responseBody = ex.contentUTF8();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                errorMessage = jsonNode.get("error").asText();
            } catch (Exception e) {
                logger.error("No se pudo extraer el mensaje de error del servicio de autenticación", e);
            }
            logger.error("Error al validar el token: {}", errorMessage);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, errorMessage);
        }



        Usuario usuario = this.usuarioRepository.buscarPorEmail(correoAutenticado);

        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no existe");

        }

        List<UsuarioRol> usuarioRoles=this.usuarioRolRepository.findByIdUsuario(usuario.getId());

        if (password == null || password.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "la password no puede ser vacia o nula");

        }

        if (usuarioRoles.isEmpty()) {
            throw new RuntimeException("El usuario no tiene roles asignados.");
        }

        List<String> rolesDisponibles = usuarioRoles.stream()
                .map(rol -> rol.getRol().getNombre())
                .collect(Collectors.toList());


        switch (rolesDisponibles.get(0)) {
            case "estudiante":

                usuario.setCorreoValido(Boolean.TRUE);

                break;

            case "docente":

                usuario.setCorreoValido(Boolean.TRUE);
                usuario.setPassword(this.encriptionService.encriptPass(password.trim()));
                usuario.setActivo(Boolean.TRUE);

                break;

            case "secretaria":

                usuario.setCorreoValido(Boolean.TRUE);
                usuario.setPassword(this.encriptionService.encriptPass(password.trim()));
                usuario.setActivo(Boolean.TRUE);
                break;

            case "direccion":

                usuario.setCorreoValido(Boolean.TRUE);
                usuario.setPassword(this.encriptionService.encriptPass(password).trim());
                usuario.setActivo(Boolean.TRUE);
                break;


            default:
                throw new RuntimeException("Rol desconocido: " + rolesDisponibles.get(0));
        }

        // Llamada a actualizar el usuario
        Usuario usuarioActualizado = this.usuarioRepository.actualizar(usuario);

        // Verificar si la actualización se realizó correctamente
        return usuarioActualizado != null;

    }

    @Override
    public Boolean actulizarContrasena(String correo, String password) {

        if (password == null || password.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "la password no puede ser vacia o nula");

        }

        Usuario usuario = this.usuarioRepository.buscarPorEmail(correo.trim());

        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no existe");

        }

        usuario.setPassword(this.encriptionService.encriptPass(password.trim()));

        // Llamada a actualizar el usuario
        Usuario usuarioActualizado = this.usuarioRepository.actualizar(usuario);

        // Verificar si la actualización se realizó correctamente
        return usuarioActualizado != null;
    }

    @Override
    public Boolean recuperarCuenta(String correo) {

        Usuario usuario = this.usuarioRepository.buscarPorEmail(correo.trim());

        if (usuario==null) {
            logger.error("El correo no existe : {}", correo);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El correo no registrado.");
        }

        String token = null;
        try {
            token = this.autenticacionRestClient.crearToken(usuario.getCorreo()).getBody();
        } catch (FeignException.Conflict ex) {
            logger.error("Error al generar token para recuperar cuenta: {}", correo, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al generar token para recuperar cuenta.");
        }

        String usarioARecuperar = usuario.getPrimerNombre() + " " + usuario.getPrimerApellido();
        String enlace = "http://localhost:4200/vista-verificacion-correo/" + token;

        try {
            this.correoRestClient.recuperacionCuentav2(usuario.getCorreo(), usarioARecuperar, enlace,
                    "fing.direccion.computacion@uce.edu.ec");
        } catch (FeignException.Conflict ex) {
            logger.error("Error al enviar el correo de recuperacion de cuenta: {}", usuario.getCorreo(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el correo de validación.");
        }


        return true;
    }

    @Override
    public Boolean recuperarContrasena( String password, String token) {
        String correoAutenticado =  null;
        //System.out.println(correoAutenticado);
        try {
            correoAutenticado = this.autenticacionRestClient.validarToken(token).getBody();
        } catch (FeignException ex) {
            String errorMessage = "Error al validar el token.";
            try {
                String responseBody = ex.contentUTF8();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                errorMessage = jsonNode.get("error").asText();
            } catch (Exception e) {
                logger.error("No se pudo extraer el mensaje de error del servicio de autenticación", e);
            }
            logger.error("Error al validar el token: {}", errorMessage);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, errorMessage);
        }


        if (password == null || password.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "la password no puede ser vacia o nula");

        }

        Usuario usuario = this.usuarioRepository.buscarPorEmail(correoAutenticado);

        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no existe");

        }
        usuario.setPassword(this.encriptionService.encriptPass(password.trim()));

        // Llamada a actualizar el usuario
        Usuario usuarioActualizado = this.usuarioRepository.actualizar(usuario);

        // Verificar si la actualización se realizó correctamente
        return usuarioActualizado != null;

    }

    @Override
    public UsuarioDTO buscarPorId(Integer idUsuario) {
        try {
            if (idUsuario <= 0) {
                logger.warn("El id no puede ser 0 o negativo.");
                return null;
            }

            Usuario usuario = this.usuarioRepository.findById(idUsuario);
            UsuarioDTO usuarioDTO = this.converter.toDTO(usuario);

            logger.info("Estudiante recuperado con IDUSUARIO {}  correctamente.", usuarioDTO.getId());
            return usuarioDTO;

        } catch (Exception e) {
            logger.error("Error al buscar estudiante con IDUSUARIO {}: {}", idUsuario, e.getMessage(), e);
            throw new RuntimeException("Error al buscar estudiante con IDUSUARIO: " + idUsuario, e);
        }
    }

    @Override
    public Boolean activarDesactivarCuenta(Integer id, Boolean accion) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del usuario no puede ser nulo.");
        }

        if (accion == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La acción de activación o desactivación no puede ser nula.");
        }

        Usuario usuario = this.usuarioRepository.findById(id);
        if (usuario == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado.");
        }

        Boolean respuesta = this.usuarioRepository.activarDesactivarUsuario(id, accion);

        if (!respuesta){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al activarDesactivarCuenta .");
        }

        return respuesta;
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 */2 * * * ?")
    public void eliminarUsuariosNoVerificadosCarrera() {
        // Obtener usuarios no verificados
        List<Usuario> usuariosNoVerificados = this.usuarioRepository.findUsuariosNoVerificadosAntesDe();

        for (Usuario usuario : usuariosNoVerificados) {
            if (ChronoUnit.MINUTES.between(usuario.getFechaCreacion(), LocalDateTime.now()) > 2) {
                try {

                    // Eliminar la referencia en Carrera si existe
                    Carrera carreraActualizar = this.carreraRepository.findByIdDireccion(usuario.getId());
                    if (carreraActualizar != null) {
                        carreraActualizar.setUsuario(null);
                        this.carreraRepository.update(carreraActualizar); // Guardar cambio
                        System.out.println("Carrera actualizada: " + carreraActualizar);
                    }

                    System.out.println("Usuario en Carrera con ID " + usuario.getId() + " eliminado.");

                } catch (Exception e) {
                    System.err.println("Error eliminando usuario en Carrera con ID  " + usuario.getId() + ": " + e.getMessage());
                }
            }
        }

        System.out.println("Proceso de eliminación de usuarios en Carrera no verificados completado.");

    }

    @Override
    @Transactional
    @Scheduled(cron = "5 */2 * * * ?") // Se ejecuta cada 2 minutos
    public void eliminarUsuariosNoVerificados() {

        // Obtener usuarios no verificados
        List<Usuario> usuariosNoVerificados = this.usuarioRepository.findUsuariosNoVerificadosAntesDe();

        for (Usuario usuario : usuariosNoVerificados) {
            if (ChronoUnit.MINUTES.between(usuario.getFechaCreacion(), LocalDateTime.now()) > 2) {
                try {

                    // Eliminar referencias en otras tablas antes de eliminar el usuario
                    this.usuarioRolRepository.deleteUsuarioRolbyIdUsuario(usuario.getId());
                    this.estudianteRepository.deleteEstudianteByIdUsuario(usuario.getId());
                    this.docenteRepository.deleteDocenteByIdUsuario(usuario.getId());
                    this.secretariaRepository.deleteSecretariaByIdUsuario(usuario.getId());

                    // Eliminar el usuario
                    this.usuarioRepository.deleteUsuario(usuario.getId());
                    System.out.println("Usuario con ID " + usuario.getId() + " eliminado.");

                } catch (Exception e) {
                    System.err.println("Error eliminando usuario con ID " + usuario.getId() + ": " + e.getMessage());
                }
            }
        }

        System.out.println("Proceso de eliminación de usuarios no verificados completado.");
    }
}
