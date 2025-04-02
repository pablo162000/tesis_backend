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
@Table(name = "usuario_rol")
public class UsuarioRol {

    @Id
    @Column(name= "usuario_rol_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usua_rol_id_seq")
    @SequenceGenerator(name = "usua_rol_id_seq", sequenceName = "usua_rol_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usua_id", referencedColumnName = "usua_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "rol_id", referencedColumnName = "rol_id", nullable = false)
    private Rol rol;


}
