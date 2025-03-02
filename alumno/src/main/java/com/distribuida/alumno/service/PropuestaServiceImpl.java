package com.distribuida.alumno.service;

import com.distribuida.alumno.repository.IPropuestaRepository;
import com.distribuida.alumno.repository.modelo.EstadoValidacion;
import com.distribuida.alumno.repository.modelo.Propuesta;
import com.distribuida.alumno.service.dto.PropuestaDTO;
import com.distribuida.alumno.service.dto.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropuestaServiceImpl implements IPropuestaService {

    @Autowired
    private IPropuestaRepository propuestaRepository;

    @Autowired
    private Converter converter;

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
        List<Propuesta> propuestas = this.propuestaRepository.findByEstudianteId(idEstudiante);

        // Convertir las entidades a DTOs usando el mapper inyectado
        return this.converter.toDTOList(propuestas);  // Usar el mapper inyectado
    }

    @Override
    public Boolean validarPropuesta(Integer idPropuesta, Boolean estadoValidacion) {
        try {
            // Obtener la propuesta por ID
            Propuesta propuestaExistente = propuestaRepository.buscarPorId(idPropuesta);

            if (propuestaExistente != null) {
                // Solo actualiza si el estado de validación no es nulo
                if (estadoValidacion != null) {
                    // Actualiza el estado según el parámetro
                    if (estadoValidacion) {
                        propuestaExistente.setValidacion(EstadoValidacion.VALIDADO);
                    } else {
                        propuestaExistente.setValidacion(EstadoValidacion.NO_VALIDADO);
                    }

                    // Llamar al método update() para guardar los cambios
                    Boolean resultadoUpdate = this.propuestaRepository.update(propuestaExistente); // Llamada al método update()

                    // Verificar si la actualización fue exitosa
                    return resultadoUpdate;
                } else {
                    return false; // El estado de validación es nulo
                }
            } else {
                return false; // No se encontró la propuesta con el ID dado
            }
        } catch (Exception e) {
            e.printStackTrace(); // Manejo de errores
            return false; // En caso de error
        }

    }
}
