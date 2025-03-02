package com.tesis.backend_tesis.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropuestaDTO {

    private Integer id;
    private String tema;
    private String observacion;
    private Boolean validacion;
    private String periodo;
    private Integer idDocente;
    private Integer idEstuCreacion;
    private ArchivoDTO archivo; // DTO en lugar de la entidad completa
    private EstudianteDTO estudiantePrimero;
    private EstudianteDTO estudianteSegundo;
    private EstudianteDTO estudianteTercero;
}
