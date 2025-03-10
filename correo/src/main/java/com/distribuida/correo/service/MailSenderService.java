package com.distribuida.correo.service;



import freemarker.template.TemplateException;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
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


    public void sendMultipleUser(String toEmail, List<String> ccEmails, String estudiante,
                                 String tema, String correoDireccion)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Asignar los valores al modelo de la plantilla
        Map<String, Object> model = new HashMap<>();
        model.put("nombreEstudiante", estudiante);
        model.put("tema", tema);
        model.put("correoDireccion", correoDireccion);

        // Usar FreeMarker para llenar la plantilla
        try {
            StringWriter stringWriter = new StringWriter();
            freeMarkerConfigurer.getConfiguration()
                    .getTemplate("confirmaciónEnvioPropuesta.html") // Nombre de la plantilla
                    .process(model, stringWriter);

            String htmlContent = stringWriter.toString();

            // Configurar destinatario principal
            helper.setTo(toEmail);

            // Agregar destinatarios en copia (CC), si hay alguno
            if (ccEmails != null && !ccEmails.isEmpty()) {
                helper.setCc(ccEmails.toArray(new String[0])); // Convertir la lista a un array de Strings
            }

            // Establecer los detalles del correo
            helper.setSubject("Registro Exitoso");
            helper.setText(htmlContent, true);  // true indica que es contenido HTML

            // Enviar el correo
            mailSender.send(message);
        } catch (Exception e) {
            throw new MessagingException("Error al procesar la plantilla FreeMarker", e);
        }
    }



    /*
    public void sendEmailWithS3Attachment(String toEmail, List<String> ccEmails, String usuario,
                                          String enlaceACuenta, String enlaceSoporte, String enlacePrivacidad,
                                          List<String> s3FileUrls, AmazonS3 s3Client) throws MessagingException, IOException {
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

            // Configurar destinatario principal
            helper.setTo(toEmail);

            // Agregar destinatarios en copia (CC), si hay alguno
            if (ccEmails != null && !ccEmails.isEmpty()) {
                helper.setCc(ccEmails.toArray(new String[0])); // Convertir la lista a un array de Strings
            }

            // Establecer los detalles del correo
            helper.setSubject("Registro Exitoso");
            helper.setText(htmlContent, true);  // true indica que es contenido HTML

            // Adjuntar archivos desde S3
            if (s3FileUrls != null && !s3FileUrls.isEmpty()) {
                for (String s3Url : s3FileUrls) {
                    // Extraer el bucket y la clave del archivo desde la URL de S3
                    String bucketName = extractBucketName(s3Url);
                    String objectKey = extractObjectKey(s3Url);

                    // Descargar el archivo desde S3
                    S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, objectKey));
                    InputStream inputStream = s3Object.getObjectContent();
                    byte[] fileBytes = inputStream.readAllBytes();
                    InputStreamSource attachment = new ByteArrayResource(fileBytes);

                    // Adjuntar el archivo al correo
                    helper.addAttachment(objectKey, attachment);
                }
            }

            // Enviar el correo
            mailSender.send(message);
        } catch (Exception e) {
            throw new MessagingException("Error al procesar la plantilla FreeMarker o adjuntar archivos", e);
        }
    }

    // Métodos auxiliares para extraer bucket y clave del archivo desde la URL de S3
    private String extractBucketName(String s3Url) {
        return s3Url.split("/")[2].split("\\.")[0]; // Extrae el nombre del bucket
    }

    private String extractObjectKey(String s3Url) {
        return s3Url.substring(s3Url.indexOf(".com/") + 5); // Extrae la clave del objeto
    }



     */


    public void sendNotificacionEnviopropuesta(String toEmail, List<String> ccEmails, String estudiante,
                                               String tema, String correoDireccion, InputStream fileInputStream, String fileName)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);  // true permite adjuntar archivos

        // Asignar los valores al modelo de la plantilla
        Map<String, Object> model = new HashMap<>();
        model.put("nombreEstudiante", estudiante);
        model.put("tema", tema);
        model.put("correoDireccion", correoDireccion);

        // Usar FreeMarker para llenar la plantilla
        try {
            StringWriter stringWriter = new StringWriter();
            freeMarkerConfigurer.getConfiguration()
                    .getTemplate("confirmaciónEnvioPropuesta.html") // Nombre de la plantilla
                    .process(model, stringWriter);

            String htmlContent = stringWriter.toString();

            // Configurar destinatario principal
            helper.setTo(toEmail);

            // Agregar destinatarios en copia (CC), si hay alguno
            if (ccEmails != null && !ccEmails.isEmpty()) {
                helper.setCc(ccEmails.toArray(new String[0])); // Convertir la lista a un array de Strings
            }

            // Establecer los detalles del correo
            helper.setSubject("Recepción Propuesta");
            helper.setText(htmlContent, true);  // true indica que es contenido HTML

            // Convertir el InputStream a DataSource (ByteArrayDataSource)
            if (fileInputStream != null && fileName != null) {
                // Crear un ByteArrayDataSource para adjuntar el archivo
                DataSource dataSource = new ByteArrayDataSource(fileInputStream, "application/pdf");
                helper.addAttachment(fileName, dataSource);
            }

            // Enviar el correo
            mailSender.send(message);
        } catch (Exception e) {
            throw new MessagingException("Error al procesar la plantilla FreeMarker", e);
        }
    }


    public void sendNotificacionNegacionTema(String toEmail, List<String> ccEmails, String estudiante,
                                               String tema, String correoDireccion, String observaciones)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);  // true permite adjuntar archivos

        // Asignar los valores al modelo de la plantilla
        Map<String, Object> model = new HashMap<>();
        model.put("nombreEstudiante", estudiante);
        model.put("tema", tema);
        model.put("observaciones", observaciones);
        model.put("correoDireccion", correoDireccion);

        // Usar FreeMarker para llenar la plantilla
        try {
            StringWriter stringWriter = new StringWriter();
            freeMarkerConfigurer.getConfiguration()
                    .getTemplate("negacionValidacionDirector.html") // Nombre de la plantilla
                    .process(model, stringWriter);

            String htmlContent = stringWriter.toString();

            // Configurar destinatario principal
            helper.setTo(toEmail);

            // Agregar destinatarios en copia (CC), si hay alguno
            if (ccEmails != null && !ccEmails.isEmpty()) {
                helper.setCc(ccEmails.toArray(new String[0])); // Convertir la lista a un array de Strings
            }

            // Establecer los detalles del correo
            helper.setSubject("Rechazo Propuesta por Tema");
            helper.setText(htmlContent, true);  // true indica que es contenido HTML


            // Enviar el correo
            mailSender.send(message);
        } catch (Exception e) {
            throw new MessagingException("Error al procesar la plantilla FreeMarker", e);
        }
    }




}



