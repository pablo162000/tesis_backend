package com.distribuida.administrativos.repository.modelo;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "administrtativo")
public class Administrativo {
    @Id
    @Column(name = "admin_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_id_seq")
    @SequenceGenerator(name = "admin_id_seq", sequenceName = "admin_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Column(name = "admin_primer_nombre")
    private String primerNombre;

    @Column(name = "admin_segundo_nombre")
    private String segundoNombre;

    @Column(name = "admin_primer_apellido")
    private String primerApellido;

    @Column(name = "admin_segundo_apellido")
    private String segundoApellido;

    @Column(name = "admin_cedula")
    private String cedula;

    @Column(name = "admin_celular")
    private String celular;

    @Column(name = "admin_id_usuario") // Este es el nombre de la columna que actúa como clave foránea
    private Integer idUsuario;

}
