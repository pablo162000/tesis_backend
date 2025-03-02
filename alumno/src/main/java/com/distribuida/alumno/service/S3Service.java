package com.distribuida.alumno.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

@Service
public class S3Service {

    private final S3Client s3Client;

    // Constructor para inyección de dependencias
    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }


    public void listBuckets() {
        s3Client.listBuckets().buckets().forEach(bucket -> System.out.println(bucket.name()));
    }


}