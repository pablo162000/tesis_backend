package com.tesis.backend_tesis.config;

import com.tesis.backend_tesis.domain.document.BucketObject;
import com.tesis.backend_tesis.domain.document.IBucket;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

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


    @Value("${aws.nombrebucket}")
    private String nombreBucket;


    private final S3Client s3Client;

    public AwsConfig(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /*
    @PostConstruct
    public void ensureBucketExists() {
        //String bucketName = "my-first-bucket";
        try {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(nombreBucket).build());
            System.out.println("✔ Bucket creado: " + nombreBucket);
        } catch (Exception e) {
            System.out.println("⚠ Bucket ya existe o error al crear: " + e.getMessage());
        }
    }

     */

    @Override
    public BucketObject uploadFile(MultipartFile multipartFile, String nombre) throws IOException {
        //String bucketName = "my-first-bucket";

        if (nombre == null || nombre.isEmpty()) {
            throw new IOException("El archivo no tiene un nombre válido.");
        }

        File file = convertMultipartFileToFile(multipartFile);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(nombreBucket)
                    .key(nombre)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

            String fileUrl = "http://localhost:4566/" + nombreBucket + "/" + nombre;
            return new BucketObject(nombre, nombreBucket, fileUrl);
        } catch (Exception e) {
            throw new IOException("Error al subir el archivo a S3", e);
        } finally {
            file.delete();
        }
    }

    @Override
    public ResponseInputStream<GetObjectResponse> downFile(String nombre) throws IOException {
        String bucketName = "my-first-bucket";

        if (nombre == null || nombre.isEmpty()) {
            throw new IOException("No hay nombre del archivo.");
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(nombreBucket)
                        .key(nombre)
                        .build();

        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);

        if (s3Object.equals(null)){
            throw new IOException("No se encontro el archivo.");
        }

        return s3Object;

    }

    @Override
    @Transactional
    public Boolean moverYEliminar(String bucket, String keyOrigen, String keyDestino) throws IOException {
        try {
            // 1. Verificar si el archivo original existe
            HeadObjectRequest headRequest = HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(keyOrigen)
                    .build();
            s3Client.headObject(headRequest); // Lanza excepción si no existe

            // 2. Copiar el archivo
            CopyObjectRequest copyReq = CopyObjectRequest.builder()
                    .sourceBucket(bucket)
                    .sourceKey(keyOrigen)
                    .destinationBucket(bucket)
                    .destinationKey(keyDestino)
                    .build();
            s3Client.copyObject(copyReq);

            // 3. Eliminar el archivo original
            DeleteObjectRequest deleteReq = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(keyOrigen)
                    .build();
            s3Client.deleteObject(deleteReq);

            return true;

        } catch (NoSuchKeyException e) {
            throw new IOException("El archivo no existe en S3: " + keyOrigen, e);
        } catch (S3Exception | SdkClientException e) {
            throw new IOException("Error al mover y eliminar el archivo en S3: " + e.getMessage(), e);
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