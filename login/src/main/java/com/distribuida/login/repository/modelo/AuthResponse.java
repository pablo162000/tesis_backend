package com.distribuida.login.repository.modelo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse implements Serializable {


    private static final long serialVersionUID =1L;

    private Integer id;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String rol;
    private Integer idUsuario;
    private Boolean activo;
    private String nombreCarrera;
}
