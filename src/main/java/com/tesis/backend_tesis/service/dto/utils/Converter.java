package com.tesis.backend_tesis.service.dto.utils;

import com.tesis.backend_tesis.repository.*;
import com.tesis.backend_tesis.repository.modelo.*;
import com.tesis.backend_tesis.service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class Converter {

    @Autowired
    private IUsuarioRepository usuarioRepository;


    @Autowired
    private IFacultadRepository facultadRepository;

    @Autowired
    private ICarreraRepository carreraRepository;






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
            Optional<Usuario> usuarioOpt = Optional.ofNullable(this.usuarioRepository.findById(archDTO.getIdUsuario()));
            usuarioOpt.ifPresent(archDTOEntity::setUsuario); // Setea el usuario solo si se encuentra
        }

        return archDTOEntity;
    }

    public Usuario toEntity(UsuarioDTO usuarioDTO) {
        if (usuarioDTO == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setId(usuarioDTO.getId());
        usuario.setPrimerNombre(usuarioDTO.getPrimerNombre());
        usuario.setSegundoNombre(usuarioDTO.getSegundoNombre());
        usuario.setPrimerApellido(usuarioDTO.getPrimerApellido());
        usuario.setSegundoApellido(usuarioDTO.getSegundoApellido());
        usuario.setCedula(usuarioDTO.getCedula());
        usuario.setCelular(usuarioDTO.getCelular());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setActivo(usuarioDTO.getActivo());
        usuario.setCorreoValido(usuarioDTO.getCorreoValido());
        usuario.setFechaCreacion(usuarioDTO.getFechaCreacion());

        return usuario;
    }


    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setPrimerNombre(usuario.getPrimerNombre());
        usuarioDTO.setSegundoNombre(usuario.getSegundoNombre());
        usuarioDTO.setPrimerApellido(usuario.getPrimerApellido());
        usuarioDTO.setSegundoApellido(usuario.getSegundoApellido());
        usuarioDTO.setCedula(usuario.getCedula());
        usuarioDTO.setCorreo(usuario.getCorreo());
        usuarioDTO.setCelular(usuarioDTO.getCelular());
        usuarioDTO.setActivo(usuario.getActivo());
        usuarioDTO.setCorreoValido(usuario.getCorreoValido());
        usuarioDTO.setFechaCreacion(usuario.getFechaCreacion());

        return usuarioDTO;
    }


    public Estudiante toEntity(EstudianteDTO estudianteDTO) {
        if (estudianteDTO == null) {
            return null;
        }

        Estudiante estudiante = new Estudiante();
        estudiante.setId(estudianteDTO.getId());
        estudiante.setUsuario(this.usuarioRepository.findById(estudianteDTO.getIdUsuario()));
        estudiante.setCarrera(this.carreraRepository.findById(estudianteDTO.getIdCarrera()));


        return estudiante;
    }


    public EstudianteDTO toDTO(Estudiante estudiante) {
        if (estudiante == null) {
            return null;
        }

        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setId(estudiante.getId());
        estudianteDTO.setIdUsuario(estudiante.getUsuario().getId());
        estudianteDTO.setIdCarrera(estudiante.getCarrera().getId());


        return estudianteDTO;
    }


    public Docente toEntity(DocenteDTO docenteDTO) {
        if (docenteDTO == null) {
            return null;
        }

        Docente docente = new Docente();
        docente.setId(docenteDTO.getId());
        docente.setUsuario(this.usuarioRepository.findById(docenteDTO.getIdUsuario()));
        docente.setFacultad(this.facultadRepository.findById(docenteDTO.getIdFacultad()));


        return docente;
    }


    public DocenteDTO toDTO(Docente docente) {
        if (docente == null) {
            return null;
        }

        DocenteDTO docenteDTO = new DocenteDTO();
        docenteDTO.setId(docente.getId());
        docenteDTO.setIdUsuario(docente.getUsuario().getId());
        docenteDTO.setIdFacultad(docente.getFacultad().getId());


        return docenteDTO;
    }


    public Carrera toEntity(CarreraDTO carreradaDTO) {
        if (carreradaDTO == null) {
            return null;
        }

        Carrera carrera = new Carrera();
        carrera.setId(carreradaDTO.getId());
        carrera.setNombre(carreradaDTO.getNombre());
        carrera.setFacultad(this.facultadRepository.findById(carreradaDTO.getIdFacultad()));


        return carrera;
    }


    public CarreraDTO toDTO(Carrera carrera) {
        if (carrera == null) {
            return null;
        }

        CarreraDTO carreraDTO = new CarreraDTO();
        carreraDTO.setId(carrera.getId());
        carreraDTO.setNombre(carrera.getNombre());
        carreraDTO.setIdFacultad(carrera.getFacultad().getId());

        return carreraDTO;
    }


    public Facultad toEntity(FacultadDTO facultadDTO) {
        if (facultadDTO == null) {
            return null;
        }

        Facultad facultad = new Facultad();
        facultad.setId(facultadDTO.getId());
        facultad.setCorreo(facultadDTO.getCorreo());
        facultad.setNombre(facultadDTO.getNombre());

        // Obtener las carreras asociadas a la facultad usando su ID
        List<Carrera> carreras = this.carreraRepository.findByFacultad(facultadDTO.getId());
        facultad.setCarreras(carreras);

        return facultad;
    }

    public FacultadDTO toDTO(Facultad facultad) {
        if (facultad == null) {
            return null;
        }

        FacultadDTO facultadDTO = new FacultadDTO();
        facultadDTO.setId(facultad.getId());
        facultadDTO.setCorreo(facultad.getCorreo());
        facultadDTO.setNombre(facultad.getNombre());

        // Convertir lista de Carrera a lista de IDs
        List<Integer> idCarreras = facultad.getCarreras()
                .stream()
                .map(Carrera::getId)
                .collect(Collectors.toList());
        facultadDTO.setIdCarreras(idCarreras);

        return facultadDTO;
    }

    public Secretaria toEntity(SecretariaDTO secretariaDTO) {
        if (secretariaDTO == null) {
            return null;
        }

        Secretaria secretaria = new Secretaria();
        secretaria.setId(secretariaDTO.getId());
        secretaria.setUsuario(this.usuarioRepository.findById(secretariaDTO.getIdUsuario()));
        secretaria.setCarrera(this.carreraRepository.findById(secretariaDTO.getIdCarrera()));


        return secretaria;
    }


    public SecretariaDTO toDTO(Secretaria secretaria) {
        if (secretaria == null) {
            return null;
        }

        SecretariaDTO secretariaDTO = new SecretariaDTO();
        secretariaDTO.setId(secretaria.getId());
        secretariaDTO.setIdUsuario(secretaria.getUsuario().getId());
        secretariaDTO.setIdCarrera(secretaria.getCarrera().getId());


        return secretariaDTO;
    }

}
