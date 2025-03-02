package com.distribuida.alumno.service.dto;

import com.distribuida.alumno.repository.modelo.EstadoValidacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropuestaDTO {

    private Integer id;
    private Integer estudiantePrimero;
    private Integer estudianteSegundo;
    private Integer estudianteTercero;
    private Integer archivoPropuesta;
    private String tema;
    private String observacion;
    private EstadoValidacion validacion;
    private String periodo;
    private Integer idDocenteTutor;
    private Integer idDocentePrimerRevisor;
    private Double notaPrimerRevisor;
    private LocalDateTime fechaPrimerRevisor;
    private Integer archivoRubricaPrimerRevisor;
    private Integer idDocenteSegundoRevisor;
    private Double notaSegundoRevisor;
    private LocalDateTime fechaSegundoRevisor;
    private Integer archivoRubricaSegundoRevisor;
    private Boolean estadoAprobación;
    private Integer idEstuCreacion;
    private LocalDateTime fechaEnvio;
}
