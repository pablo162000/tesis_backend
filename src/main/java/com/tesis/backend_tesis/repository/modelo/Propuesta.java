package com.tesis.backend_tesis.repository.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "propuesta")
public class Propuesta {


    @Id
    @Column(name = "prop_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prop_id_seq")
    @SequenceGenerator(name = "prop_id_seq", sequenceName = "prop_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Column(name = "prop_carrera", nullable = false)
    private String carrera;

    @Column(name = "prop_tipo", nullable = false)
    private String tipo;

    @Column(name = "prop_tema", nullable = false, unique = true)
    private String tema;

    @Column(name = "prop_categoria", nullable = false)
    private String categoria;

    @Column(name = "prop_estado_validacion", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoValidacion estadoValidacion;

    @Column(name = "prop_estado_aprobacion", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoAprobacion estadoAprobacion;

    @Column(name = "prop_observaciones", nullable = true)
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prop_estu_id_1",referencedColumnName = "estu_id", nullable = false)
    @ToString.Exclude
    private Estudiante estudiante1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prop_estu_id_2", referencedColumnName = "estu_id", nullable = true)
    @ToString.Exclude
    private Estudiante estudiante2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prop_estu_id_3", referencedColumnName = "estu_id", nullable = true)
    @ToString.Exclude
    private Estudiante estudiante3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prop_tutor_id", referencedColumnName = "docen_id", nullable = true)
    @ToString.Exclude
    private Docente tutor;
/*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prop_revi_primera_id", referencedColumnName = "revi_id", nullable = false)
    @ToString.Exclude
    private Revision primeraRevision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prop_revi_segunda_id", referencedColumnName = "revi_id", nullable = true)
    @ToString.Exclude
    private Revision segundaRevision;

 */


    @OneToMany(mappedBy = "propuesta", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Revision> revisiones;



    @Column(name = "prop_periodo", length = 80, nullable = false)
    private String periodo;


}
