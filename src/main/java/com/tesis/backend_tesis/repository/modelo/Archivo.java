package com.tesis.backend_tesis.repository.modelo;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "archivo")
public class Archivo {


    @Id
    @Column(name = "archivo_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "archivo_id_seq")
    @SequenceGenerator(name = "archivo_id_seq", sequenceName = "archivo_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;  // ID único para identificar cada archivo

    @Column(name = "archivo_nombre")
    private String nombre;  // Nombre del archivo, como 'mi-documento.pdf'

    @Column(name = "archivo_url")
    private String url;

    @Column(name = "archivo_fecha_creacion")
    private LocalTime fechaCreacion;

    // Relación Muchos a Uno con Usuario
    @ManyToOne
    @JoinColumn(name = "archivo_id_usuario", nullable = false)
    private Usuarios usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prop_id", referencedColumnName = "prop_id", nullable = true)
    private Propuesta propuesta;

    public Archivo(String nombreArchivo, String url, Usuarios usuario) {
        this.nombre = nombreArchivo;
        this.url = url;
        this.fechaCreacion = LocalTime.now();
        this.usuario = usuario;
    }
}
