package com.distribuida.administrativos.repository.modelo;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "docente")
public class Docente {

    @Id
    @Column(name = "docen_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "docen_id_seq")
    @SequenceGenerator(name = "docen_id_seq", sequenceName = "docen_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Column(name = "docen_primer_nombre")
    private String primerNombre;

    @Column(name = "docen_segundo_nombre")
    private String segundoNombre;

    @Column(name = "docen_primer_apellido")
    private String primerApellido;

    @Column(name = "docen_segundo_apellido")
    private String segundoApellido;

    @Column(name = "docen_cedula")
    private String cedula;

    @Column(name = "docen_celular")
    private String celular;

    @Column(name = "docen_id_usuario") // Este es el nombre de la columna que actúa como clave foránea
    private Integer idUsuario;

    /*
    @ManyToOne
    @JoinColumn(name = "usua_carr_id", referencedColumnName = "carr_id")
    @ToString.Exclude
    private Carrera carrera; // Relación muchos a uno con Carrera


     */
}
