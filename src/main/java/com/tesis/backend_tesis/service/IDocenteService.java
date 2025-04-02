package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.service.dto.DocenteDTO;
import com.tesis.backend_tesis.service.dto.EstudianteDTO;

public interface IDocenteService {

    public DocenteDTO insertar(DocenteDTO docenteDTO);

    public DocenteDTO buscarPorIdUsuario(Integer idUsuario);
}


