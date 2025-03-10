package com.distribuida.alumno.bucket;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IBucket {

    BucketObject uploadFile(MultipartFile multipartFile) throws IOException;
    public ResponseEntity<InputStreamResource> downloadFile(String fileName) throws IOException;
    public InputStreamResource downloadFileMultipart(String fileName) throws IOException;

}
