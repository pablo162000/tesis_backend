package com.tesis.backend_tesis.repository.modelo;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "view_secretarias")
@Getter
public class VistaSecretaria {

    @Id
    @Column(name = "usua_id")
    private Integer idUsuario;

    @Column(name = "secre_id")
    private Integer idSecretaria;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "correo_valido")
    private Boolean correoValido;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "cedula")
    private String cedula;

    @Column(name = "correo")
    private String correo;

    @Column(name = "celular")
    private String celular;

    @Column(name = "facultad")
    private String facultad;

    @Column(name = "carrera")
    private String carrera;


}
