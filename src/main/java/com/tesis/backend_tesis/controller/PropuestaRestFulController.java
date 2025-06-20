package com.tesis.backend_tesis.controller;

import com.tesis.backend_tesis.service.IPropuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(path = "/propuestas")
public class PropuestaRestFulController {

    @Autowired
    private IPropuestaService propuestaService;

    @PreAuthorize("hasRole('estudiante') ")
    @PostMapping
    public ResponseEntity<String> guardarPropuesta(
            @RequestParam String tipo,
            @RequestParam String tema,
            @RequestParam String categoria,
            @RequestParam String primerCorreo,
            @RequestParam(required = false) String segundoCorreo,
            @RequestParam(required = false) String tercerCorreo,
            @RequestParam(required = false) Integer idDocenteTutor,
            @RequestParam MultipartFile archivo) throws IOException {

            String segundoCorreoVal = segundoCorreo != null ? segundoCorreo.trim() : null;
            String tercerCorreoVal = tercerCorreo != null ? tercerCorreo.trim() : null;

            String respuesta = propuestaService.guardar(
                    tipo, tema.trim(), categoria, primerCorreo.trim(), segundoCorreoVal,tercerCorreoVal, idDocenteTutor, archivo);

            return ResponseEntity.ok(respuesta);

    }

    @PreAuthorize("hasAnyRole('direccion', 'coordinador')")
    @PutMapping("/{idPropuesta}/validar")
    //@PreAuthorize("hasRole('direccion')")
    public ResponseEntity<Boolean> validarPropuesta(@PathVariable Integer idPropuesta,
                                                    @RequestParam("respuesta") Boolean respuesta,
                                                    @RequestParam(value="observaciones", required = false) String observaciones,
                                                    @RequestParam(value="idUsuarioSecretaria", required = false) Integer idUsuarioSecretaria,
                                                    @RequestParam("taskID") String taskID) {


        Boolean exito = this.propuestaService.validarPropuesta(idPropuesta,
                respuesta,
                observaciones,  idUsuarioSecretaria, taskID);

        return ResponseEntity.ok(exito); // Retorna un HTTP 200 con true si fue exitoso

    }

    @PreAuthorize("hasRole('secretaria') ")
    @PutMapping(value = "/{idPropuesta}/asignarrevisores")
    public ResponseEntity<Boolean> asignarRevisores(@PathVariable Integer idPropuesta,
                                                    @RequestParam("idDocente1") Integer idDocente1,
                                                    @RequestParam("idDocente2") Integer idDocente2,
                                                    @RequestParam("rubrica") MultipartFile rubrica,
                                                    @RequestParam("oficio") MultipartFile oficio,
                                                    @RequestParam("taskID") String taskID

    ) {

        Boolean exito = this.propuestaService.asignarRevisor(idPropuesta, idDocente1, idDocente2,rubrica, oficio, taskID);

        return ResponseEntity.ok(exito);
    }


    @PreAuthorize("hasAnyRole('docente', 'secretaria', 'direccion','coordinador') ")
    @PutMapping(value = "/{idPropuesta}/calificar")
    public ResponseEntity<Boolean> asignarCalificacion(@PathVariable Integer idPropuesta,
                                                       @RequestParam("nota")  Double nota,
                                                       @RequestParam(value="observaciones", required = false) String observaciones,
                                                       @RequestParam("idDocente") Integer idDocente,
                                                       @RequestParam("rubrica") MultipartFile rubrica,
                                                       @RequestParam("taskID") String taskID)throws IOException   {

        Boolean exito = this.propuestaService.calificarPropuestaRevisor(idPropuesta,nota, observaciones, idDocente,rubrica, taskID);
        return ResponseEntity.ok(exito);
    }



    @PreAuthorize("hasAnyRole('direccion', 'coordinador')")
    @PutMapping(value = "/{idPropuesta}/aprobar")
    public ResponseEntity<Boolean> aprobarPropuesta(@PathVariable Integer idPropuesta,
                                                       @RequestParam(value="observaciones", required = false) String observaciones,
                                                       @RequestParam("idTutor") Integer idTutor,
                                                       @RequestParam("rubrica") MultipartFile archivo,
                                                       @RequestParam("taskID") String taskID)throws IOException   {

        Boolean exito = this.propuestaService.aprobarPropuesta(idPropuesta, observaciones, idTutor,archivo, taskID);

        return ResponseEntity.ok(exito);
    }

    @PreAuthorize("hasAnyRole('direccion', 'coordinador')")
    @PutMapping(value = "/{idPropuesta}/negar")
    public ResponseEntity<Boolean> negarPropuesta(@PathVariable Integer idPropuesta,
                                                    @RequestParam(value="observaciones", required = false) String observaciones,
                                                    @RequestParam("taskID") String taskID){
        Boolean exito = this.propuestaService.negacionPropuesta(idPropuesta, observaciones, taskID);

        return ResponseEntity.ok(exito);
    }


    @PostMapping(value = "/{idPropuesta}/recordatorio")
    public void recordatorioRevisor(@PathVariable Integer idPropuesta,
                                                  @RequestParam("idUsuario") Integer idUsuario){

        this.propuestaService.recordatorioRevisores(idPropuesta, idUsuario);

    }



}
