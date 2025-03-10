package com.distribuida.alumno.controller;

import com.distribuida.alumno.bucket.IBucket;
import com.distribuida.alumno.clients.AdministrativoRestClient;
import com.distribuida.alumno.clients.CorreoRestClient;
import com.distribuida.alumno.clients.LoginRestClient;
import com.distribuida.alumno.repository.IEstudianteRepository;
import com.distribuida.alumno.repository.modelo.Archivo;
import com.distribuida.alumno.repository.modelo.EstadoValidacion;
import com.distribuida.alumno.repository.modelo.Estudiante;
import com.distribuida.alumno.repository.modelo.Propuesta;
import com.distribuida.alumno.service.IArchivoService;
import com.distribuida.alumno.service.IEstudianteService;
import com.distribuida.alumno.service.IPropuestaService;
import com.distribuida.alumno.service.dto.EstudianteDTO;
import com.distribuida.alumno.service.dto.IEstudianteMapper;
import com.distribuida.alumno.service.dto.UsuarioDTO;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import java.io.InputStream;
import java.time.LocalDateTime;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@CrossOrigin
@RequestMapping(path = "/estudiantes")
public class EstudianteRestFullController {


    @Autowired
    private IEstudianteService estudianteService;

    @Autowired
    private IEstudianteRepository estudianteRepository;

    @Autowired
    private IArchivoService archivoService;

    @Autowired
    private IPropuestaService propuestaService;

    @Autowired
    private IEstudianteMapper estudianteMapper;

    @Autowired
    private LoginRestClient loginRestClient;

    @Autowired
    private AdministrativoRestClient administrativoRestClient;

    @Autowired
    private CorreoRestClient correoRestClient;

    @Autowired
    private IBucket bucketDataSource;


    @PostMapping("/crear")
    public ResponseEntity<Boolean> guardarEstudiante(@RequestBody EstudianteDTO estudianteDTO) {
        if (estudianteDTO == null) {
            return ResponseEntity.badRequest().body(false);
        }
/*
        // Verificar si el estudiante ya existe por su cédula
        if (this.estudianteService.existeEstudiante(estudianteDTO.getCedula())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(false); // Retorna un conflicto si ya existe
        }

 */

        Estudiante estudiante = this.estudianteMapper.toEntity(estudianteDTO);
        estudiante = this.estudianteService.insertar(estudiante);

        if (estudiante == null) {
            return ResponseEntity.badRequest().body(false);
        }

        return ResponseEntity.ok(true);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteDTO> obtenerEstudiante(@PathVariable Integer id) {
        Estudiante estudiante = estudianteService.buscarPorIdUsuario(id);
        if (estudiante == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estudianteMapper.toDTO(estudiante));
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<EstudianteDTO> obtenerEstudiantePorCedula(@PathVariable String cedula) {
        Estudiante estudiante = estudianteService.buscarPorCedula(cedula);
        if (estudiante == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estudianteMapper.toDTO(estudiante));
    }

    @Transactional
    @PostMapping(value = "/propuesta", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> cargarPropuesta(@RequestParam("tema") String tema,
                                                  @RequestParam("idEstuCreacion") Integer idEstuCreacion,
                                                  @RequestParam("correoEstudiantePrimero") String correoEstudiantePrimero,
                                                  @RequestParam(value = "correoEstudianteSegundo", required = false) String correoEstudianteSegundo,
                                                  @RequestParam(value = "correoEstudianteTercero", required = false) String correoEstudianteTercero,
                                                  @RequestParam(value = "iDDocenteTutor", required = false) Integer iDDocenteTutor,
                                                  @RequestParam("file") MultipartFile archivo) throws IOException {

        Logger logger = LoggerFactory.getLogger(getClass());



        // 1. Validar si el archivo está presente
        if (Objects.isNull(archivo) || archivo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha seleccionado ningún archivo.");
        }

        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se permiten archivos en formato PDF.");
        }

        // 2. Obtener el estudiante principal (obligatorio)

        Estudiante estudiantePrimero= null;
        try {
            estudiantePrimero = this.estudianteService.buscarPorId(idEstuCreacion);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estudiante principal no existe.");
        } catch (Exception e) {
            logger.error("Error inesperado al obtener el estudiante con ID {}: {}", idEstuCreacion, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno al buscar el estudiante principal.");
        }


        // 3. Validar que los correos sean correctos y distintos entre sí
        if (!this.estudianteService.esCorreoValido(correoEstudiantePrimero)) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo del estudiante principal debe tener el dominio @uce.edu.ec.");
        }

        if (correoEstudianteSegundo != null && !this.estudianteService.esCorreoValido(correoEstudianteSegundo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo del segundo estudiante debe tener el dominio @uce.edu.ec.");
        }

        if (correoEstudianteTercero != null && !this.estudianteService.esCorreoValido(correoEstudianteTercero)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo del tercer estudiante debe tener el dominio @uce.edu.ec.");
        }

        // Validar que los correos no se repitan
        if ((correoEstudianteSegundo != null && correoEstudiantePrimero.equalsIgnoreCase(correoEstudianteSegundo)) ||
                (correoEstudianteTercero != null && correoEstudiantePrimero.equalsIgnoreCase(correoEstudianteTercero)) ||
                (correoEstudianteSegundo != null && correoEstudianteTercero != null && correoEstudianteSegundo.equalsIgnoreCase(correoEstudianteTercero))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los estudaintes no deben ser los mismos.");
        }

        // 4. Validar que los estudiantes son de diferentes carreras usando loginRestClient
        UsuarioDTO usuarioPrimero = this.loginRestClient.existeUsuarioPorCorreo(correoEstudiantePrimero);
        if (usuarioPrimero == null || !usuarioPrimero.getActivo() || !"estudiante".equals(usuarioPrimero.getRol())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estudiante principal con correo " + correoEstudiantePrimero + " no existe, no está activo o no tiene rol de estudiante.");

        }
        logger.info("Estudiante principal: Correo: {}, ID Carrera: {}", correoEstudiantePrimero, usuarioPrimero.getIDCarrera());

        UsuarioDTO usuarioSegundo = null;
        if (correoEstudianteSegundo != null) {
            usuarioSegundo = this.loginRestClient.existeUsuarioPorCorreo(correoEstudianteSegundo);
            if (usuarioSegundo == null || !usuarioSegundo.getActivo() || !"estudiante".equals(usuarioSegundo.getRol())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El segundo estudiante con correo " + correoEstudianteSegundo + " no existe, no está activo o no tiene rol de estudiante.");
            }
            logger.info("Segundo estudiante: Correo: {}, ID Carrera: {}", correoEstudianteSegundo, usuarioSegundo.getIDCarrera());
        }

        UsuarioDTO usuarioTercero = null;
        if (correoEstudianteTercero != null) {
            usuarioTercero = this.loginRestClient.existeUsuarioPorCorreo(correoEstudianteTercero);
            if (usuarioTercero == null || !usuarioTercero.getActivo() || !"estudiante".equals(usuarioTercero.getRol())) {
                return ResponseEntity.badRequest().body("El tercer estudiante con correo " + correoEstudianteTercero + " no existe, no está activo o no tiene rol de estudiante.");
            }
            logger.info("Tercer estudiante: Correo: {}, ID Carrera: {}", correoEstudianteTercero, usuarioTercero.getIDCarrera());
        }


        if (usuarioPrimero != null) {
            estudiantePrimero = this.estudianteService.buscarPorIdUsuario(usuarioPrimero.getId());
            if (estudiantePrimero != null && !this.propuestaService.puedeEnviarPropuestas(estudiantePrimero.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El primer estudiante tiene propuestas vigentes.");
            }
        }

        Estudiante estudianteSegundo = null;
        String nombreSegundo = "";
        if (usuarioSegundo != null) {
            estudianteSegundo = this.estudianteService.buscarPorIdUsuario(usuarioSegundo.getId());
            if (estudianteSegundo != null && !this.propuestaService.puedeEnviarPropuestas(estudianteSegundo.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El segundo estudiante tiene propuestas vigentes.");
            } else if (estudianteSegundo != null) {
                // Recuperar el nombre completo del segundo estudiante
                nombreSegundo = estudianteSegundo.getPrimerNombre() + " " + estudianteSegundo.getPrimerApellido();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El segundo estudiante no fue encontrado.");
            }
        }

        Estudiante estudianteTercero = null;
        String nombreTercero = "";
        if (usuarioTercero != null) {
            estudianteTercero = this.estudianteService.buscarPorIdUsuario(usuarioTercero.getId());
            if (estudianteTercero != null && !this.propuestaService.puedeEnviarPropuestas(estudianteTercero.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tercer estudiante tiene propuestas vigentes.");
            } else if (estudianteTercero != null) {
                // Recuperar el nombre completo del tercer estudiante
                nombreTercero = estudianteTercero.getPrimerNombre() + " " + estudianteTercero.getPrimerApellido();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El tercer estudiante no fue encontrado.");
            }
        }




        Integer idDocenteTutor = null;
        if (iDDocenteTutor != null) {
            try {
                idDocenteTutor = this.administrativoRestClient.obtenerDocente(iDDocenteTutor).getId();
            } catch (Exception e) {
                logger.error("No se pudo obtener el docente con ID {}: {}", iDDocenteTutor, e.getMessage(), e);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El docente tutor no existe.");
            }
        }



        // 5. Guardar el archivo
        Archivo ar = archivoService.guardar(archivo, idEstuCreacion, "estudiante");
        if (Objects.isNull(ar)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el archivo.");
        }




        // 6. Construir la propuesta
        Propuesta propuesta = Propuesta.builder()
                .tema(tema)
                .primerEstudiante(estudiantePrimero)
                .segundoEstudiante(usuarioSegundo != null ? this.estudianteService.buscarPorIdUsuario(usuarioSegundo.getId()) : null)
                .tercerEstudiante(usuarioTercero != null ? this.estudianteService.buscarPorIdUsuario(usuarioTercero.getId()) : null)
                .archivoPropuesta(ar)
                .idDocenteTutor(iDDocenteTutor!= null ? idDocenteTutor : null)
                .observacion("")
                .validacion(EstadoValidacion.NO_REVISADO)
                .periodo("2024")
                .idEstuCreacion(estudiantePrimero.getId())
                .fechaEnvio(LocalDateTime.now())
                .estadoAprobacion(Boolean.FALSE)
                .build();

        // 7. Guardar la propuesta en base de datos
        this.propuestaService.guardar(propuesta);
        logger.info("La propuesta se ha guardado correctamente: {}", propuesta);

        List<String> ccEmails = new ArrayList<>();
        Stream.of(correoEstudianteSegundo, correoEstudianteTercero)
                .filter(Objects::nonNull) // Filtra solo los que no son null
                .forEach(ccEmails::add);



        String nombresSecundarios = Stream.of(nombreSegundo, nombreTercero)
                .filter(Objects::nonNull)  // Filtra solo los que no son null
                .collect(Collectors.joining(" y "));

        String nombrePrincipal = estudiantePrimero.getPrimerNombre() + " " + estudiantePrimero.getPrimerApellido() + ", ";

        String nombres = nombrePrincipal.concat(nombresSecundarios);





        try {
            this.correoRestClient.enviareachivo(correoEstudiantePrimero, ccEmails, nombres, tema, "fing.direccion.computacion@uce.edu.ec", archivo);
            logger.info("Correo enviado exitosamente a {}", correoEstudiantePrimero);
        } catch (Exception e) {
            logger.error("Error al enviar correo: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el correo.");
        }
        // 8. Respuesta exitosa
        return ResponseEntity.ok("Propuesta cargada exitosamente: " + ar.getNombre());
    }




// no va

    @GetMapping("/estudiante/{idUsuario}")
    public ResponseEntity<Estudiante> obtenerestudainte(@PathVariable  Integer idUsuario) {

        System.out.println(this.estudianteService.buscarPorIdUsuario(idUsuario));
        return ResponseEntity.ok().body(this.estudianteRepository.findByIdUsuario(idUsuario));

    }

    @GetMapping("/existe")
    public ResponseEntity<Boolean> existencia(@RequestParam String cedula) {

        // Verifica si el docente con la cédula proporcionada existe
        Boolean existe = this.estudianteService.existeEstudiante(cedula);


        // Si el docente no existe, retorna false con un estado 404 Not Found
        return ResponseEntity.ok(existe);
    }

    @PostMapping("/eliminar/{idUsuario}")
    public void eliminarRegistro(@PathVariable Integer idUsuario) {

        this.estudianteRepository.delete(idUsuario);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileName) throws IOException {
        return this.bucketDataSource.downloadFile(fileName);
    }


}
