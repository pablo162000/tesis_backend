package com.distribuida.administrativos.repository.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "view_estudiantes") // Nombre de la vista en PostgreSQL
@Getter
public class VistaEstudiante {

    @Id
    private Integer usuaId;
    private Boolean usuaActivo;
    private String usuaCorreo;
    private String usuaFechaCreacion;
    private String estuCedula;
    private String nombre;
    private String apellido;
    private String estuCelular;
    private String carrNombre;
}