package com.tesis.backend_tesis.service;


import com.tesis.backend_tesis.repository.ISecretariaRepository;
import com.tesis.backend_tesis.repository.modelo.Estudiante;
import com.tesis.backend_tesis.repository.modelo.Secretaria;
import com.tesis.backend_tesis.service.dto.EstudianteDTO;
import com.tesis.backend_tesis.service.dto.SecretariaDTO;
import com.tesis.backend_tesis.service.dto.utils.Converter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecretariaServiceImpl implements ISecretariaService {

    private static final Logger logger = LogManager.getLogger(SecretariaServiceImpl.class);

    @Autowired
    private Converter converter;

    @Autowired
    private ISecretariaRepository secretariaRepository;

    @Override
    public SecretariaDTO insertar(SecretariaDTO secretariaDTO) {
        try {
            if (secretariaDTO == null) {
                logger.warn("Intento de insertar un secretaria NULL.");
                return null;
            }

            // Convertir DTO a Entidad
            Secretaria secretaria = this.converter.toEntity(secretariaDTO);

            // Guardar usuario
            this.secretariaRepository.insert(secretaria);
            logger.info("Secretaria ingresado con IDUSUARIO {} insertado correctamente.", secretaria.getUsuario().getId());
            return this.converter.toDTO(secretaria);

        } catch (Exception e) {
            logger.error("Error al insertar secretaria con IDUSUARIO {}: {}", secretariaDTO.getIdUsuario(), e.getMessage(), e);
            throw new RuntimeException("Error al ingresar secretaria con IDUSUARIO: " + secretariaDTO.getIdUsuario(), e);
        }
    }

    @Override
    public SecretariaDTO buscarPorIdUsuario(Integer idUsuario) {
        try {
            if (idUsuario <= 0) {
                logger.warn("El id no puede ser 0 o negativo.");
                return null;
            }

            Secretaria secretaria = this.secretariaRepository.findByIdUsuario(idUsuario);
            SecretariaDTO secretariaDTO = this.converter.toDTO(secretaria);

            logger.info("Secretaria recuperado con IDUSUARIO {}  correctamente.", secretariaDTO.getIdUsuario());
            return secretariaDTO;

        } catch (Exception e) {
            logger.error("Error al buscar Secretaria con IDUSUARIO {}: {}", idUsuario, e.getMessage(), e);
            throw new RuntimeException("Error al buscar Secretaria con IDUSUARIO: " + idUsuario, e);
        }
    }

}
