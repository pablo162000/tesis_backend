package com.tesis.backend_tesis.service.dto;

import com.tesis.backend_tesis.repository.modelo.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocenteDTO {

    private Integer id;
    private Integer idUsuario;
    private Integer idFacultad;

}
