package com.distribuida.login.service.dto.utils;

import com.distribuida.login.repository.ICarreraRepository;
import com.distribuida.login.repository.modelo.Carrera;
import com.distribuida.login.repository.modelo.Usuario;
import com.distribuida.login.service.dto.CarreraDTO;
import com.distribuida.login.service.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Converter {

    @Autowired
    private ICarreraRepository carreraRepository;


    public Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }

        return Usuario.builder()
                .id(dto.getId())
                .correo(dto.getCorreo())
                .rol(dto.getRol())
                .fechaCreacion(dto.getFechaCreacion())
                .activo(dto.getActivo())
                .carrera(this.carreraRepository.findById(dto.getIDCarrera()))
                .build();
    }

    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return UsuarioDTO.builder()
                .id(usuario.getId())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol())
                .fechaCreacion(usuario.getFechaCreacion())
                .activo(usuario.getActivo())
                .iDCarrera(usuario.getCarrera().getId())
                .build();
    }

    public List<UsuarioDTO> toUsuarioDTOList(List<Usuario> usuarioList) {
        if (usuarioList == null) {
            return null;
        }

        return usuarioList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public CarreraDTO toDTO(Carrera carrera) {
        if (carrera == null) {
            return null;
        }

        return CarreraDTO.builder()
                .id(carrera.getId())
                .nombre(carrera.getNombre())
                .facultad(carrera.getFacultad())
                .idUsuarios(carrera.getUsuarios() != null
                        ? carrera.getUsuarios().stream()
                        .map(Usuario::getId) // Extrae solo los IDs de los usuarios
                        .collect(Collectors.toList())
                        : new ArrayList<>()) // Manejo de nulos con una lista vacía
                .build();
    }


    public List<CarreraDTO> toCarreraDTOList(List<Carrera> carreraList) {
        if (carreraList == null) {
            return null;
        }

        return carreraList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
