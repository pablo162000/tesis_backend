package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.repository.IEstudianteRepository;
import com.tesis.backend_tesis.repository.modelo.Estudiante;
import com.tesis.backend_tesis.service.dto.EstudianteDTO;
import com.tesis.backend_tesis.service.dto.utils.Converter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstudianteServiceImpl implements IEstudianteService{

    private static final Logger logger = LogManager.getLogger(EstudianteServiceImpl.class);

    @Autowired
    private Converter converter;

    @Autowired
    private IEstudianteRepository estudianteRepository;

    @Override
    public EstudianteDTO insertar(EstudianteDTO estudianteDTO) {
        try {
            if (estudianteDTO == null) {
                logger.warn("Intento de insertar un estudiante NULL.");
                return null;
            }
            // Convertir DTO a Entidad
            Estudiante estudiante = this.converter.toEntity(estudianteDTO);

            // Guardar usuario
            this.estudianteRepository.insert(estudiante);
            logger.info("Estudiante ingresado con IDUSUARIO {} insertado correctamente.", estudiante.getUsuario().getId());
            return this.converter.toDTO(estudiante);

        } catch (Exception e) {
            logger.error("Error al insertar estudiante con IDUSUARIO {}: {}", estudianteDTO.getIdUsuario(), e.getMessage(), e);
            throw new RuntimeException("Error al ingresar estudiante con IDUSUARIO: " + estudianteDTO.getIdUsuario(), e);
        }
    }

    @Override
    public EstudianteDTO buscarPorIdUsuario(Integer idUsuario) {

        try {
            if (idUsuario <= 0) {
                logger.warn("El id no puede ser 0 o negativo.");
                return null;
            }

            Estudiante estudiante = this.estudianteRepository.findByIdUsuario(idUsuario);
            EstudianteDTO estudianteDTO = this.converter.toDTO(estudiante);

            logger.info("Estudiante recuperado con IDUSUARIO {}  correctamente.", estudianteDTO.getIdUsuario());
            return estudianteDTO;

        } catch (Exception e) {
            logger.error("Error al buscar estudiante con IDUSUARIO {}: {}", idUsuario, e.getMessage(), e);
            throw new RuntimeException("Error al buscar estudiante con IDUSUARIO: " + idUsuario, e);
        }

    }

}
