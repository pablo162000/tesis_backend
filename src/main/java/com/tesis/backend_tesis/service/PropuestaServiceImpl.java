package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.repository.IPropuestaRepository;
import com.tesis.backend_tesis.repository.modelo.Propuesta;
import com.tesis.backend_tesis.service.dto.PropuestaDTO;
import com.tesis.backend_tesis.service.dto.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PropuestaServiceImpl implements IPropuestaService{

    @Autowired
    private Converter converter;

    @Autowired
    private IPropuestaRepository propuestaRepository;

    @Override
    public Propuesta guardar(Propuesta propuesta) {
        return this.propuestaRepository.crear(propuesta);
    }

    @Override
    public Propuesta buscar(Integer id) {
        return null;
    }

    @Override
    public List<PropuestaDTO> buscarPorIdEstudiante(Integer idEstudiante) {
        return this.converter.toDTOList(this.propuestaRepository.findByEstudianteId(idEstudiante));
    }
}
