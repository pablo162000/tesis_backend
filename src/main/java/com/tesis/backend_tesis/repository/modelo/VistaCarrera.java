package com.tesis.backend_tesis.repository.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "view_carreras")
@Getter
public class VistaCarrera {


    @Id
    @Column(name = "carr_id")
    private Integer idCarrera;

    @Column(name = "fac_id")
    private Integer idFacultad;

    @Column(name = "facultad")
    private String facultad;

    @Column(name = "carrera")
    private String carrera;

    @Column(name = "correo_direccion")
    private String correoDireccion;

    @Column(name = "director_id")
    private Integer idDirector;

    @Column(name = "nombre_director")
    private String nombreDirector;

    @Column(name = "correo_director")
    private String correoDirector;

    @Column(name = "coordinador_id")
    private Integer idCoordinador;

    @Column(name = "nombre_coordinador")
    private String nombreCoordinador;

    @Column(name = "correo_coordinador")
    private String correoCoordinador;



}
