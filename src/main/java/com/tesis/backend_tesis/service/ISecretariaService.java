package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.service.dto.EstudianteDTO;
import com.tesis.backend_tesis.service.dto.SecretariaDTO;

public interface ISecretariaService {

    public SecretariaDTO insertar(SecretariaDTO secretariaDTO);
    public SecretariaDTO buscarPorIdUsuario(Integer idUsuario);
}
