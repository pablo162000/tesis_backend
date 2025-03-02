package com.distribuida.login.service.dto;

import com.distribuida.login.repository.modelo.Carrera;
import jakarta.persistence.*;
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

    private String correo;

    private String password;

    private String rol;

    private LocalDateTime fechaCreacion;

    private Boolean activo;

    private Integer iDCarrera; // Relación uno a uno con Carrera

    private List<Integer> archivos;
}
