package com.tesis.backend_tesis.domain.document;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.util.List;

public interface IBucket {

    BucketObject uploadFile(MultipartFile multipartFile, String nombre) throws IOException;

    ResponseInputStream<GetObjectResponse> downFile(String nombre) throws IOException;

    public Boolean moverYEliminar(String bucket, String keyOrigen, String keyDestino)throws IOException;

}
