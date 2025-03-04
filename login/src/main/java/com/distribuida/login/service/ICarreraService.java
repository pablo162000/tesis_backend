package com.distribuida.login.service;

import com.distribuida.login.repository.modelo.Carrera;
import com.distribuida.login.service.dto.CarreraDTO;

import java.util.List;

public interface ICarreraService {

    public List<CarreraDTO> buscarTodos();
    public Carrera findById(Integer id);
}
