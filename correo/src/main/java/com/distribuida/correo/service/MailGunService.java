package com.distribuida.correo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
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




}