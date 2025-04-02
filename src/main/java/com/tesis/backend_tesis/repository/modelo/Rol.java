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
@Table(name = "rol")
public class Rol {


    @Id
    @Column(name = "rol_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rol_id_seq")
    @SequenceGenerator(name = "rol_id_seq", sequenceName = "rol_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Column(name = "rol_nombre", unique = true, nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<UsuarioRol> usuariosRoles;  // Relación con los docentes de esta facultad


}
