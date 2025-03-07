package com.distribuida.administrativos.repository.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "view_docentes") // Nombre de la vista en PostgreSQL
@Getter
@Setter
public class VistaDocente {

    @Id
    private Integer usuaId;
    private Boolean usuaActivo;
    private String usuaCorreo;
    private String usuaFechaCreacion;
    private String docenCedula;
    private String nombre;
    private String apellido;
    private String docenCelular;
    private String carrNombre;
}