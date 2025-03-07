package com.distribuida.correo.controller;

import com.distribuida.correo.service.MailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/correo")
public class MailRestController {

    @Autowired
    private MailSenderService mailSenderService;

    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam String usuario, @RequestParam String correo) throws MessagingException {
        // Lógica para registrar al usuario

        String enlaceACuenta = "http://miapp.com/mi-cuenta";
        String enlaceSoporte = "http://miapp.com/soporte";
        String enlacePrivacidad = "http://miapp.com/privacidad";

        // Enviar el correo de bienvenida
        mailSenderService.sendRegistrationEmail(correo, usuario, enlaceACuenta, enlaceSoporte, enlacePrivacidad);

        return "registroExitoso"; // Redirige o muestra una página de éxito
    }
}