package com.tesis.backend_tesis.repository.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

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

    @ManyToOne
    @JoinColumn(name = "prop_id_estu_primero", referencedColumnName = "estu_id", nullable = false)
    //@JsonBackReference
    private Estudiantes estudiantePrimero;

    @ManyToOne
    @JoinColumn(name = "prop_id_estu_segundo", referencedColumnName = "estu_id")
    //@JsonBackReference
    private Estudiantes estudianteSegundo;

    @ManyToOne
    @JoinColumn(name = "prop_id_estu_tercero", referencedColumnName = "estu_id")
    //@JsonBackReference
    private Estudiantes estudianteTercero;

    @Column(name = "prop_tema")
    private String tema;

    @ManyToOne
    @JoinColumn(name = "archivo_id", referencedColumnName = "archivo_id")
    private Archivo archivo;  // Relación con la entidad Archivo

    @Column(name = "prop_observacion", length = 500, nullable = false)
    private String observacion;

    @Column(name = "prop_validacion")
    private Boolean validacion;

    @Column(name = "prop_periodo", length = 80, nullable = false)
    private String periodo;

    // Guardar solo el ID del docente
    @Column(name = "prop_id_docente", nullable = false)
    private Integer idDocente;

    @Column(name = "prop_id_estu_creacion", nullable = false)
    private Integer idEstuCreacion;

 /*
    @Column(name = "prop_id_fecha_creacion", nullable = false)
    private LocalTime fechaCreacion;


    @Column(name = "prop_activo", nullable = false)
    private Boolean propActivo;
     */
}
