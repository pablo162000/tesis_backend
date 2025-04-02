package com.tesis.backend_tesis.repository.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "usuario")
public class Usuario {

    @Id
    @Column(name = "usua_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usua_id_seq")
    @SequenceGenerator(name = "usua_id_seq", sequenceName = "usua_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Column(name = "usua_primer_nombre", nullable = false)
    private String primerNombre;

    @Column(name = "usua_segundo_nombre")
    private String segundoNombre;

    @Column(name = "usua_primer_apellido", nullable = false)
    private String primerApellido;

    @Column(name = "usua_segundo_apellido")
    private String segundoApellido;

    @Column(name = "usua_cedula", unique = true)
    private String cedula;

    @Column(name = "usua_correo", unique = true)
    private String correo;

    @Column(name = "usua_password")
    private String password;

    @Column(name = "usua_celular")
    private String celular;

    @Column(name = "usua_fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usua_activo")
    private Boolean activo;

    @Column(name = "usua_correo_valido")
    private Boolean correoValido;

    @ToString.Exclude
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Estudiante usuarioEstudiante;

    @ToString.Exclude
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Docente usuarioDocente;

    @ToString.Exclude
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Carrera usuarioCarrera;

    @ToString.Exclude
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Secretaria usuarioSecretaria;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<UsuarioRol> usuariosRoles;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Archivo> archivos;


}
