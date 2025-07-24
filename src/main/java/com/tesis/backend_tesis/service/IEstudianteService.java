package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.service.dto.EstudianteDTO;

public interface IEstudianteService {

    public EstudianteDTO insertar(EstudianteDTO estudianteDTO);
    public EstudianteDTO buscarPorIdUsuario(Integer idUsuario);

}
