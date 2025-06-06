package com.tesis.backend_tesis.config;

import com.tesis.backend_tesis.domain.document.BucketObject;
import com.tesis.backend_tesis.domain.document.IBucket;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


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

    public AwsConfig(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @PostConstruct
    public void ensureBucketExists() {
        String bucketName = "my-first-bucket";
        try {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
            System.out.println("✔ Bucket creado: " + bucketName);
        } catch (Exception e) {
            System.out.println("⚠ Bucket ya existe o error al crear: " + e.getMessage());
        }
    }

    @Override
    public BucketObject uploadFile(MultipartFile multipartFile, String nombre) throws IOException {
        String bucketName = "my-first-bucket";

        if (nombre == null || nombre.isEmpty()) {
            throw new IOException("El archivo no tiene un nombre válido.");
        }

        File file = convertMultipartFileToFile(multipartFile);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(nombre)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

            String fileUrl = "http://localhost:4566/" + bucketName + "/" + nombre;
            return new BucketObject(nombre, bucketName, fileUrl);
        } catch (Exception e) {
            throw new IOException("Error al subir el archivo a S3", e);
        } finally {
            file.delete();
        }
    }

    private static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        }
        return file;
    }
}