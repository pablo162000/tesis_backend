package com.tesis.backend_tesis.repository.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "usuario")
public class Usuarios {

    @Id
    @Column(name = "usua_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usua_id_seq")
    @SequenceGenerator(name = "usua_id_seq", sequenceName = "usua_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Column(name = "usua_username")
    private String username;

    @Column(name = "usua_correo")
    private String correo;

    @Column(name = "usua_password")
    private String password;

    @Column(name = "usua_rol")
    private String usua_rol;

    @Column(name = "usua_fecha_creacion")
    private Date fechaCreacion;

    @Column(name = "usua_activo")
    private Boolean activo;


    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Estudiantes usuariosEstudiantes;



}
