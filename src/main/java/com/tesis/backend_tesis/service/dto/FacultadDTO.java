package com.tesis.backend_tesis.service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacultadDTO {

    private Integer id;

    private String nombre;

    private String correo;

    private List<Integer> idCarreras;

}
