package com.tesis.backend_tesis.repository.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "carrera")
public class Carrera {


    @Id
    @Column(name = "carr_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carr_id_seq")
    @SequenceGenerator(name = "carr_id_seq", sequenceName = "carr_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Column(name = "carr_nombre")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "carr_fac_id",  referencedColumnName = "fac_id",nullable = false) // Clave foránea
    private Facultad facultad;

    @ManyToOne
    @JoinColumn(name = "carr_director_id", referencedColumnName = "docen_id", nullable = true, unique = true)
    private Docente director;

    @OneToOne
    @JoinColumn(name = "carr_usua_id", referencedColumnName = "usua_id", unique = true, nullable = true)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "carr_coordinador_id", referencedColumnName = "docen_id", nullable = true,unique = true)
    private Docente coordinador;

    @OneToMany(mappedBy = "carrera", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Estudiante> estudiantes;  // Relación con los docentes de esta facultad

    @OneToMany(mappedBy = "carrera", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Secretaria> secretarias;  // Relación con los docentes de esta facultad




}
