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
@Table(name = "estudiante")

public class Estudiante {

    @Id
    @Column(name = "estu_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estu_id_seq")
    @SequenceGenerator(name = "estu_id_seq", sequenceName = "estu_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "estu_usua_id", referencedColumnName = "usua_id", unique = true, nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estu_carr_id", referencedColumnName = "carr_id", nullable = false)
    private Carrera carrera;

    // Relación bidireccional con Propuesta
    @OneToMany(mappedBy = "estudiante1", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Propuesta> propuestasPrimero;

    @OneToMany(mappedBy = "estudiante2", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Propuesta> propuestasSegundo;

    @OneToMany(mappedBy = "estudiante3", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Propuesta> propuestasTercero;




}
