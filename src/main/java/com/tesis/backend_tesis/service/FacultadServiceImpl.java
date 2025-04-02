package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.repository.ICarreraRepository;
import com.tesis.backend_tesis.repository.IFacultadRepository;
import com.tesis.backend_tesis.repository.modelo.Carrera;
import com.tesis.backend_tesis.repository.modelo.Facultad;
import com.tesis.backend_tesis.service.dto.CarreraDTO;
import com.tesis.backend_tesis.service.dto.FacultadDTO;
import com.tesis.backend_tesis.service.dto.utils.Converter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacultadServiceImpl implements IFacultadService {


    private static final Logger logger = LogManager.getLogger(FacultadServiceImpl.class);

    @Autowired
    private IFacultadRepository facultadRepository;

    @Autowired
    private Converter converter;

    @Override
    public FacultadDTO buscarFacultadPorNombre(String nombre) {
        return null;
    }

    @Override
    public FacultadDTO buscarFacultadPorId(Integer id) {
        try {
            if (id <= 0) {
                logger.warn("El id no puede ser 0 o negativo para buscar una Facultad.");
                return null;
            }

            Facultad facultad = this.facultadRepository.findById(id);
            FacultadDTO facultadDTO = this.converter.toDTO(facultad);

            logger.info("Facultad recuperad con ID {}  correctamente.", facultadDTO.getId());
            return facultadDTO;

        } catch (Exception e) {
            logger.error("Error al buscar Facultad con ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al buscar Facultad con ID: " + id, e);
        }
    }

}
