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
    @JoinColumn(name = "revi_archivo_id", referencedColumnName = "archivo_id")
    private Archivo archivoSubidoEstudiantes;

    @OneToMany(mappedBy = "primeraRevision", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Propuesta> propuestasRevisionPrimera;

    @OneToMany(mappedBy = "segundaRevision", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Propuesta> propuestasRevisionSegunda;
/*
    @OneToMany(mappedBy = "revision", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<EvaluacionRevisor> evaluaciones;

 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revi_evaluacion_primera_id", referencedColumnName = "eva_revi_id", nullable = true)
    @ToString.Exclude
    private EvaluacionRevisor evaluacionDocente1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revi_evaluacion_segunda_id", referencedColumnName = "eva_revi_id", nullable = true)
    @ToString.Exclude
    private EvaluacionRevisor evaluacionDocente2;



}
