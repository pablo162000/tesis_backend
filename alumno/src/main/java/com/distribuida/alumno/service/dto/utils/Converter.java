package com.distribuida.alumno.service.dto.utils;


import com.distribuida.alumno.repository.IArchivoRepository;
import com.distribuida.alumno.repository.IEstudianteRepository;
import com.distribuida.alumno.repository.modelo.Estudiante;
import com.distribuida.alumno.repository.modelo.Propuesta;
import com.distribuida.alumno.service.dto.EstudianteDTO;
import com.distribuida.alumno.service.dto.PropuestaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Converter {

/*
    @Autowired
    private IEstudianteRepository estudiantesRepository;


    public Estudiante toEntity(EstudianteDTO estudianteDTO) {


        Estudiante estudiante = new Estudiante();
        estudiante.setId(estudianteDTO.getId());
        estudiante.setPrimerApellido(estudianteDTO.getPrimerNombre());
        estudiante.setSegundoNombre(estudianteDTO.getSegundoNombre());
        estudiante.setPrimerApellido(estudianteDTO.getPrimerApellido());
        estudiante.setSegundoApellido(estudianteDTO.getSegundoApellido());
        estudiante.setCedula(estudianteDTO.getCedula());
        estudiante.setCelular(estudianteDTO.getCelular());
        estudiante.setActivo(estudianteDTO.getActivo());
        estudiante.setIdUsuario(estudianteDTO.getIdUsuario());

        return estudiante;

    }

    public EstudianteDTO toDTO(Estudiante estudiante) {


        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setId(estudiante.getId());
        estudianteDTO.setPrimerNombre(estudiante.getPrimerNombre()); // Corregido
        estudianteDTO.setSegundoNombre(estudiante.getSegundoNombre());
        estudianteDTO.setPrimerApellido(estudiante.getPrimerApellido());
        estudianteDTO.setSegundoApellido(estudiante.getSegundoApellido());
        estudianteDTO.setCedula(estudiante.getCedula());
        estudianteDTO.setCelular(estudiante.getCelular());
        estudianteDTO.setActivo(estudiante.getActivo());
        estudianteDTO.setIdUsuario(estudiante.getIdUsuario());
        return estudianteDTO;

    }


 */

    @Autowired
    private IEstudianteRepository estudianteRepository;

    @Autowired
    private IArchivoRepository archivoRepository;

    public Propuesta toEntity(PropuestaDTO dto) {
        if (dto == null) {
            return null;
        }

        return Propuesta.builder()
                .id(dto.getId())
                .primerEstudiante(this.estudianteRepository.findByIdUsuario(dto.getEstudiantePrimero()))
                .segundoEstudiante(this.estudianteRepository.findByIdUsuario(dto.getEstudianteSegundo()))
                .tercerEstudiante(this.estudianteRepository.findByIdUsuario(dto.getEstudianteTercero()))
                .archivoPropuesta(this.archivoRepository.buscarPorId(dto.getArchivoPropuesta()))
                .tema(dto.getTema())
                .observacion(dto.getObservacion())
                .validacion(dto.getValidacion())
                .periodo(dto.getPeriodo())
                .idDocenteTutor(dto.getIdDocenteTutor())
                .archivoDesignacionRevisores(this.archivoRepository.buscarPorId(dto.getIdArchivoDesignacionRevisores()))
                .fechaDesignacionRevisores(dto.getFechaDesignacionRevisores())
                .idDocentePrimerRevisor(dto.getIdDocentePrimerRevisor())
                .notaPrimerRevisor(dto.getNotaPrimerRevisor())
                .fechaPrimerRevisor(dto.getFechaPrimerRevisor())
                .archivoRubricaPrimerRevisor(this.archivoRepository.buscarPorId(dto.getArchivoRubricaPrimerRevisor()))
                .observacionDocentePrimerRevisor(dto.getObservacionDocentePrimerRevisor())
                .idDocenteSegundoRevisor(dto.getIdDocenteSegundoRevisor())
                .notaSegundoRevisor(dto.getNotaSegundoRevisor())
                .fechaSegundoRevisor(dto.getFechaSegundoRevisor())
                .archivoRubricaSegundoRevisor(this.archivoRepository.buscarPorId(dto.getArchivoRubricaSegundoRevisor()))
                .observacionDocentePrimerRevisor(dto.getObservacionDocenteSegundoRevisor())
                .estadoAprobacion(dto.getEstadoAprobacion())
                .idEstuCreacion(dto.getIdEstuCreacion())
                .fechaEnvio(dto.getFechaEnvio())
                .archivoDesignacionTutor(this.archivoRepository.buscarPorId(dto.getIdArchivoDesignacionTutor()))
                .build();
    }

    public PropuestaDTO toDTO(Propuesta propuesta) {
        if (propuesta == null) {
            return null;
        }

        return PropuestaDTO.builder()
                .id(propuesta.getId())
                .estudiantePrimero(propuesta.getPrimerEstudiante() != null ? propuesta.getPrimerEstudiante().getId() : null)
                .estudianteSegundo(propuesta.getSegundoEstudiante() != null ? propuesta.getSegundoEstudiante().getId() : null)
                .estudianteTercero(propuesta.getTercerEstudiante() != null ? propuesta.getTercerEstudiante().getId() : null)
                .archivoPropuesta(propuesta.getArchivoPropuesta() != null ? propuesta.getArchivoPropuesta().getId() : null)
                .tema(propuesta.getTema())
                .observacion(propuesta.getObservacion())
                .validacion(propuesta.getValidacion())
                .periodo(propuesta.getPeriodo())
                .idArchivoDesignacionRevisores(propuesta.getArchivoDesignacionRevisores() != null ? propuesta.getArchivoDesignacionRevisores().getId() : null)
                .fechaDesignacionRevisores(propuesta.getFechaDesignacionRevisores() != null ? propuesta.getFechaDesignacionRevisores() : null)
                .idDocenteTutor(propuesta.getIdDocenteTutor() != null ? propuesta.getIdDocenteTutor() : null)
                .idDocentePrimerRevisor(propuesta.getIdDocentePrimerRevisor() != null ? propuesta.getIdDocentePrimerRevisor() : null)
                .notaPrimerRevisor(propuesta.getNotaPrimerRevisor())
                .fechaPrimerRevisor(propuesta.getFechaPrimerRevisor())
                .archivoRubricaPrimerRevisor(propuesta.getArchivoRubricaPrimerRevisor() != null ? propuesta.getArchivoRubricaPrimerRevisor().getId() : null)
                .observacionDocentePrimerRevisor(propuesta.getObservacionDocentePrimerRevisor()!=null ? propuesta.getObservacionDocentePrimerRevisor() : null)
                .idDocenteSegundoRevisor(propuesta.getIdDocenteSegundoRevisor() != null ? propuesta.getIdDocenteSegundoRevisor() : null)
                .notaSegundoRevisor(propuesta.getNotaSegundoRevisor())
                .fechaSegundoRevisor(propuesta.getFechaSegundoRevisor())
                .archivoRubricaSegundoRevisor(propuesta.getArchivoRubricaSegundoRevisor() != null ? propuesta.getArchivoRubricaSegundoRevisor().getId() : null)
                .observacionDocenteSegundoRevisor(propuesta.getObservacionDocenteSegundoRevisor()!=null ? propuesta.getObservacionDocenteSegundoRevisor() : null)
                .estadoAprobacion(propuesta.getEstadoAprobacion())
                .idEstuCreacion(propuesta.getIdEstuCreacion() != null ? propuesta.getIdEstuCreacion() : null)
                .fechaEnvio(propuesta.getFechaEnvio())
                .idArchivoDesignacionTutor(propuesta.getArchivoDesignacionTutor() != null ? propuesta.getArchivoDesignacionTutor().getId() : null)
                .build();
    }

    public List<PropuestaDTO> toDTOList(List<Propuesta> propuestas) {
        if (propuestas == null || propuestas.isEmpty()) {
            return Collections.emptyList();
        }

        return propuestas.stream().map(this::toDTO).collect(Collectors.toList());
    }


}
