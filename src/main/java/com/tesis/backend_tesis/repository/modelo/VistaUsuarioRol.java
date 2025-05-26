package com.tesis.backend_tesis.repository.modelo;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "view_usuarios_roles")
@Getter
public class VistaUsuarioRol {

    @Id
    @Column(name = "usuario_rol_id")
    private Integer idUsuarioRol;

    @Column(name = "rol_id")
    private Integer idRol;

    @Column(name = "rol_nombre")
    private String nombreRol;

    @Column(name = "usua_correo")
    private String correoUsuario;

    @Column(name = "usua_id")
    private Integer idUsuario;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "carr_nombre")
    private String carrera;


}
