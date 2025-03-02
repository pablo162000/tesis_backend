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

        if (correoEstudianteSegundo != null) {
            if (!this.estudianteService.esCorreoValido(correoEstudianteSegundo)) {
                return ResponseEntity.badRequest().body("El correo del segundo estudiante debe tener el dominio @uce.edu.ec.");
            }
            if (correoEstudiantePrimero.equalsIgnoreCase(correoEstudianteSegundo)) {
                return ResponseEntity.badRequest().body("El correo del estudiante principal no puede ser igual al del segundo estudiante.");
            }
        }

        if (correoEstudianteTercero != null) {
            if (!this.estudianteService.esCorreoValido(correoEstudianteTercero)) {
                return ResponseEntity.badRequest().body("El correo del tercer estudiante debe tener el dominio @uce.edu.ec.");
            }
            if (correoEstudiantePrimero.equalsIgnoreCase(correoEstudianteTercero)) {
                return ResponseEntity.badRequest().body("El correo del estudiante principal no puede ser igual al del tercer estudiante.");
            }
        }

        if (correoEstudianteSegundo != null && correoEstudianteTercero != null && correoEstudianteSegundo.equalsIgnoreCase(correoEstudianteTercero)) {
            return ResponseEntity.badRequest().body("Los correos electrónicos de los estudiantes opcionales no pueden ser iguales.");
        }

        // 4. Validar que los estudiantes son de diferentes carreras usando loginRestClient

        UsuarioDTO usuarioPrimero = null;
        UsuarioDTO usuarioSegundo = null;
        if (correoEstudianteSegundo != null) {
            usuarioSegundo = this.loginRestClient.existeUsuarioPorCorreo(correoEstudianteSegundo);
            usuarioPrimero = this.loginRestClient.existeUsuarioPorCorreo(correoEstudiantePrimero);
            if (usuarioSegundo == null) {
                return ResponseEntity.badRequest().body("El segundo estudiante con correo " + correoEstudianteSegundo + " no existe.");
            }
            // Verificar que el segundo estudiante pertenece a una carrera diferente
            Estudiante estudianteSegundo = this.estudianteService.buscarPorIdUsuario(usuarioSegundo.getId());
            if (estudianteSegundo == null) {
                return ResponseEntity.badRequest().body("El segundo estudiante no existe.");
            }

            if (usuarioPrimero.getIDCarrera().equals(usuarioSegundo.getIDCarrera())) {
                return ResponseEntity.badRequest().body("El segundo estudiante debe pertenecer a una carrera diferente.");
            }
        }

        UsuarioDTO usuarioTercero = null;
        if (correoEstudianteTercero != null) {
            usuarioTercero = this.loginRestClient.existeUsuarioPorCorreo(correoEstudianteTercero);
            if (usuarioTercero == null) {
                return ResponseEntity.badRequest().body("El tercer estudiante con correo " + correoEstudianteTercero + " no existe.");
            }
            // Verificar que el tercer estudiante pertenece a una carrera diferente
            Estudiante estudianteTercero = this.estudianteService.buscarPorIdUsuario(usuarioTercero.getId());
            if (estudianteTercero == null) {
                return ResponseEntity.badRequest().body("El tercer estudiante no existe.");
            }
            if (usuarioPrimero.getIDCarrera().equals(usuarioTercero.getIDCarrera())) {
                return ResponseEntity.badRequest().body("El tercer estudiante debe pertenecer a una carrera diferente.");
            }
        }

        // 5. Guardar el archivo
        Archivo ar = archivoService.guardar(archivo, idEstuCreacion);
        if (ar == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el archivo.");
        }

        // 6. Construir la propuesta
        Propuesta propuesta = Propuesta.builder()
                .tema(tema)
                .primerEstudiante(estudiantePrimero)  // Estudiante obligatorio
                .segundoEstudiante(
                        usuarioSegundo != null && usuarioSegundo.getId() != null
                                ? this.estudianteService.buscarPorIdUsuario(usuarioSegundo.getId())
                                : null)  // Opcional
                .tercerEstudiante(
                        usuarioTercero != null && usuarioTercero.getId() != null
                                ? this.estudianteService.buscarPorIdUsuario(usuarioTercero.getId())
                                : null)  // Opcional
                .archivoPropuesta(ar)
                .idDocenteTutor(iDDocenteTutor)
                .observacion("")
                .validacion(EstadoValidacion.NO_REVISADO)
                .periodo("2024")
                .idEstuCreacion(estudiantePrimero.getId())
                .fechaEnvio(LocalDateTime.now())
                .estadoAprobación(Boolean.FALSE)
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

}
