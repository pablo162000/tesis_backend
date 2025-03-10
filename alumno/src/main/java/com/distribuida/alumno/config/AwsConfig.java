package com.distribuida.alumno.config;

import com.distribuida.alumno.bucket.BucketObject;
import com.distribuida.alumno.bucket.IBucket;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@Configuration
@EnableConfigurationProperties
public class AwsConfig implements IBucket {
/*
    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                "test",
                "test"
        );

        return S3Client.builder()
                .region(Region.of("us-east-1"))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

 */

    private final S3Client s3Client;

    public AwsConfig() {
        this.s3Client = createS3Client(); // Inicializamos el cliente S3
    }

    @Bean
    public S3Client s3Client() {
        return createS3Client();
    }

    private S3Client createS3Client() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("test", "test");

        return S3Client.builder()
                .endpointOverride(URI.create("http://localhost:4566"))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
                .forcePathStyle(true)
                .build();
    }

    @Override
    public BucketObject uploadFile(MultipartFile multipartFile) throws IOException {
        String bucketName = "my-first-bucket"; // Nombre del bucket
        String fileName = multipartFile.getOriginalFilename();

        if (fileName == null || fileName.isEmpty()) {
            throw new IOException("El archivo no tiene un nombre válido.");
        }

        File file = convertMultipartFileToFile(multipartFile); // Convertimos MultipartFile a File

        try {
            // Preparar la solicitud de carga del archivo
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            // Subimos el archivo
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

            // Generamos la URL del archivo subido
            String fileUrl = "http://localhost:4566/" + bucketName + "/" + fileName;

            // Devolvemos el objeto BucketObject con la información relevante
            return new BucketObject(fileName, bucketName, fileUrl);
        } catch (Exception e) {
            throw new IOException("Error al subir el archivo a S3", e);
        } finally {
            file.delete(); // Eliminamos el archivo temporal después de la carga
        }
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadFile(String fileName) throws IOException {
        String bucketName = "my-first-bucket"; // Nombre del bucket

        try {
            // Crear la solicitud para obtener el objeto desde S3
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            // Obtener el objeto desde S3
            InputStream inputStream = s3Client.getObject(getObjectRequest);

            // Crear un recurso de entrada para la respuesta
            InputStreamResource resource = new InputStreamResource(inputStream);

            // Configurar los headers de la respuesta
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            throw new IOException("Error al descargar el archivo desde S3", e);
        }
    }

    @Override
    public InputStreamResource downloadFileMultipart(String fileName) throws IOException {
        return null;
    }


    private static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        }
        return file;
    }

}

