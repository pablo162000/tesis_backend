package com.tesis.backend_tesis.repository.modelo;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "secretaria")
public class Secretaria {

    @Id
    @Column(name = "secre_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secre_id_seq")
    @SequenceGenerator(name = "secre_id_seq", sequenceName = "secre_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "secre_usua_id", referencedColumnName = "usua_id", unique = true, nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "secre_carr_id", referencedColumnName = "carr_id", nullable = false)
    private Carrera carrera;

}
