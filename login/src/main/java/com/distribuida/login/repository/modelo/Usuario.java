package com.distribuida.login.repository.modelo;

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

    @Column(name = "usua_correo")
    private String correo;

    @Column(name = "usua_password")
    private String password;

    @Column(name = "usua_rol")
    private String rol;

    @Column(name = "usua_fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usua_activo")
    private Boolean activo;

    @ManyToOne
    @JoinColumn(name = "usua_carr_id", referencedColumnName = "carr_id")
    @ToString.Exclude
    private Carrera carrera; // Relación muchos a uno con Carrera

}
