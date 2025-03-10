package com.distribuida.alumno.clients;

import com.distribuida.alumno.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "correoRestClient", url = "http://localhost:8282/API/tesis/", configuration = FeignClientConfig.class)
public interface CorreoRestClient {


    @PostMapping("/correo/registro")
    String registrarUsuario(@RequestParam String usuario, @RequestParam String correo, @RequestParam String enlaceVerificaion);

    @PostMapping("/correo/enviomultiple")
    ResponseEntity<String> envioCorreomultiple(@RequestParam String toEmail,
                                               @RequestParam List<String> ccEmails,
                                               @RequestParam String estudiante,
                                               @RequestParam String tema,
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

}