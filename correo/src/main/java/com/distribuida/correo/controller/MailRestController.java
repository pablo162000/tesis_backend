package com.distribuida.correo.controller;

import com.distribuida.correo.service.MailGunService;
import com.distribuida.correo.service.MailSenderService;
import jakarta.mail.MessagingException;
import kong.unirest.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/correo")
public class MailRestController {

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private MailGunService mailService;


    @PostMapping("/registrov2")
    public ResponseEntity<String> sendMail(
            @RequestParam String toEmail,
            @RequestParam String usuario,
            @RequestParam String enlaceCuenta,
            @RequestParam String correoDireccion,
            @RequestParam String tipoUsuario) {

        String enlaceSoporte = "http://miapp.com/soporte";
        String enlacePrivacidad = "http://miapp.com/privacidad";

        try {
            this.mailService.sendEmail(toEmail, usuario, enlaceCuenta,
                    correoDireccion,
                    tipoUsuario);
            return ResponseEntity.ok("Correo enviado exitosamente a " + toEmail);
        } catch (UnirestException e) {
            return ResponseEntity.status(500).body("Error al enviar el correo: " + e.getMessage());
        }
    }


    @PostMapping("/recuperacionv2")
    public String recuperacionCuentav2(@RequestParam String correoUsuario,
                                     @RequestParam String nombreUsuario,
                                     @RequestParam String enlaceRecuperacion,
                                     @RequestParam String correoDireccion) {
        this.mailService.sendRecoverEmail(correoUsuario,
                nombreUsuario,
                enlaceRecuperacion,
                correoDireccion);

        return "envio correcto de recuperacion";
    }





    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam String usuario,
                                   @RequestParam String correo,
                                   @RequestParam String enlaceVerificaion,
                                   @RequestParam String correoDireccion,
                                   @RequestParam String tipoUsuario) throws MessagingException {
        // Lógica para registrar al usuario

        //String enlaceACuenta = "http://miapp.com/mi-cuenta";
        String enlaceSoporte = "http://miapp.com/soporte";
        String enlacePrivacidad = "http://miapp.com/privacidad";

        // Enviar el correo de bienvenida
        this.mailSenderService.sendRegistrationEmail(correo,
                                                usuario,
                                                enlaceVerificaion,
                                                enlaceSoporte,
                                                enlacePrivacidad,
                                                correoDireccion,
                                                tipoUsuario);


        return "registroExitoso"; // Redirige o muestra una página de éxito
    }

    @PostMapping("/recuperacion")
    public String recuperacionCuenta(@RequestParam String correoUsuario,
                                   @RequestParam String nombreUsuario,
                                   @RequestParam String enlaceRecuperacion,
                                   @RequestParam String correoDireccion)
            throws MessagingException {
        this.mailSenderService.sendRecoverEmail(correoUsuario,
                                                nombreUsuario,
                                                enlaceRecuperacion,
                                                correoDireccion);

        return "envio correcto de recuperacion";
    }


    @PostMapping(value = "/notificacionenviopropuesta", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> notificacionEnvioPropuesta(
            @RequestParam("email") String toEmail,  // Correo del destinatario principal
            @RequestParam("ccemails") List<String> ccEmails,  // Lista de correos CC
            @RequestParam("estudiante") String estudiante,  // Información del estudiante
            @RequestParam("tema") String tema,  // Tema del correo
            @RequestParam("correodireccion") String correoDireccion,  // Dirección del correo (si es necesario)
            @RequestPart("archivo") MultipartFile archivo  // Archivo adjunto
    ) {
        try {
            // Convertir el MultipartFile a InputStream
            InputStream fileInputStream = archivo.getInputStream();
            String fileName = archivo.getOriginalFilename();

            // Llamar al servicio para enviar el correo
            mailSenderService.sendNotificacionEnviopropuesta(toEmail, ccEmails, estudiante, tema, correoDireccion, fileInputStream, fileName);

            // Si todo va bien, respondemos con un mensaje de éxito
            return ResponseEntity.status(HttpStatus.OK).body("Correo enviado exitosamente.");
        } catch (IOException | MessagingException e) {
            // En caso de error al enviar el correo
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar el correo: " + e.getMessage());
        }
    }

    @PostMapping(value = "/notificacionnegaciontema")
    public ResponseEntity<String> notificacionNegacionTema(
            @RequestParam("email") String toEmail,  // Correo del destinatario principal
            @RequestParam("ccemails") List<String> ccEmails,  // Lista de correos CC
            @RequestParam("estudiante") String estudiante,  // Información del estudiante
            @RequestParam("tema") String tema,  // Tema del correo
            @RequestParam("correodireccion") String correoDireccion,  // Dirección del correo (si es necesario)
            @RequestParam("observaciones") String observaciones
    ) {
        try {


            // Llamar al servicio para enviar el correo
            mailSenderService.sendNotificacionNegacionTema(toEmail, ccEmails, estudiante, tema, correoDireccion, observaciones);

            // Si todo va bien, respondemos con un mensaje de éxito
            return ResponseEntity.status(HttpStatus.OK).body("Correo enviado exitosamente.");
        } catch (MessagingException e) {
            // En caso de error al enviar el correo
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar el correo: " + e.getMessage());
        }
    }


}