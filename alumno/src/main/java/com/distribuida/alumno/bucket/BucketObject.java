package com.distribuida.alumno.bucket;


import lombok.Getter;

@Getter
public class BucketObject {


    private String fileName;
    private String bucket;
    private String fileUrl;

    public BucketObject(String fileName, String bucket, String fileUrl) {
        this.fileName = fileName;
        this.bucket = bucket;
        this.fileUrl = fileUrl;
    }
}
