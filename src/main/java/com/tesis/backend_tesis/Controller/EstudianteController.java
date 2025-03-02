package com.tesis.backend_tesis.Controller;


import com.tesis.backend_tesis.repository.IPropuestaRepository;
import com.tesis.backend_tesis.repository.IUsuariosRepository;
import com.tesis.backend_tesis.repository.modelo.Archivo;
import com.tesis.backend_tesis.repository.modelo.Estudiantes;
import com.tesis.backend_tesis.repository.modelo.Propuesta;
import com.tesis.backend_tesis.repository.modelo.PropuestaRequest;
import com.tesis.backend_tesis.service.IArchivoService;
import com.tesis.backend_tesis.service.IEstudiantesService;
import com.tesis.backend_tesis.service.IPropuestaService;
import com.tesis.backend_tesis.service.S3Service;
import com.tesis.backend_tesis.service.dto.PropuestaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping(path = "/estudiante")
public class EstudianteController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private IArchivoService archivoService;

    @Autowired
    private IEstudiantesService estudiantesService;

    @Autowired
    private IPropuestaService propuestaService;

    @Autowired
    private IUsuariosRepository usuariosRepository;

    @PostMapping(value = "/propuesta", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("tema") String tema,
                                             @RequestParam("idEstuCreacion") Integer idEstuCreacion,
                                             @RequestParam("correoEstudiantePrimero") String correoEstudiantePrimero,
                                             @RequestParam(value = "correoEstudianteSegundo", required = false) String correoEstudianteSegundo,
                                             @RequestParam(value = "correoEstudianteTercero", required = false) String correoEstudianteTercero,
                                             @RequestParam("file") MultipartFile archivo) throws IOException {
        Logger logger = LoggerFactory.getLogger(getClass());

        // 1. Validar si el archivo está presente
        if (archivo == null || archivo.isEmpty()) {
            return ResponseEntity.badRequest().body("No se ha seleccionado ningún archivo.");
        }

        // 2. Obtener el estudiante principal (obligatorio)
        Estudiantes estudiantePrimero = this.estudiantesService.buscarPorId(idEstuCreacion);
        if (estudiantePrimero == null) {
            return ResponseEntity.badRequest().body("El estudiante principal no existe.");
        }

        // 3. Validar que los correos sean correctos y distintos entre sí
        if (!this.usuariosRepository.esCorreoValido(correoEstudiantePrimero)) {
            return ResponseEntity.badRequest().body("El correo del estudiante principal debe tener el dominio @uce.edu.ec.");
        }

        if (correoEstudianteSegundo != null) {
            if (!this.usuariosRepository.esCorreoValido(correoEstudianteSegundo)) {
                return ResponseEntity.badRequest().body("El correo del segundo estudiante debe tener el dominio @uce.edu.ec.");
            }
            if (correoEstudiantePrimero.equalsIgnoreCase(correoEstudianteSegundo)) {
                return ResponseEntity.badRequest().body("El correo del estudiante principal no puede ser igual al del segundo estudiante.");
            }
        }

        if (correoEstudianteTercero != null) {
            if (!this.usuariosRepository.esCorreoValido(correoEstudianteTercero)) {
                return ResponseEntity.badRequest().body("El correo del tercer estudiante debe tener el dominio @uce.edu.ec.");
            }
            if (correoEstudiantePrimero.equalsIgnoreCase(correoEstudianteTercero)) {
                return ResponseEntity.badRequest().body("El correo del estudiante principal no puede ser igual al del tercer estudiante.");
            }
        }

        if (correoEstudianteSegundo != null && correoEstudianteTercero != null && correoEstudianteSegundo.equalsIgnoreCase(correoEstudianteTercero)) {
            return ResponseEntity.badRequest().body("Los correos electrónicos de los estudiantes opcionales no pueden ser iguales.");
        }

        // 4. Validar y obtener los estudiantes opcionales
        Estudiantes estudianteSegundo = null;
        if (correoEstudianteSegundo != null) {
            estudianteSegundo = this.estudiantesService.existeConEmail(correoEstudianteSegundo);
            if (estudianteSegundo == null) {
                logger.warn("El segundo estudiante con correo {} no existe.", correoEstudianteSegundo);
            }
        }

        Estudiantes estudianteTercero = null;
        if (correoEstudianteTercero != null) {
            estudianteTercero = this.estudiantesService.existeConEmail(correoEstudianteTercero);
            if (estudianteTercero == null) {
                logger.warn("El tercer estudiante con correo {} no existe.", correoEstudianteTercero);
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
                .estudiantePrimero(estudiantePrimero)  // Estudiante obligatorio
                .estudianteSegundo(estudianteSegundo)  // Opcional
                .estudianteTercero(estudianteTercero)  // Opcional
                .archivo(ar)
                .idDocente(1)
                .observacion("")
                .validacion(false)
                .periodo("2024")
                .idEstuCreacion(estudiantePrimero.getId())
                .build();

        // 7. Guardar la propuesta en base de datos
        this.propuestaService.guardar(propuesta);
        logger.info("La propuesta se ha guardado correctamente: {}", propuesta);

        // 8. Respuesta exitosa
        return ResponseEntity.ok("Propuesta cargada exitosamente: " + ar.getNombre());
    }

    @GetMapping("/propuesta/{idEstudiante}")
    public List<PropuestaDTO> obtenerPropuestasPorEstudiante(@PathVariable  Integer idEstudiante) {
        return this.propuestaService.buscarPorIdEstudiante(idEstudiante);
    }


}