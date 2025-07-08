package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.repository.modelo.CarreraRequest;
import com.tesis.backend_tesis.repository.modelo.RegistroRequest;
import com.tesis.backend_tesis.service.dto.CarreraDTO;

public interface ICarreraService {


    public Boolean insertar(CarreraRequest carreraRequest);
    public CarreraDTO buscarCarreraPorId(Integer id);
    public Boolean insertarUsuarioCarrera(RegistroRequest registroRequest);
    public CarreraDTO buscarPorIDUsuario(Integer idUsuario);
    public Boolean insertarAutoridadesCarrera(Integer idCarrera, Integer idUsuario);


}
