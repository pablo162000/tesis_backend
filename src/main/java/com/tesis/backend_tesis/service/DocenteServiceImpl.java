package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.repository.IDocenteRepository;
import com.tesis.backend_tesis.repository.modelo.Docente;
import com.tesis.backend_tesis.repository.modelo.Estudiante;
import com.tesis.backend_tesis.service.dto.DocenteDTO;
import com.tesis.backend_tesis.service.dto.EstudianteDTO;
import com.tesis.backend_tesis.service.dto.utils.Converter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocenteServiceImpl implements IDocenteService {

    private static final Logger logger = LogManager.getLogger(DocenteServiceImpl.class);

    @Autowired
    private IDocenteRepository docenteRepository;

    @Autowired
    private Converter converter;

    @Override
    public DocenteDTO insertar(DocenteDTO docenteDTO) {

        try {
            if (docenteDTO == null) {
                logger.warn("Intento de insertar un docente NULL.");
                return null;
            }

            // Convertir DTO a Entidad
            Docente docente = this.converter.toEntity(docenteDTO);

            // Guardar usuario
            this.docenteRepository.insert(docente);
            logger.info("Estudiante ingresado con IDUSUARIO {} insertado correctamente.", docente.getUsuario().getId());
            return this.converter.toDTO(docente);

        } catch (Exception e) {
            logger.error("Error al insertar estudiante con IDUSUARIO {}: {}", docenteDTO.getIdUsuario(), e.getMessage(), e);
            throw new RuntimeException("Error al estudiante usuario con IDUSUARIO: " + docenteDTO.getIdUsuario(), e);
        }

    }

    @Override
    public DocenteDTO buscarPorIdUsuario(Integer idUsuario) {
        try {
            if (idUsuario <= 0) {
                logger.warn("El id no puede ser 0 o negativo.");
                return null;
            }

            Docente docente = this.docenteRepository.findByIdUsuario(idUsuario);
            DocenteDTO docenteDTO = this.converter.toDTO(docente);

            logger.info("Docente recuperado con IDUSUARIO {}  correctamente.", docenteDTO.getIdUsuario());
            return docenteDTO;

        } catch (Exception e) {
            logger.error("Error al buscar Docente con IDUSUARIO {}: {}", idUsuario, e.getMessage(), e);
            throw new RuntimeException("Error al buscar Docente con IDUSUARIO: " + idUsuario, e);
        }    }


}
