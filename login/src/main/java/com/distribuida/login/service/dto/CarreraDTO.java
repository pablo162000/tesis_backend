package com.distribuida.login.service.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarreraDTO {

    private Integer id;

    private String nombre;

    private String facultad;

    private List<Integer> idUsuarios;
}
