package com.tesis.backend_tesis.repository.modelo;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Column(name = "archivo_nombre", length = 1500)
    private String nombre;  // Nombre del archivo, como 'mi-documento.pdf'

    @Column(name = "archivo_url", length = 1500)
    private String url;

    @Builder.Default
    @Column(name = "archivo_fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Relación Muchos a Uno con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archivo_usua_id",referencedColumnName = "usua_id", nullable = false)
    private Usuario usuario;

    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prop_id", referencedColumnName = "prop_id", nullable = true)
    private Propuesta propuesta;

     */

    // Relación con Revision
    @OneToMany(mappedBy = "archivoSubidoEstudiantes", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Revision> revisiones;

    // Relación con EvaluacionRevisor
    @OneToMany(mappedBy = "archivoRevisado1", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Revision> evaluacionesRevisor1;

    @OneToMany(mappedBy = "archivoRevisado2", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Revision> evaluacionesRevisor2;



    public Archivo(String nombreArchivo, String url, Usuario usuario) {
        this.nombre = nombreArchivo;
        this.url = url;
        this.fechaCreacion = LocalDateTime.now();
        this.usuario = usuario;
    }
}
