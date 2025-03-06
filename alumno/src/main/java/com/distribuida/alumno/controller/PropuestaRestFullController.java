package com.distribuida.alumno.controller;


import com.distribuida.alumno.service.IArchivoService;
import com.distribuida.alumno.service.IPropuestaService;
import com.distribuida.alumno.service.dto.PropuestaDTO;
import com.distribuida.alumno.service.dto.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/propuesta")
public class PropuestaRestFullController {

    @Autowired
    private IPropuestaService propuestaService;

    @Autowired
    private IArchivoService archivoService;

    @Autowired
    private Converter converter;



    @PutMapping("/{idPropuesta}/validar")
    public ResponseEntity<Boolean> validarPropuesta(@PathVariable Integer idPropuesta,  @RequestParam("idDocenteDirector")  Integer idDocenteDirector,@RequestParam("respuesta") Boolean respuesta ) {

        Boolean exito = this.propuestaService.validarPropuesta(idPropuesta, idDocenteDirector ,respuesta);

        if (Boolean.TRUE.equals(exito)) {
            return ResponseEntity.ok(Boolean.TRUE); // Retorna un HTTP 200 con true si fue exitoso
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Boolean.FALSE); // Retorna un HTTP 400 con false si falló
        }
    }

    @GetMapping
    public ResponseEntity<List<PropuestaDTO>> obtenerPropuestas() {

        List<PropuestaDTO> propuestas = this.propuestaService.buscarTodaspropuestas();

        //System.out.println(propuestas); // Para depuración

        return ResponseEntity.ok(propuestas);

    }

    @GetMapping("/estudiante/{idEstudiante}")
    public ResponseEntity<?> obtenerPropuestasPorIdEstudiante(@PathVariable Integer idEstudiante) {
        try {
            // Validación del ID del estudiante
            if (idEstudiante == null || idEstudiante <= 0) {
                return ResponseEntity.badRequest().body("El ID del estudiante no es válido.");
            }

            // Buscar las propuestas
            List<PropuestaDTO> propuestas = this.propuestaService.buscarPorIdEstudiante(idEstudiante);

            // Validar si se encontraron propuestas
            if (propuestas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron propuestas para el estudiante con ID " + idEstudiante);
            }

            // Retornar las propuestas encontradas
            return ResponseEntity.ok(propuestas);

        } catch (Exception e) {
            // Manejo de errores inesperados
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error al obtener las propuestas: " + e.getMessage());
        }

    }


    @PutMapping(value = "/asignarrevisores", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> asignarRevisores(@RequestParam("idPropuesta") Integer idPropuesta,
                                                   @RequestParam("idDocente") Integer idDocente,
                                                   @RequestParam("tipoRevisor") String tipoRevisor) {

        // Validaciones en una sola línea
        if (idPropuesta == null || idDocente == null || tipoRevisor == null || tipoRevisor.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todos los campos son obligatorios.");
        }

        Boolean exito = this.propuestaService.asignarRevisor(idPropuesta, idDocente, tipoRevisor);

        if (!exito) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo asignar el revisor.");
        }

        return ResponseEntity.ok("Se asignó el revisor.");
    }



    @PutMapping(value = "/cargararchivorevisores", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> cargarArchivoRevisores(
            @RequestParam("idPropuesta") Integer idPropuesta,
            @RequestParam("idAdministrativo") Integer idAdministrativo,
            @RequestParam("file") MultipartFile archivo)  throws IOException{

        String mensaje = this.propuestaService.procesarArchivoRevisores(idPropuesta, idAdministrativo, archivo);
        return ResponseEntity.ok(mensaje);
    }



    @PutMapping(value = "/calificacion", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> calificarPropuesta(
            @RequestParam("idPropuesta") Integer idPropuesta,
            @RequestParam("nota") Double nota,
            @RequestParam("tipoRevisor") String tipoRevisor,
            @RequestParam("observaciones") String observaciones,
            @RequestParam("file") MultipartFile archivo) {



        try {
            // Llamada al servicio que procesa la calificación
            String mensaje = propuestaService.calificarPropuesta(idPropuesta, nota, tipoRevisor, observaciones, archivo);
            return ResponseEntity.ok(mensaje); // Responde con el mensaje de éxito
        } catch (IOException e) {
            // Este es un caso específico de IO, si ocurre, responde con un error de servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el archivo.");
        }
    }



    @PutMapping(value = "/aprobacion", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> aproobacion (@RequestParam("idPropuesta") Integer idPropuesta,
                                               @RequestParam("idDirector") Integer idDirector,
                                               @RequestParam("observaciones") String observaciones,
                                               @RequestParam("file") MultipartFile archivo) throws IOException {

        String mensaje = propuestaService.aprobarPropuesta(idPropuesta, idDirector,observaciones , archivo);
        return ResponseEntity.ok(mensaje);

        }


    }
