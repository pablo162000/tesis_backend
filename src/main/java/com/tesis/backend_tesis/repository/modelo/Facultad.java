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
@Table(name = "facultad")
public class Facultad {

    @Id
    @Column(name = "fac_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fac_id_seq")
    @SequenceGenerator(name = "fac_id_seq", sequenceName = "fac_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Column(name = "fac_nombre")
    private String nombre;

    @Column(name = "fac_correo")
    private String correo;

    @OneToMany(mappedBy = "facultad", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Carrera> carreras;

    
    @OneToMany(mappedBy = "facultad", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Docente> docentes;  // Relación con los docentes de esta facultad


}
