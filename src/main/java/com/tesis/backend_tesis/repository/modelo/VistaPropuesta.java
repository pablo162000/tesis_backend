package com.tesis.backend_tesis.repository.modelo;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "view_propuestas")
@Getter
public class VistaPropuesta {

    @Id
    @Column(name = "prop_id")
    private Integer id;

    @Column(name = "prop_carrera")
    private String carrera;

    @Column(name = "prop_estado_validacion")
    @Enumerated(EnumType.STRING)
    private EstadoValidacion estadoValidacion;

    @Column(name = "prop_estado_aprobacion")
    @Enumerated(EnumType.STRING)
    private EstadoAprobacion estadoAprobacion;

    @Column(name = "prop_observaciones")
    private String observaciones;

    @Column(name = "prop_periodo")
    private String periodo;

    @Column(name = "prop_tipo")
    private String tipo;

    @Column(name = "prop_categoria")
    private String categoria;

    @Column(name = "archivo_id")
    private Integer archivoEstudianteId;

    @Column(name = "archivo_fecha_creacion")
    private String archivoEstudianteFecha;

    @Column(name = "archivo_nombre")
    private String archivoEstudianteNombre;

    @Column(name = "prop_tema")
    private String tema;

    // Primer estudiante
    @Column(name = "primer_estudiante")
    private String primerEstudiante;

    @Column(name = "primer_usua_id")
    private Integer primerUsuaId;

    @Column(name = "primer_estu_id")
    private Integer primerEstuId;

    @Column(name = "primer_carrera")
    private String primerCarrera;

    @Column(name = "primer_facultad")
    private String primerFacultad;

    // Segundo estudiante
    @Column(name = "segundo_estudiante")
    private String segundoEstudiante;

    @Column(name = "segundo_usua_id")
    private Integer segundoUsuaId;

    @Column(name = "segundo_estu_id")
    private Integer segundoEstuId;

    @Column(name = "segundo_carrera")
    private String segundoCarrera;

    @Column(name = "segundo_facultad")
    private String segundoFacultad;

    // Tercer estudiante
    @Column(name = "tercer_estudiante")
    private String tercerEstudiante;

    @Column(name = "tercer_usua_id")
    private Integer tercerUsuaId;

    @Column(name = "tercer_estu_id")
    private Integer tercerEstuId;

    @Column(name = "tercer_carrera")
    private String tercerCarrera;

    @Column(name = "tercer_facultad")
    private String tercerFacultad;

    // Tutor
    @Column(name = "tutor")
    private String tutor;

    @Column(name = "tutor_usua_id")
    private Integer tutorUsuaId;

    @Column(name = "tutor_docen_id")
    private Integer tutorDocenteId;

    @Column(name = "tutor_facultad")
    private String tutorFacultad;

    // Revisión
    @Column(name = "revi_id")
    private Integer revisionId;

    @Column(name = "revi_numero")
    private Integer revisionNumero;

    // Revisor 1
    @Column(name = "revisor_usua_1_id")
    private Integer revisor1UsuaId;

    @Column(name = "revisor_docen_1_id")
    private Integer revisor1DocenId;

    @Column(name = "primer_revisor")
    private String primerRevisor;

    @Column(name = "archivo_id_revisor_1")
    private Integer archivoIdRevisor1;

    @Column(name = "archivo_fecha_revisor_1")
    private String archivoFechaRevisor1;

    @Column(name = "archivo_nombre_revisor_1")
    private String archivoNombreRevisor1;

    @Column(name = "nota_1")
    private Double nota1;

    @Column(name = "observacion_1")
    private String observacion1;

    // Revisor 2
    @Column(name = "revisor_usua_2_id")
    private Integer revisor2UsuaId;

    @Column(name = "revisor_docen_2_id")
    private Integer revisor2DocenId;

    @Column(name = "segundo_revisor")
    private String segundoRevisor;

    @Column(name = "archivo_id_revisor_2")
    private Integer archivoIdRevisor2;

    @Column(name = "archivo_fecha_revisor_2")
    private String archivoFechaRevisor2;

    @Column(name = "archivo_nombre_revisor_2")
    private String archivoNombreRevisor2;

    @Column(name = "nota_2")
    private Double nota2;

    @Column(name = "observacion_2")
    private String observacion2;


}
