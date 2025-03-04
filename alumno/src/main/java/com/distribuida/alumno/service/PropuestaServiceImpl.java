package com.distribuida.alumno.service;

import com.distribuida.alumno.repository.IPropuestaRepository;
import com.distribuida.alumno.repository.modelo.EstadoValidacion;
import com.distribuida.alumno.repository.modelo.Propuesta;
import com.distribuida.alumno.service.dto.PropuestaDTO;
import com.distribuida.alumno.service.dto.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
    public Propuesta buscarPorId(Integer id) {

        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El ID proporcionado no es válido.");
            }

            Propuesta propuesta = this.propuestaRepository.buscarPorId(id);

            if (propuesta == null) {
                throw new NoSuchElementException("No se encontró una propuesta con el ID: " + id);
            }

            return propuesta;
        } catch (Exception e) {
            e.printStackTrace(); // Se recomienda usar un logger en lugar de esto
            return null; // O lanzar una excepción personalizada según el caso
        }
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

    @Override
    public Boolean asignarRevisor(Integer idPropuesta, Integer idDocente, String tipoRevisor) {
        try {
            // Validar entrada
            if (idPropuesta == null || idDocente == null || tipoRevisor == null || tipoRevisor.trim().isEmpty()) {
                throw new IllegalArgumentException("Los parámetros no pueden ser nulos o vacíos.");
            }

            // Obtener la propuesta por ID
            Propuesta propuestaExistente = propuestaRepository.buscarPorId(idPropuesta);

            if (propuestaExistente == null) {
                throw new NoSuchElementException("No se encontró una propuesta con el ID: " + idPropuesta);
            }

            // Validar que el docente no sea asignado como primer y segundo revisor a la vez
            if (("primer".equals(tipoRevisor) && idDocente.equals(propuestaExistente.getIdDocenteSegundoRevisor())) ||
                    ("segundo".equals(tipoRevisor) && idDocente.equals(propuestaExistente.getIdDocentePrimerRevisor()))) {
                throw new IllegalStateException("El mismo docente no puede ser asignado como primer y segundo revisor.");
            }

            // Asignar el docente según el tipo de revisor
            boolean cambioRealizado = false;
            if ("primer".equals(tipoRevisor) && !idDocente.equals(propuestaExistente.getIdDocentePrimerRevisor())) {
                propuestaExistente.setIdDocentePrimerRevisor(idDocente);
                cambioRealizado = true;
            } else if ("segundo".equals(tipoRevisor) && !idDocente.equals(propuestaExistente.getIdDocenteSegundoRevisor())) {
                propuestaExistente.setIdDocenteSegundoRevisor(idDocente);
                cambioRealizado = true;
            }

            // Si hubo cambios, actualizar la base de datos
            return cambioRealizado && propuestaRepository.update(propuestaExistente);

        } catch (Exception e) {
            e.printStackTrace(); // Se recomienda usar un logger en producción
            return false;
        }
    }


    @Override
    public List<PropuestaDTO> buscarTodaspropuestas() {
        List<Propuesta> propuestas = this.propuestaRepository.finall();

        // Convertir la lista de Usuario a una lista de UsuarioDTO
        return this.converter.toDTOList(propuestas);
    }

    @Override
    public Boolean actualizar(Propuesta propuesta) {
        return this.propuestaRepository.update(propuesta);
    }
}
