package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.repository.modelo.Propuesta;
import com.tesis.backend_tesis.service.dto.PropuestaDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPropuestaService {


    public Propuesta guardar(Propuesta propuesta);

    public Propuesta buscar(Integer id);

    public List<PropuestaDTO> buscarPorIdEstudiante(Integer idEstudiante);
}
