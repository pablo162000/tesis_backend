package com.distribuida.alumno.repository.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private LocalDateTime fechaCreacion;

    @Column(name = "archivo_id_usuario")
    private Integer idUsuario;

    public Archivo(String nombreArchivo, String url, Integer idUsuario) {
        this.nombre = nombreArchivo;
        this.url = url;
        this.fechaCreacion = LocalDateTime.now();
        this.idUsuario = idUsuario;
    }


}
