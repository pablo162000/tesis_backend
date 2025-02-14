package com.tesis.backend_tesis.repository.modelo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "estudiante", uniqueConstraints = {@UniqueConstraint(columnNames = {"estu_correo"})})
public class Estudiantes {

    @Id
    @Column(name = "estu_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estudiantes_estu_id_seq")
    @SequenceGenerator(name = "estudiantes_estu_id_seq", sequenceName = "estudiantes_estu_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Column(name = "estu_primer_nombre")
    private String primer_nombre;

    @Column(name = "estu_segundo_nombre")
    private String segundo_nombre;

    @Column(name = "estu_primer_apellido")
    private String primer_apellido;

    @Column(name = "estu_segundo_apellido")
    private String segundo_apellido;

    @Column(name = "estu_cedula")
    private String cedula;

    @Column(name = "estu_activo")
    private Boolean activo;


    @OneToOne
    @JoinColumn(name = "estu_id_usuario") // Este es el nombre de la columna que actúa como clave foránea
    private Usuarios usuario;

}
