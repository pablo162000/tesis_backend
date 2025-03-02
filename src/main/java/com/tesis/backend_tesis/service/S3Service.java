package com.tesis.backend_tesis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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