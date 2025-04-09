package com.tesis.backend_tesis.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "correoRestClient", url = "http://localhost:8282/API/tesis/")
public interface CorreoRestClient {


    @PostMapping("/correo/registro")
    String registrarUsuario(@RequestParam String usuario,
                            @RequestParam String correo,
                            @RequestParam String enlaceVerificaion,
                            @RequestParam String correoDireccion,
                            @RequestParam String tipoUsuario);


    @PostMapping("/correo/recuperacion")
    public String recuperacionCuenta(@RequestParam String correoUsuario,
                                     @RequestParam String nombreUsuario,
                                     @RequestParam String enlaceRecuperacion,
                                     @RequestParam String correoDireccion);



    @PostMapping("/correo/registrov2")
    String registrarUsuariov2(@RequestParam String toEmail,
                              @RequestParam String usuario,
                              @RequestParam String enlaceCuenta,
                              @RequestParam String correoDireccion,
                              @RequestParam String tipoUsuario);


    @PostMapping("/correo/recuperacionv2")
    public String recuperacionCuentav2(@RequestParam String correoUsuario,
                                     @RequestParam String nombreUsuario,
                                     @RequestParam String enlaceRecuperacion,
                                     @RequestParam String correoDireccion);


    @PostMapping(value = "/correo/notificacionenviopropuesta", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> enviareachivo(
            @RequestParam("email") String toEmail,  // Correo del destinatario principal
            @RequestParam("ccemails") List<String> ccEmails,  // Lista de correos CC
            @RequestParam("estudiante") String estudiante,  // Información del estudiante
            @RequestParam("tema") String tema,  // Tema del correo
            @RequestParam("correodireccion") String correoDireccion,  // Dirección del correo (si es necesario)
            @RequestPart("archivo") MultipartFile archivo  // Archivo adjunto
    );

    @PostMapping(value = "/correo/notificacionnegaciontema")
    public ResponseEntity<String> notificacionNegacionTema(
            @RequestParam("email") String toEmail,  // Correo del destinatario principal
            @RequestParam("ccemails") List<String> ccEmails,  // Lista de correos CC
            @RequestParam("estudiante") String estudiante,  // Información del estudiante
            @RequestParam("tema") String tema,  // Tema del correo
            @RequestParam("correodireccion") String correoDireccion,  // Dirección del correo (si es necesario)
            @RequestParam("observaciones") String observaciones
    );



    @PostMapping(value = "/correo/notificacionenviopropuestav2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> notificacionenviopropuestav2(
            @RequestParam("email") String toEmail,  // Correo del destinatario principal
            @RequestParam("ccemails") List<String> ccEmails,  // Lista de correos CC
            @RequestParam("estudiante") String estudiante,  // Información del estudiante
            @RequestParam("tema") String tema,  // Tema del correo
            @RequestParam("correodireccion") String correoDireccion,  // Dirección del correo (si es necesario)
            @RequestPart("archivo") MultipartFile archivo  // Archivo adjunto
    );

    @PostMapping(value="/notificacionasignacionrevisorv2")
    public ResponseEntity<String> asignacionrtevisores(
            @RequestParam("toemails") List<String> toEmails,
            @RequestParam("ccemails") List<String> ccEmails,
            @RequestParam("nombreRevisor")String nombreRevisor,
            @RequestParam("nombreEstudiantes")String nombreEstudiantes,
            @RequestParam("linkRevision")String linkRevision,
            @RequestParam("tema")String temaPropuesta,
            @RequestParam("correoDireccion")String correoDireccion,
            @RequestParam("fechaEntrega")String fechaEntrega,
            @RequestPart("rubrica") MultipartFile rubrica,
            @RequestPart("archivo") MultipartFile archivo ,
            @RequestPart("oficio") MultipartFile oficio );
}
