package com.tesis.backend_tesis.repository.modelo;

import java.io.Serializable;
import java.util.*;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistroRequest implements Serializable {

    private static final long serialVersionUID =1L;

    private String primerNombre;

    private String segundoNombre;

    private String primerApellido;

    private String segundoApellido;

    private String cedula;

    private String celular;

    private String correo;

    private String password;

    private Date fechaRegistro;

    private Integer idCarrera;

    private Integer idFacultad;

    private String tipoUsuario;


}
