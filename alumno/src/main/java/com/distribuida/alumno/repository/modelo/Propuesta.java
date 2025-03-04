package com.distribuida.alumno.repository.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString(exclude = "estudiante")
@Table(name = "propuesta")
public class Propuesta {

    @Id
    @Column(name = "prop_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prop_id_seq")
    @SequenceGenerator(name = "prop_id_seq", sequenceName = "prop_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "prop_id_primer_estu", referencedColumnName = "estu_id", nullable = false)
    //@JsonBackReference
    private Estudiante primerEstudiante;

    @ManyToOne
    @JoinColumn(name = "prop_id_segundo_estu", referencedColumnName = "estu_id")
    //@JsonBackReference
    private Estudiante segundoEstudiante;

    @ManyToOne
    @JoinColumn(name = "prop_id_tercer_estu", referencedColumnName = "estu_id")
    //@JsonBackReference
    private Estudiante tercerEstudiante;

    @Column(name = "prop_tema", nullable = false)
    private String tema;

    @ManyToOne
    @JoinColumn(name = "prop_id_archivo", referencedColumnName = "archivo_id", nullable = false)
    private Archivo archivoPropuesta;  // Relación con la entidad Archivo

    @Column(name = "prop_observacion", length = 500, nullable = true)
    private String observacion;

    @Column(name = "prop_validacion", nullable = false)
    @Enumerated(EnumType.STRING)  // O @Enumerated(EnumType.ORDINAL) si prefieres almacenar como 0, 1, 2
    private EstadoValidacion validacion;

    @Column(name = "prop_periodo", length = 80, nullable = false)
    private String periodo;

    // Guardar solo el ID del docente tutor
    @Column(name = "prop_id_docente_tutor", nullable = true)
    private Integer idDocenteTutor;

    @Column(name = "prop_id_estu_creacion", nullable = false)
    private Integer idEstuCreacion;

    @ManyToOne
    @JoinColumn(name = "prop_id_archivo_designacion_revisores", referencedColumnName = "archivo_id", nullable = true)
    private Archivo archivoDesignacionRevisores;  // Relación con la entidad Archivo

    @Column(name = "prop_fecha_designacion_revisores", nullable = true)
    private LocalDateTime fechaDesignacionRevisores;

    // Guardar solo el ID del docente revisor
    @Column(name = "prop_id_docente_primero_revisor", nullable = true)
    private Integer idDocentePrimerRevisor;

    @Column(name = "prop_nota_docente_primero_revisor", nullable = true)
    private Double notaPrimerRevisor;

    @Column(name = "prop_fecha_docente_primero_revisor", nullable = true)
    private LocalDateTime fechaPrimerRevisor;

    @ManyToOne
    @JoinColumn(name = "prop_id_archivo_rubrica_primero_revisor", referencedColumnName = "archivo_id", nullable = true)
    private Archivo archivoRubricaPrimerRevisor;  // Relación con la entidad Archivo

    // Guardar solo el ID del docente revisor
    @Column(name = "prop_id_docente_segundo_revisor", nullable = true)
    private Integer idDocenteSegundoRevisor;

    @Column(name = "prop_nota_docente_segundo_revisor", nullable = true)
    private Double notaSegundoRevisor;

    @Column(name = "prop_fecha_docente_segundo_revisor", nullable = true)
    private LocalDateTime fechaSegundoRevisor;

    @ManyToOne
    @JoinColumn(name = "prop_id_archivo_rubrica_segundo_revisor", referencedColumnName = "archivo_id", nullable = true)
    private Archivo archivoRubricaSegundoRevisor;  // Relación con la entidad Archivo

    @Column(name = "prop_estado_aprobacion", nullable = false)
    private Boolean estadoAprobacion;

    @Column(name = "prop_fecha_aprobacion", nullable = true)
    private LocalDateTime fechaAprobacion;

    @Column(name = "prop_fecha_envio", nullable = true)
    private LocalDateTime fechaEnvio;

    @ManyToOne
    @JoinColumn(name = "prop_id_archivo_designacion_tutor", referencedColumnName = "archivo_id", nullable = true)
    private Archivo archivoDesignacionTutor;  // Relación con la entidad Archivo


}
