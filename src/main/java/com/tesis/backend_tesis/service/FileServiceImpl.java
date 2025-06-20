package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.config.AwsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class FileServiceImpl implements IFileService{

    @Autowired
    private AwsConfig awsConfig;

    @Override
    public File downFileToTemp(String nombre) throws IOException {
        if (nombre == null || nombre.isEmpty()) {
            throw new IOException("Nombre del archivo no puede ser nulo");
        }

        // Reemplazar slashes para evitar rutas dentro del nombre
        String nombreArchivoSeguro = nombre.replaceAll("[/\\\\]", "_");

        // Extraer extensión (opcional)
        String extension = "";
        int i = nombreArchivoSeguro.lastIndexOf('.');
        if (i > 0 && i < nombreArchivoSeguro.length() - 1) {
            extension = nombreArchivoSeguro.substring(i); // ej: ".pdf"
            nombreArchivoSeguro = nombreArchivoSeguro.substring(0, i); // quitar extensión del nombre
        }

        // Crear archivo temporal
        File temp = File.createTempFile("descarga-", extension);

        // Descargar desde S3 y escribir en el archivo temporal
        try (ResponseInputStream<GetObjectResponse> s3Object = awsConfig.downFile(nombre);
             FileOutputStream fos = new FileOutputStream(temp)) {

            s3Object.transferTo(fos);
        } catch (Exception e) {
            throw new IOException("Error al guardar archivo temporal: " + e.getMessage(), e);
        }

        return temp;
    }
    /*
    public File downFileToTemp(String nombre) throws IOException {
        if (nombre == null || nombre.isEmpty()) {
            ResponseEntity.badRequest().body("Nombre del archivo no puede ser nulo");
        }


        // Crear archivo temporal
        File temp = File.createTempFile("descarga-", nombre);

        System.out.println("nombrearchivo en downloadfile....: "+nombre);
        //System.out.println("nombrearchivo seguro en downloadfile....: "+nombreArchivoSeguro);

        // Obtener el archivo desde S3
        try (ResponseInputStream<GetObjectResponse> s3Object = awsConfig.downFile(nombre);
             FileOutputStream fos = new FileOutputStream(temp)) {

            // Transferir los bytes al archivo temporal
            s3Object.transferTo(fos);
        } catch (Exception e) {
            throw new IOException("Error al guardar archivo temporal: " + e.getMessage(), e);
        }

        return temp;
    }

     */




}
