package com.tesis.backend_tesis.repository.modelo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "revision")
public class Revision {

    @Id
    @Column(name = "revi_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "revi_id_seq")
    @SequenceGenerator(name = "revi_id_seq", sequenceName = "revi_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Column(name = "revi_numero", nullable = false)
    private Integer numeroRevision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revi_archivo_estudiantes_id", referencedColumnName = "archivo_id")
    private Archivo archivoSubidoEstudiantes;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revi_docente_id_1",referencedColumnName = "docen_id",nullable = true)
    @ToString.Exclude
    private Docente revisor1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revi_archivo_id_1", referencedColumnName = "archivo_id")
    @ToString.Exclude
    private Archivo archivoRevisado1;

    @Column(name = "revi_observaciones_1", length = 2000)
    private String observaciones1;

    @Column(name = "revi_nota_1")
    private Double nota1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revi_docente_id_2",referencedColumnName = "docen_id",nullable = true)
    @ToString.Exclude
    private Docente revisor2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revi_archivo_id_2", referencedColumnName = "archivo_id")
    @ToString.Exclude
    private Archivo archivoRevisado2;

    @Column(name = "revi_observaciones_2", length = 2000)
    private String observaciones2;

    @Column(name = "revi_nota_2")
    private Double nota2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revi_propuesta_id", referencedColumnName = "prop_id")
    @ToString.Exclude
    private Propuesta propuesta;

    /*
    @OneToMany(mappedBy = "primeraRevision", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Propuesta> propuestasRevisionPrimera;

    @OneToMany(mappedBy = "segundaRevision", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Propuesta> propuestasRevisionSegunda;

     */



/*
    @OneToMany(mappedBy = "revision", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<EvaluacionRevisor> evaluaciones;

 */



}
