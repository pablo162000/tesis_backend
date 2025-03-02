package com.tesis.backend_tesis.Controller;

import com.tesis.backend_tesis.repository.modelo.Archivo;
import com.tesis.backend_tesis.service.IArchivoService;
import com.tesis.backend_tesis.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping(path = "/archivo")
public class FileUploadController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private IArchivoService archivoService;




    @GetMapping("/list-buckets")
    public void listBuckets() {
        s3Service.listBuckets();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("idUsuario") Integer idUsuario) throws IOException {

        // Verificar si el archivo está vacío
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No se ha seleccionado ningún archivo.");
        }

        // Intentar guardar el archivo
        Archivo ar = archivoService.guardar(file, idUsuario);

        // Si no se pudo guardar el archivo, devolver un error
        if (ar == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el archivo.");
        }

        // Respuesta exitosa
        return ResponseEntity.ok("Archivo cargado exitosamente: " + ar.getNombre());
    }


}