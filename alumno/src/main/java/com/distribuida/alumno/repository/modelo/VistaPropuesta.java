package com.distribuida.alumno.repository.modelo;


import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "vista_propuestas") // Nombre de la vista en PostgreSQL
@Getter
public class VistaPropuesta {

    @Id
    @Column(name = "prop_id")
    private Long id;

    @Column(name = "prop_tema")
    private String tema;

    @Column(name = "prop_observacion")
    private String observacion;

    @Column(name = "prop_validacion")
    @Enumerated(EnumType.STRING)
    private EstadoValidacion validacion;

    @Column(name = "prop_estado_aprobacion")
    private Boolean estadoAprobacion;

    @Column(name = "prop_fecha_aprobacion")
    private LocalDateTime fechaAprobacion;

    @Column(name = "prop_periodo")
    private String periodo;

    @Column(name = "primer_estudiante")
    private String primerEstudiante;

    @Column(name = "segundo_estudiante")
    private String segundoEstudiante;

    @Column(name = "tercer_estudiante")
    private String tercerEstudiante;

    @Column(name = "prop_fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "nombre_docente_tutor")
    private String nombreDocenteTutor;

    @Column(name = "archivo_nombre_designacion_tutor")
    private String archivoNombreDesignacionTutor;

    @Column(name = "archivo_url_designacion_tutor")
    private String archivoUrlDesignacionTutor;

    @Column(name = "archivo_nombre_designacion_revisores")
    private String archivoNombreDesignacionRevisores;

    @Column(name = "archivo_url_designacion_revisores")
    private String archivoUrlDesignacionRevisores;

    @Column(name = "prop_fecha_designacion_revisores")
    private LocalDateTime fechaDesignacionRevisores;

    @Column(name = "nombre_docente_primero_revisor")
    private String nombreDocentePrimeroRevisor;

    @Column(name = "archivo_nombre_rubrica_revisor1")
    private String archivoNombreRubricaRevisor1;

    @Column(name = "archivo_url_rubrica_revisor1")
    private String archivoUrlRubricaRevisor1;

    @Column(name = "archivo_fecha_creacion_rubrica_revisor1")
    private LocalDateTime archivoFechaCreacionRubricaRevisor1;

    @Column(name = "prop_nota_docente_primero_revisor")
    private Double notaDocentePrimeroRevisor;

    @Column(name = "prop_observacion_docente_primero_revisor")
    private String observacionDocentePrimeroRevisor;

    @Column(name = "nombre_docente_segundo_revisor")
    private String nombreDocenteSegundoRevisor;

    @Column(name = "archivo_nombre_rubrica_revisor2")
    private String archivoNombreRubricaRevisor2;

    @Column(name = "archivo_url_rubrica_revisor2")
    private String archivoUrlRubricaRevisor2;

    @Column(name = "archivo_fecha_creacion_rubrica_revisor2")
    private LocalDateTime archivoFechaCreacionRubricaRevisor2;

    @Column(name = "prop_nota_docente_segundo_revisor")
    private Double notaDocenteSegundoRevisor;

    @Column(name = "prop_observacion_docente_segundo_revisor")
    private String observacionDocenteSegundoRevisor;

    @Column(name = "archivo_nombre_propuesta")
    private String archivoNombrePropuesta;

    @Column(name = "archivo_url_propuesta")
    private String archivoUrlPropuesta;
}