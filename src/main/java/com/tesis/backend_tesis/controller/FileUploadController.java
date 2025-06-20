package com.tesis.backend_tesis.controller;

import com.tesis.backend_tesis.config.AwsConfig;
import com.tesis.backend_tesis.repository.modelo.Archivo;
import com.tesis.backend_tesis.service.IArchivoService;
import com.tesis.backend_tesis.service.IFileService;
import com.tesis.backend_tesis.service.S3Service;
import kong.unirest.FileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping(path = "/archivo")
public class FileUploadController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private IArchivoService archivoService;

    @Autowired
    private IFileService fileService;




    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/list-buckets")
    public void listBuckets() {
        s3Service.listBuckets();
    }


    //@PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("idUsuario") Integer idUsuario, @RequestParam("nombre") String nombre) throws IOException {

        // Verificar si el archivo está vacío
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No se ha seleccionado ningún archivo.");
        }

        if (nombre == null || nombre.isEmpty()) {
            return ResponseEntity.badRequest().body("No se ha seleccionado ningún nombre.");
        }

        System.out.println(file.getOriginalFilename());
        System.out.println(file.getContentType());

        // Intentar guardar el archivo
        Archivo ar = archivoService.guardar(file, idUsuario, nombre);


        // Si no se pudo guardar el archivo, devolver un error
        if (ar == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el archivo.");
        }

        // Respuesta exitosa
        return ResponseEntity.ok("Archivo cargado exitosamente: " + ar.getNombre());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/download")
    public ResponseEntity<Resource> abrirArchivo(@RequestParam("nombre") String nombreArchivo) throws IOException {
        File archivo = this.fileService.downFileToTemp(nombreArchivo);

        MediaType mediaType = detectarMimeType(nombreArchivo);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(archivo));

        return ResponseEntity.ok()
                .contentLength(archivo.length())
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline().filename(nombreArchivo).build().toString())
                .body(resource);
    }

    private MediaType detectarMimeType(String nombreArchivo) {
        if (nombreArchivo == null) return MediaType.APPLICATION_OCTET_STREAM;

        String nombre = nombreArchivo.toLowerCase();
        if (nombre.endsWith(".pdf")) return MediaType.APPLICATION_PDF;
        if (nombre.endsWith(".png")) return MediaType.IMAGE_PNG;
        if (nombre.endsWith(".jpg") || nombre.endsWith(".jpeg")) return MediaType.IMAGE_JPEG;
        if (nombre.endsWith(".txt")) return MediaType.TEXT_PLAIN;

        return MediaType.APPLICATION_OCTET_STREAM; // Por defecto
    }




}