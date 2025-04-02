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
@Table(name = "evaluacion_revisor")
public class EvaluacionRevisor {

    @Id
    @Column(name = "eva_revi_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eva_revi_id_seq")
    @SequenceGenerator(name = "eva_revi_id_seq", sequenceName = "eva_revi_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Column(name = "eva_revi_numero")
    private Integer numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eva_revi_docente_id",referencedColumnName = "docen_id",nullable = false)
    @ToString.Exclude
    private Docente revisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eva_revi_archivo_id", referencedColumnName = "archivo_id")
    @ToString.Exclude
    private Archivo archivoRevisado;
/*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eva_revi_revision_id", referencedColumnName = "revi_id",nullable = false)
    @ToString.Exclude
    private Revision revision;

 */

    @Column(name = "eva_revi_observaciones")
    private String observaciones;

    @Column(name = "eva_revi_nota")
    private Double nota;

    @OneToMany(mappedBy = "evaluacionDocente1", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Revision> revisionesEvalucionesPrimera;

    @OneToMany(mappedBy = "evaluacionDocente2", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Revision> revisionesEvalucionesSegunda;


}
