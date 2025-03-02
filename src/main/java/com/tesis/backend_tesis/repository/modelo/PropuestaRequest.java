package com.tesis.backend_tesis.repository.modelo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropuestaRequest implements Serializable {

    private static final long serialVersionUID =1L;

    private String correoEstudiantePrimero;
    private String correoEstudianteSegundo; // Opcional
    private String correoEstudianteTercero; // Opcional
    private String tema;
    private String periodo;
    private Integer idEstuCreacion; // Solo el ID del estudiante creador

}