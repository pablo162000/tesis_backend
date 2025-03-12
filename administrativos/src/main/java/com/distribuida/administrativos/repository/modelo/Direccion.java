package com.distribuida.administrativos.repository.modelo;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "direccion")
public class Direccion {

    @Id
    @Column(name = "direcc_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_id_seq")
    @SequenceGenerator(name = "admin_id_seq", sequenceName = "admin_id_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Column(name = "direcc_correo")
    private String correo;





}
