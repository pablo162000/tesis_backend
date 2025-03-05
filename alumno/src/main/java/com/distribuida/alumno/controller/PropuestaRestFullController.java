package com.distribuida.alumno.controller;


import com.distribuida.alumno.repository.modelo.Archivo;
import com.distribuida.alumno.repository.modelo.Propuesta;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    public ResponseEntity<Boolean> validarPropuesta(@PathVariable Integer idPropuesta, @RequestParam("respuesta") Boolean respuesta ) {

        Boolean exito = this.propuestaService.validarPropuesta(idPropuesta, respuesta);

        if (Boolean.TRUE.equals(exito)) {
            return ResponseEntity.ok(Boolean.TRUE); // Retorna un HTTP 200 con true si fue exitoso
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Boolean.FALSE); // Retorna un HTTP 400 con false si falló
        }
    }

    @GetMapping
    public ResponseEntity<List<PropuestaDTO>> obtenerPropuestas() {

        List<PropuestaDTO> propuestas = this.propuestaService.buscarTodaspropuestas();

        System.out.println(propuestas); // Para depuración

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
                                                    @RequestParam("tipoRevisor") String tipoRevisor
                                                    ) {

        if (Objects.isNull(idPropuesta)) {
            return ResponseEntity.badRequest().body("El ID de la propuesta no puede ser nulo.");
        }

        if (Objects.isNull(idDocente)) {
            return ResponseEntity.badRequest().body("El ID de la propuesta no puede ser nulo.");
        }

        if (Objects.isNull(tipoRevisor)) {
            return ResponseEntity.badRequest().body("El tiporevisor no puede ser nulo.");
        }


        Boolean exito = this.propuestaService.asignarRevisor(idPropuesta, idDocente, tipoRevisor);

        if (Boolean.TRUE.equals(exito)) {
            return ResponseEntity.ok("Se asigno el revisor"); // Retorna un HTTP 200 con true si fue exitoso
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no se pudoa asignar el revisor"); // Retorna un HTTP 400 con false si falló
        }
    }


    @PutMapping(value = "/cargararchivorevisores", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> cargarArchivoRevisores(@RequestParam("idPropuesta") Integer idPropuesta,
                                                         @RequestParam("idAdministrativo") Integer idAdministrativo,
                                                         @RequestParam("file") MultipartFile archivo) throws IOException
     {

        if (Objects.isNull(idPropuesta)) {
            return ResponseEntity.badRequest().body("El ID de la propuesta no puede ser nulo.");
        }

        if (Objects.isNull(idAdministrativo)) {
            return ResponseEntity.badRequest().body("El ID de la propuesta no puede ser nulo.");
        }

        if (Objects.isNull(archivo) || archivo.isEmpty()) {
             return ResponseEntity.badRequest().body("No se ha seleccionado ningún archivo.");
        }

        Propuesta propuestaExistente = this.propuestaService.buscarPorId(idPropuesta);

         if (propuestaExistente == null) {
             return ResponseEntity.badRequest().body("No se ha encontrado la propuesta.");
         }


         // Guardar archivo con el ID del revisor
         Archivo archivoGuardado = this.archivoService.guardar(archivo, idAdministrativo, "administrativo");

         propuestaExistente.setArchivoDesignacionRevisores(archivoGuardado);

         Boolean exito = this.propuestaService.actualizar(propuestaExistente);

        if (Boolean.TRUE.equals(exito)) {
            return ResponseEntity.ok("Se asigno el archivo de designacion de revisores"); // Retorna un HTTP 200 con true si fue exitoso
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no se pudoa asignar el archivo de designacion de revisores"); // Retorna un HTTP 400 con false si falló
        }
    }





    @PutMapping(value = "/calificacion", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> calificarPropuesta (@RequestParam("idPropuesta") Integer idPropuesta,
                                                      @RequestParam("nota") Double nota,
                                                      @RequestParam("tipoRevisor") String tipoRevisor,
                                                      @RequestParam("observaciones") String observaciones,
                                                      @RequestParam("file") MultipartFile archivo) throws IOException {

        if (Objects.isNull(idPropuesta)) {
            return ResponseEntity.badRequest().body("El ID de la propuesta no puede ser nulo.");
        }

        if (Objects.isNull(nota) || Double.isNaN(nota)) {
            return ResponseEntity.badRequest().body("La nota es inválida.");
        }

        Propuesta propuestaExistente = this.propuestaService.buscarPorId(idPropuesta);

        if (propuestaExistente == null) {
            return ResponseEntity.badRequest().body("No se ha encontrado la propuesta.");
        }

        if (Objects.isNull(archivo) || archivo.isEmpty()) {
            return ResponseEntity.badRequest().body("No se ha seleccionado ningún archivo.");
        }


        // Determinar el ID del revisor según el tipo de revisor
        Integer idRevisor = null;
        if ("primer".equalsIgnoreCase(tipoRevisor)) {
            idRevisor = propuestaExistente.getIdDocentePrimerRevisor();
        } else if ("segundo".equalsIgnoreCase(tipoRevisor)) {
            idRevisor = propuestaExistente.getIdDocenteSegundoRevisor();
        } else {
            return ResponseEntity.badRequest().body("El tipo de revisor debe ser 'primer' o 'segundo'.");
        }

        // Verificar que el ID del revisor no sea nulo
        if (Objects.isNull(idRevisor) ) {
            return ResponseEntity.badRequest().body("No se encontró el revisor correspondiente en la propuesta.");
        }

        // Guardar archivo con el ID del revisor
        Archivo archivoGuardado = this.archivoService.guardar(archivo, idRevisor, "administrativo");

        if (archivoGuardado == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el archivo.");
        }

        boolean cambioRealizado = false;
        LocalDateTime fechaActual = LocalDateTime.now();

        // Asignar nota, fecha y archivo según el revisor
        if ("primer".equalsIgnoreCase(tipoRevisor)) {
            propuestaExistente.setNotaPrimerRevisor(nota);
            propuestaExistente.setFechaPrimerRevisor(fechaActual);
            propuestaExistente.setArchivoRubricaPrimerRevisor(archivoGuardado);
            propuestaExistente.setObservacionDocentePrimerRevisor(observaciones);
            cambioRealizado = true;
        } else if ("segundo".equalsIgnoreCase(tipoRevisor)) {
            propuestaExistente.setNotaSegundoRevisor(nota);
            propuestaExistente.setFechaSegundoRevisor(fechaActual);
            propuestaExistente.setArchivoRubricaSegundoRevisor(archivoGuardado);
            propuestaExistente.setObservacionDocenteSegundoRevisor(observaciones);
            cambioRealizado = true;
        }

        // Guardar cambios en la base de datos solo si hubo un cambio
        if (cambioRealizado) {
            this.propuestaService.actualizar(propuestaExistente);
            return ResponseEntity.ok("Calificación registrada correctamente.");
        } else {
            return ResponseEntity.badRequest().body("No se realizaron cambios en la calificación.");
        }
    }



    @PutMapping(value = "/aprobacion", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> aproobacion (@RequestParam("idPropuesta") Integer idPropuesta,
                                               @RequestParam("idDirector") Integer idDirector,
                                               @RequestParam("file") MultipartFile archivo) throws IOException {

        if (Objects.isNull(idPropuesta)) {
            return ResponseEntity.badRequest().body("El ID de la propuesta no puede ser nulo.");
        }

        Propuesta propuestaExistente = this.propuestaService.buscarPorId(idPropuesta);

        if (propuestaExistente == null) {
            return ResponseEntity.badRequest().body("No se ha encontrado la propuesta.");
        }

        if (archivo == null || archivo.isEmpty()) {
            return ResponseEntity.badRequest().body("No se ha seleccionado ningún archivo.");
        }


        // Validaciones de notas de revisores
        if (Objects.isNull(propuestaExistente.getNotaPrimerRevisor())) {
            return ResponseEntity.badRequest().body("El primer revisor aún no ha calificado.");
        }

        if (Objects.isNull(propuestaExistente.getNotaSegundoRevisor())) {
            return ResponseEntity.badRequest().body("El segundo revisor aún no ha calificado.");
        }

        Double notaFinal =  Double.sum(propuestaExistente.getNotaPrimerRevisor(),propuestaExistente.getNotaSegundoRevisor())/2.0;

        if (notaFinal <= Double.valueOf(11)){

            return ResponseEntity.badRequest().body("El promedio simple de las calificaciones es menor a 11, se obtuvo:" + notaFinal );

        }

        // Guardar archivo con el ID del revisor
        Archivo archivoGuardado = this.archivoService.guardar(archivo, idDirector, "administrativo");


        LocalDateTime fechaActual = LocalDateTime.now();

        // Actualización del estado de aprobación
        propuestaExistente.setEstadoAprobacion(Boolean.TRUE);
        propuestaExistente.setFechaAprobacion(fechaActual);
        propuestaExistente.setArchivoDesignacionTutor(archivoGuardado);
        this.propuestaService.actualizar(propuestaExistente);

        return ResponseEntity.ok("Aprobación registrada correctamente.");

        }


    }
