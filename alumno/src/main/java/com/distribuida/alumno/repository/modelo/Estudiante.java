package com.distribuida.alumno.repository.modelo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString(exclude = {"propuestasPrimero", "propuestasSegundo", "propuestasTercero"})
@Table(name = "estudiante",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"estu_correo"}),
                @UniqueConstraint(columnNames = {"estu_cedula"})
        })
public class Estudiante {

    @Id
    @Column(name = "estu_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estu_id_seq")
    @SequenceGenerator(name = "estu_id_seq", sequenceName = "estu_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Column(name = "estu_primer_nombre")
    private String primerNombre;

    @Column(name = "estu_segundo_nombre")
    private String segundoNombre;

    @Column(name = "estu_primer_apellido")
    private String primerApellido;

    @Column(name = "estu_segundo_apellido")
    private String segundoApellido;

    @Column(name = "estu_cedula")
    private String cedula;

    @Column(name = "estu_celular")
    private String celular;

    @Column(name = "estu_id_usuario") // Este es el nombre de la columna que actúa como clave foránea
    private Integer idUsuario;

    // Relación bidireccional con Propuesta
    @OneToMany(mappedBy = "primerEstudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Propuesta> propuestasPrimero;

    @OneToMany(mappedBy = "segundoEstudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Propuesta> propuestasSegundo;



    @OneToMany(mappedBy = "tercerEstudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Propuesta> propuestasTercero;


}