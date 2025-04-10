package com.tesis.backend_tesis.repository.modelo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse implements Serializable {


    private static final long serialVersionUID =1L;

    private Integer idUsuario;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String correo;
    private Boolean validdo;
    private Boolean activo;
    private String nombreFacultad;
    private Integer idFacultad;
    private String nombreCarrera;
    private Integer idCarrera;
    private List<String> rolesDisponibles;
    private String rolSeleccionado;

}
