package com.distribuida.correo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MailGunService {

    @Value("${mailgun.apiKey}")
    private String apiKey;

    @Value("${mailgun.sandboxDomain}")
    private String sandboxDomain;

    @Value("${mailgun.fromEmail}")
    private String fromEmail;

    public void sendEmail(String toEmail, String usuario, String enlaceCuenta,
                          String correoDireccion,
                          String tipoUsuario) throws UnirestException {
        toEmail = "luismosquera97@gmail.com"; //FORZADO SOLO PARA PRUEBAS

        // Seleccionar la plantilla: solo "estudiante" usa su plantilla, los demás van a "docente"
        String templateName = tipoUsuario.equalsIgnoreCase("estudiante")
                ? "ingresoestudiante"
                : "registrousuario";

        //Crear el mapa de variables dinámicas
        Map<String, String> variablesMap = new HashMap<>();
        variablesMap.put("usuario", usuario);
        variablesMap.put("enlace_a_tu_cuenta", enlaceCuenta);
        variablesMap.put("correoDireccion", correoDireccion);


        //Convertir el mapa a JSON
        String variablesJson;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            variablesJson = objectMapper.writeValueAsString(variablesMap);
        } catch (Exception e) {
            throw new UnirestException("Error al generar JSON de variables", e);
        }

        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + sandboxDomain + "/messages")
                .basicAuth("api", apiKey)
                .queryString("from", fromEmail)
                .queryString("to", toEmail)
                .queryString("subject", "Registro Exitoso")
                .queryString("template", templateName) // 🔹 Nombre de la plantilla
                .queryString("h:X-Mailgun-Variables", variablesJson) // 🔹 Enviar JSON bien formateado
                .asJson();

        if (response.getStatus() != 200) {
            throw new UnirestException("Error al enviar el correo: " + response.getStatus() + " " + response.getBody());
        }
    }

    public void sendEmailAsignacionRevisor(List<String> toEmails,
                                           List<String> ccEmails,
                                           String nombreRevisor,
                                           String nombreEstudiantes,
                                           String linkRevision,
                                           String temaPropuesta,
                                           String correoDireccion,
                                           String fechaEntrega,
                                           InputStream rubrica, String fileNameRubrica,
                                           InputStream oficio, String fileNameOficio) throws UnirestException {

        // Crear el mapa de variables dinámicas
        Map<String, String> variablesMap = new HashMap<>();
        variablesMap.put("nombreRevisor", nombreRevisor);
        variablesMap.put("nombreEstudiantes", nombreEstudiantes);
        variablesMap.put("temaPropuesta", temaPropuesta);
        variablesMap.put("fechaEntrega", fechaEntrega);
        variablesMap.put("linkRevision", linkRevision);
        variablesMap.put("correoDireccion", correoDireccion);

        String variablesJson;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            variablesJson = objectMapper.writeValueAsString(variablesMap);
        } catch (Exception e) {
            throw new UnirestException("Error al generar JSON de variables", e);
        }

        MultipartBody request = Unirest.post("https://api.mailgun.net/v3/" + sandboxDomain + "/messages")
                .basicAuth("api", apiKey)
                .field("from", fromEmail)
                .field("subject", "Registro Exitoso")
                .field("template", "asignacionrevisor")
                .field("h:X-Mailgun-Variables", variablesJson);

        // Agregar destinatarios principales
        if (toEmails != null && !toEmails.isEmpty()) {
            for (String email : toEmails) {
                request.field("to", email);
            }
        }

        // Agregar destinatarios en copia (CC)
        if (ccEmails != null && !ccEmails.isEmpty()) {
            for (String cc : ccEmails) {
                request.field("cc", cc);
            }
        }

        // Adjuntar archivos si existen
        try {
            if (rubrica != null && fileNameRubrica != null)
                request.field("attachment", new ByteArrayInputStream(rubrica.readAllBytes()), fileNameRubrica);

            if (oficio != null && fileNameOficio != null)
                request.field("attachment", new ByteArrayInputStream(oficio.readAllBytes()), fileNameOficio);
        } catch (IOException e) {
            throw new UnirestException("Error al leer archivos adjuntos", e);
        }

        HttpResponse<JsonNode> response = request.asJson();

        if (response.getStatus() != 200) {
            throw new UnirestException("Error al enviar el correo: " + response.getStatus() + " " + response.getBody());
        }
    }

    public void sendNotificacionEnvioPropuestaMailgun(String toEmail, List<String> ccEmails,
                                                      String estudiante, String tema, String correoDireccion,
                                                      InputStream fileInputStream, String fileName)
            throws UnirestException {

        // Variables dinámicas de plantilla
        Map<String, String> variablesMap = new HashMap<>();
        variablesMap.put("nombreEstudiante", estudiante);
        variablesMap.put("tema", tema);
        variablesMap.put("correoDireccion", correoDireccion);

        String variablesJson;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            variablesJson = objectMapper.writeValueAsString(variablesMap);
        } catch (Exception e) {
            throw new UnirestException("Error al generar JSON de variables", e);
        }

        // Preparar solicitud base con campos comunes
        MultipartBody request = Unirest.post("https://api.mailgun.net/v3/" + sandboxDomain + "/messages")
                .basicAuth("api", apiKey)
                .field("from", fromEmail)
                .field("to", toEmail)
                .field("subject", "Recepción Propuesta")
                .field("template", "confirmacionpropuesta")
                .field("h:X-Mailgun-Variables", variablesJson);

        // Agregar CC si existen
        if (ccEmails != null && !ccEmails.isEmpty()) {
            String ccList = String.join(",", ccEmails);
            request.field("cc", ccList);
        }

        // Agregar archivo adjunto si existe
        if (fileInputStream != null && fileName != null) {
            try {
                byte[] fileBytes = fileInputStream.readAllBytes();
                request.field("attachment", new ByteArrayInputStream(fileBytes), fileName);
            } catch (IOException e) {
                throw new UnirestException("Error al leer el archivo adjunto", e);
            }
        }

        // Enviar correo
        HttpResponse<JsonNode> response = request.asJson();

        if (response.getStatus() != 200) {
            throw new UnirestException("Error al enviar el correo: " + response.getStatus() + " " + response.getBody());
        }
    }


    public void sendRecoverEmail(String toEmail, String usuario, String enlaceRecuperacion,
                                 String correoDireccion) throws UnirestException{

        toEmail = "luismosquera97@gmail.com"; //FORZADO SOLO PARA PRUEBAS

        //Crear el mapa de variables dinámicas
        Map<String, String> variablesMap = new HashMap<>();
        variablesMap.put("usuario", usuario);
        variablesMap.put("enlace_recuperacion", enlaceRecuperacion);
        variablesMap.put("correoDireccion", correoDireccion);


        //Convertir el mapa a JSON
        String variablesJson;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            variablesJson = objectMapper.writeValueAsString(variablesMap);
        } catch (Exception e) {
            throw new UnirestException("Error al generar JSON de variables", e);
        }

        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + sandboxDomain + "/messages")
                .basicAuth("api", apiKey)
                .queryString("from", fromEmail)
                .queryString("to", toEmail)
                .queryString("subject", "Recuperación de Cuenta")
                .queryString("template", "recuperarcuenta") // 🔹 Nombre de la plantilla
                .queryString("h:X-Mailgun-Variables", variablesJson) // 🔹 Enviar JSON bien formateado
                .asJson();

        if (response.getStatus() != 200) {
            throw new UnirestException("Error al enviar el correo: " + response.getStatus() + " " + response.getBody());
        }

    }


    public void sendEmaiNegacionTema(String toEmail, List<String> ccEmails, String estudiante,
                                     String tema, String correoDireccion, String observaciones) throws UnirestException {

        toEmail = "luismosquera97@gmail.com";
        List<String> ccEmailsQuemados = new ArrayList<>();
        ccEmailsQuemados.add("jdmasabanda@uce.edu.ec");
        ccEmailsQuemados.add("lfmosquerar@uce.edu.ec");
        ccEmailsQuemados.add("pasuntaxih@uce.edu.ec");


        // Crear el mapa de variables dinámicas
        Map<String, String> variablesMap = new HashMap<>();
        variablesMap.put("nombreEstudiante", estudiante);
        variablesMap.put("tema", tema);
        variablesMap.put("observaciones", observaciones);
        variablesMap.put("correoDireccion", correoDireccion);

        String variablesJson;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            variablesJson = objectMapper.writeValueAsString(variablesMap);
        } catch (Exception e) {
            throw new UnirestException("Error al generar JSON de variables", e);
        }

        MultipartBody request = Unirest.post("https://api.mailgun.net/v3/" + sandboxDomain + "/messages")
                .basicAuth("api", apiKey)
                .field("from", fromEmail)
                .field("to", toEmail)
                .field("subject", "Rechazo Propuesta por Tema")
                .field("template", "negaciontema")
                .field("h:X-Mailgun-Variables", variablesJson);



        // Agregar destinatarios en copia (CC)
        if (ccEmailsQuemados != null && !ccEmailsQuemados.isEmpty()) {
            for (String cc : ccEmailsQuemados) {
                request.field("cc", cc);
            }
        }


        HttpResponse<JsonNode> response = request.asJson();

        if (response.getStatus() != 200) {
            throw new UnirestException("Error al enviar el correo: " + response.getStatus() + " " + response.getBody());
        }
    }



}