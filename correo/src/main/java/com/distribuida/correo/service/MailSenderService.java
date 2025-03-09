package com.distribuida.correo.service;



import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;


import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    public void sendRegistrationEmail(String toEmail, String usuario, String enlaceACuenta, String enlaceSoporte, String enlacePrivacidad) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Asignar los valores al modelo de la plantilla
        Map<String, Object> model = new HashMap<>();
        model.put("usuario", usuario);
        model.put("enlace_a_tu_cuenta", enlaceACuenta);
        model.put("enlace_soporte", enlaceSoporte);
        model.put("enlace_privacidad", enlacePrivacidad);

        // Usar FreeMarker para llenar la plantilla
        try {
            StringWriter stringWriter = new StringWriter();
            freeMarkerConfigurer.getConfiguration()
                    .getTemplate("registroUsuarioEstudiante.html") // Nombre de la plantilla
                    .process(model, stringWriter);

            String htmlContent = stringWriter.toString();

            // Establecer los detalles del correo
            helper.setTo(toEmail);
            helper.setSubject("Registro Exitoso");
            helper.setText(htmlContent, true);  // true indica que es contenido HTML

            // Enviar el correo
            mailSender.send(message);
        } catch (Exception e) {
            throw new MessagingException("Error al procesar la plantilla FreeMarker", e);
        }
    }
}



