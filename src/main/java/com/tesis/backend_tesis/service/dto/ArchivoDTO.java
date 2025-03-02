package com.tesis.backend_tesis.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArchivoDTO {

    private Integer id;
    private String nombre;
    private String url;
    private LocalTime fechaCreacion;
    private Integer idUsuario;
}
