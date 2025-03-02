package com.tesis.backend_tesis.service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {

    private Integer id;
    private String username;
    private String correo;
    private String rol;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private Integer idEstudiante;
    private List<Integer> archivos;
}
