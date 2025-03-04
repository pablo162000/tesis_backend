package com.distribuida.administrativos.repository.modelo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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

    private Integer idCarrera;


}
