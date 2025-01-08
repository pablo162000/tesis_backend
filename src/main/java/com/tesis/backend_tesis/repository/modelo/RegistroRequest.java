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


    private String primer_nombre;

    private String segundo_nombre;

    private String primer_apellido;

    private String segundo_apellido;

    private String cedula;

    private String correo;

    private String password;

    private Date fecha_registro;

    private Boolean activo;

    private String rol;

}
