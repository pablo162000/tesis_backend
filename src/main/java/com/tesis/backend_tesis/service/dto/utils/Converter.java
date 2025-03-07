package com.tesis.backend_tesis.service.dto.utils;

import com.tesis.backend_tesis.repository.IEstudiantesRepository;
import com.tesis.backend_tesis.repository.IUsuariosRepository;
import com.tesis.backend_tesis.repository.modelo.Archivo;
import com.tesis.backend_tesis.repository.modelo.Estudiantes;
import com.tesis.backend_tesis.repository.modelo.Propuesta;
import com.tesis.backend_tesis.repository.modelo.Usuarios;
import com.tesis.backend_tesis.service.dto.ArchivoDTO;
import com.tesis.backend_tesis.service.dto.EstudianteDTO;
import com.tesis.backend_tesis.service.dto.PropuestaDTO;
import com.tesis.backend_tesis.service.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class Converter {
    @Autowired
    private IUsuariosRepository usuariosRepository;

    @Autowired
    private IEstudiantesRepository estudiantesRepository;

    public ArchivoDTO toDTOSimple(Archivo archivo) {
        if (archivo == null) {
            return null; // Si el archivo es null, retornamos null
        }

        ArchivoDTO archDTO = new ArchivoDTO();
        archDTO.setId(archivo.getId());
        archDTO.setNombre(archivo.getNombre());
        archDTO.setUrl(archivo.getUrl());
        archDTO.setFechaCreacion(archivo.getFechaCreacion());
        archDTO.setIdUsuario(archivo.getUsuario() != null ? archivo.getUsuario().getId() : null);
        return archDTO;
    }

    public Archivo toEntity(ArchivoDTO archDTO) {
        if (archDTO == null) {
            return null; // Si el DTO es null, retornamos null
        }

        Archivo archDTOEntity = new Archivo();
        archDTOEntity.setId(archDTO.getId());
        archDTOEntity.setNombre(archDTO.getNombre());
        archDTOEntity.setUrl(archDTO.getUrl());
        archDTOEntity.setFechaCreacion(archDTO.getFechaCreacion());
        if (archDTO.getIdUsuario() != null) {
            Optional<Usuarios> usuarioOpt = Optional.ofNullable(this.usuariosRepository.buscarPorId(archDTO.getIdUsuario()));
            usuarioOpt.ifPresent(archDTOEntity::setUsuario); // Setea el usuario solo si se encuentra
        }

        return archDTOEntity;
    }

    public EstudianteDTO toDTOSimple(Estudiantes estudiantes) {
        if (estudiantes == null) {
            return null; // Si el estudiante es null, retornamos null
        }

        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setId(estudiantes.getId());
        estudianteDTO.setPrimerNombre(estudiantes.getPrimer_nombre());
        estudianteDTO.setSegundoNombre(estudiantes.getSegundo_nombre());
        estudianteDTO.setPrimerApellido(estudiantes.getPrimer_apellido());
        estudianteDTO.setSegundoApellido(estudiantes.getSegundo_apellido());
        estudianteDTO.setCedula(estudiantes.getCedula());
        estudianteDTO.setActivo(estudiantes.getActivo());
        estudianteDTO.setIdUsuario(estudiantes.getUsuario() != null ? estudiantes.getUsuario().getId() : null);

        return estudianteDTO;
    }

    public Estudiantes toEntity(EstudianteDTO estudianteDTO) {
        if (estudianteDTO == null) {
            return null; // Si el DTO es null, retornamos null
        }

        Estudiantes estudiante = new Estudiantes();
        estudiante.setId(estudianteDTO.getId());
        estudiante.setPrimer_nombre(estudianteDTO.getPrimerNombre());
        estudiante.setSegundo_nombre(estudianteDTO.getSegundoNombre());
        estudiante.setPrimer_apellido(estudianteDTO.getPrimerApellido());
        estudiante.setSegundo_apellido(estudianteDTO.getSegundoApellido());
        estudiante.setCedula(estudianteDTO.getCedula());
        estudiante.setActivo(estudianteDTO.getActivo());

        // Si existe un usuario asociado, se establece su id
        if (estudianteDTO.getIdUsuario() != null) {
            Optional<Usuarios> usuarioOpt = Optional.ofNullable(this.usuariosRepository.buscarPorId(estudianteDTO.getIdUsuario()));
            usuarioOpt.ifPresent(estudiante::setUsuario); // Setea el usuario solo si se encuentra
        }

        return estudiante;
    }
/*
    public UsuarioDTO toDTOSimple(Usuarios usuarios) {
        if (usuarios == null) {
            return null; // Si el usuario es null, retornamos null
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuarios.getId());
        usuarioDTO.setUsername(usuarios.getUsername());
        usuarioDTO.setCorreo(usuarios.getCorreo());
        usuarioDTO.setRol(usuarios.getRol());
        usuarioDTO.setFechaCreacion(usuarios.getFechaCreacion());
        usuarioDTO.setActivo(usuarios.getActivo());
        usuarioDTO.setIdEstudiante(usuarioDTO.getIdEstudiante());

        if (usuarios.getArchivos() != null && !usuarios.getArchivos().isEmpty()) {
            List<ArchivoDTO> archivosDTO = usuarios.getArchivos().stream()
                    .map(this::toDTOSimple) // Usando el método toDTO de Archivo
                    .collect(Collectors.toList());
            usuarioDTO.setArchivos(archivosDTO);
        }

        return usuarioDTO;
    }


 */
    public Usuarios toEntity(UsuarioDTO usuarioDTO) {
        if (usuarioDTO == null) {
            return null; // Si el DTO es null, retornamos null
        }

        Usuarios usuario = new Usuarios();
        usuario.setId(usuarioDTO.getId());
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setRol(usuarioDTO.getRol());
        usuario.setFechaCreacion(usuarioDTO.getFechaCreacion());
        usuario.setActivo(usuarioDTO.getActivo());

        // Asegúrate de tratar cualquier otro campo adicional que pueda ser necesario.
        return usuario;
    }

    public PropuestaDTO toDTOSimple(Propuesta propuesta) {
        if (propuesta == null) {
            return null; // Si la propuesta es null, retornamos null
        }

        PropuestaDTO propuestaDTO = new PropuestaDTO();
        propuestaDTO.setId(propuesta.getId());
        propuestaDTO.setEstudiantePrimero(toDTOSimple(propuesta.getEstudiantePrimero()));
        propuestaDTO.setEstudianteSegundo(toDTOSimple(propuesta.getEstudianteSegundo()));
        propuestaDTO.setEstudianteTercero(toDTOSimple(propuesta.getEstudianteTercero()));
        propuestaDTO.setTema(propuesta.getTema());
        propuestaDTO.setPeriodo(propuesta.getPeriodo());
        propuestaDTO.setArchivo(toDTOSimple(propuesta.getArchivo()));
        propuestaDTO.setObservacion(propuesta.getObservacion());
        propuestaDTO.setValidacion(propuesta.getValidacion());
        propuestaDTO.setIdDocente(propuesta.getIdDocente());
        propuestaDTO.setIdEstuCreacion(propuesta.getIdEstuCreacion());
        return propuestaDTO;
    }

    public List<PropuestaDTO> toDTOList(List<Propuesta> propuestas) {
        if (propuestas == null || propuestas.isEmpty()) {
            return null; // Si la lista de propuestas es null o vacía, retornamos null
        }

        return propuestas.stream()
                .map(this::toDTOSimple) // Convierte cada elemento de la lista utilizando el método toDTO
                .collect(Collectors.toList()); // Recoge los resultados en una lista
    }
}
