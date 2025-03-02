package com.distribuida.alumno.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArchivoDTO {

    private Integer id;
    private String nombre;
    private String url;
    private LocalDateTime fechaCreacion;
    private Integer idCarrera;
    private Integer idUsuario;

}
