package com.distribuida.login.service;

import com.distribuida.login.repository.ICarreraRepository;
import com.distribuida.login.repository.modelo.Carrera;
import com.distribuida.login.service.dto.CarreraDTO;
import com.distribuida.login.service.dto.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarreraServiceImpl implements ICarreraService{

    @Autowired
    private ICarreraRepository carreraRepository;

    @Autowired
    private Converter converter;

    @Override
    public List<CarreraDTO> buscarTodos() {
        return this.converter.toCarreraDTOList(this.carreraRepository.findAll());
    }

    @Override
    public Carrera findById(Integer id) {
        return this.carreraRepository.findById(id);
    }
}
