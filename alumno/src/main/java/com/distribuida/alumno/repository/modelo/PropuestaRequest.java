package com.distribuida.alumno.repository.modelo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    private String idDocenteTutor;
    private Integer idEstuCreacion; // Solo el ID del estudiante creador

}