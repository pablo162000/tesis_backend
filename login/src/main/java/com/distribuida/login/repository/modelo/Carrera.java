package com.distribuida.login.repository.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    @Column(name = "carr_facultad")
    private String facultad;

    @OneToMany(mappedBy = "carrera")
    private List<Usuario> usuarios; // Relación uno a muchos con Usuario
}
