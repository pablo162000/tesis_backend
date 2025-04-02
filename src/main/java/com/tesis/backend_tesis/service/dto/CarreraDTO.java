package com.tesis.backend_tesis.service.dto;

import com.tesis.backend_tesis.repository.modelo.Facultad;
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
    private Integer idFacultad;
    private Integer idDirector;
    private Integer idCoordinador;
    private Integer idUsuario;
    private List<Integer> idEstudiantes;
    private List<Integer> idSecretarias;

}
