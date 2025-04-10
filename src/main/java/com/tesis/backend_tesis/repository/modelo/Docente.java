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
@Table(name = "docente")
public class Docente {

    @Id
    @Column(name = "docen_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "docen_id_seq")
    @SequenceGenerator(name = "docen_id_seq", sequenceName = "docen_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "docen_usua_id", referencedColumnName = "usua_id", unique = true, nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docen_fac_id", referencedColumnName = "fac_id", nullable = false)
    private Facultad facultad;

    /*
    @OneToMany(mappedBy = "director", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Carrera> carrerasDirector;
     */

    @OneToMany(mappedBy = "coordinador", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Carrera> carrerasCoordinador;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Propuesta> propuestas;

    @OneToMany(mappedBy = "revisor1", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Revision> revisionesDocente1;

    @OneToMany(mappedBy = "revisor2", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Revision> revisionesDocente2;




}
