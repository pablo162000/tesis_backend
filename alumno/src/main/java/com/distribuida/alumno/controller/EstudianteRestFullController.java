package com.distribuida.alumno.controller;

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
import com.distribuida.alumno.service.dto.PropuestaDTO;
import com.distribuida.alumno.service.dto.UsuarioDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.List;


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


    @PostMapping("/crear")
    public ResponseEntity<Boolean> guardarEstudiante(@RequestBody EstudianteDTO estudianteDTO) {
        if (estudianteDTO == null) {
            return ResponseEntity.badRequest().body(false);
        }

        // Verificar si el estudiante ya existe por su cédula
        if (this.estudianteService.existeEstudiante(estudianteDTO.getCedula())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(false); // Retorna un conflicto si ya existe
        }

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
        if (archivo == null || archivo.isEmpty()) {
            return ResponseEntity.badRequest().body("No se ha seleccionado ningún archivo.");
        }

        // 2. Obtener el estudiante principal (obligatorio)
        Estudiante estudiantePrimero = this.estudianteService.buscarPorId(idEstuCreacion);
        if (estudiantePrimero == null) {
            return ResponseEntity.badRequest().body("El estudiante principal no existe.");
        }

        // 3. Validar que los correos sean correctos y distintos entre sí
        if (!this.estudianteService.esCorreoValido(correoEstudiantePrimero)) {
            return ResponseEntity.badRequest().body("El correo del estudiante principal debe tener el dominio @uce.edu.ec.");
        }

        if (correoEstudianteSegundo != null && !this.estudianteService.esCorreoValido(correoEstudianteSegundo)) {
            return ResponseEntity.badRequest().body("El correo del segundo estudiante debe tener el dominio @uce.edu.ec.");
        }

        if (correoEstudianteTercero != null && !this.estudianteService.esCorreoValido(correoEstudianteTercero)) {
            return ResponseEntity.badRequest().body("El correo del tercer estudiante debe tener el dominio @uce.edu.ec.");
        }

        // Validar que los correos no se repitan
        if ((correoEstudianteSegundo != null && correoEstudiantePrimero.equalsIgnoreCase(correoEstudianteSegundo)) ||
                (correoEstudianteTercero != null && correoEstudiantePrimero.equalsIgnoreCase(correoEstudianteTercero)) ||
                (correoEstudianteSegundo != null && correoEstudianteTercero != null && correoEstudianteSegundo.equalsIgnoreCase(correoEstudianteTercero))) {
            return ResponseEntity.badRequest().body("Los correos de los estudiantes deben ser únicos.");
        }

        // 4. Validar que los estudiantes son de diferentes carreras usando loginRestClient
        UsuarioDTO usuarioPrimero = this.loginRestClient.existeUsuarioPorCorreo(correoEstudiantePrimero);
        if (usuarioPrimero == null || !usuarioPrimero.getActivo() || !"estudiante".equals(usuarioPrimero.getRol())) {
            return ResponseEntity.badRequest().body("El estudiante principal con correo " + correoEstudiantePrimero + " no existe, no está activo o no tiene rol de estudiante.");
        }
        logger.info("Estudiante principal: Correo: {}, ID Carrera: {}", correoEstudiantePrimero, usuarioPrimero.getIDCarrera());

        UsuarioDTO usuarioSegundo = null;
        if (correoEstudianteSegundo != null) {
            usuarioSegundo = this.loginRestClient.existeUsuarioPorCorreo(correoEstudianteSegundo);
            if (usuarioSegundo == null || !usuarioSegundo.getActivo() || !"estudiante".equals(usuarioSegundo.getRol())) {
                return ResponseEntity.badRequest().body("El segundo estudiante con correo " + correoEstudianteSegundo + " no existe, no está activo o no tiene rol de estudiante.");
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

        // Validar que los estudiantes sean de carreras diferentes
        if (usuarioSegundo != null && usuarioPrimero.getIDCarrera().equals(usuarioSegundo.getIDCarrera())) {
            return ResponseEntity.badRequest().body("El segundo estudiante debe pertenecer a una carrera diferente.");
        }

        if (usuarioTercero != null && usuarioPrimero.getIDCarrera().equals(usuarioTercero.getIDCarrera())) {
            return ResponseEntity.badRequest().body("El tercer estudiante debe pertenecer a una carrera diferente.");
        }

        if (usuarioSegundo != null && usuarioTercero != null && usuarioSegundo.getIDCarrera().equals(usuarioTercero.getIDCarrera())) {
            return ResponseEntity.badRequest().body("El segundo y tercer estudiante deben pertenecer a carreras diferentes.");
        }

        // 5. Guardar el archivo
        Archivo ar = archivoService.guardar(archivo, idEstuCreacion, "estudiante");
        if (ar == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el archivo.");
        }

        // 6. Construir la propuesta
        Propuesta propuesta = Propuesta.builder()
                .tema(tema)
                .primerEstudiante(estudiantePrimero)
                .segundoEstudiante(usuarioSegundo != null ? this.estudianteService.buscarPorIdUsuario(usuarioSegundo.getId()) : null)
                .tercerEstudiante(usuarioTercero != null ? this.estudianteService.buscarPorIdUsuario(usuarioTercero.getId()) : null)
                .archivoPropuesta(ar)
                .idDocenteTutor(iDDocenteTutor)
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
        Boolean exito = this.estudianteService.existeEstudiante(cedula);

        // Si el docente existe, retorna true con un estado 200 OK
        if (Boolean.TRUE.equals(exito)) {
            return ResponseEntity.ok(true);
        }

        // Si el docente no existe, retorna false con un estado 404 Not Found
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
    }

}
