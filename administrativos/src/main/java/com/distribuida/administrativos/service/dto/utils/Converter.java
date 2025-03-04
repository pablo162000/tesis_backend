package com.distribuida.administrativos.service.dto.utils;

import com.distribuida.administrativos.repository.IDocenteRepository;
import com.distribuida.administrativos.repository.modelo.Docente;
import com.distribuida.administrativos.service.dto.DocenteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Converter {

    @Autowired
    private IDocenteRepository docenteRepository;

    public Docente toEntity(DocenteDTO dto) {
        if (dto == null) {
            return null;
        }

        return Docente.builder()
                .primerNombre(dto.getPrimerNombre())
                .segundoNombre(dto.getSegundoNombre())
                .primerApellido(dto.getPrimerApellido())
                .segundoApellido(dto.getSegundoApellido())
                .cedula(dto.getCedula())
                .celular(dto.getCelular())
                .idUsuario(dto.getIdUsuario())
                .build();
    }

    public DocenteDTO toDTO(Docente docente) {
        if (docente == null) {
            return null;
        }

        return DocenteDTO.builder()
                .primerNombre(docente.getPrimerNombre())
                .segundoNombre(docente.getSegundoNombre())
                .primerApellido(docente.getPrimerApellido())
                .segundoApellido(docente.getSegundoApellido())
                .cedula(docente.getCedula())
                .celular(docente.getCelular())
                .idUsuario(docente.getIdUsuario())
                .build();
    }

    public List<DocenteDTO> toDTOList(List<Docente> docentes) {
        if (docentes == null || docentes.isEmpty()) {
            return Collections.emptyList();
        }
        return docentes.stream().map(this::toDTO).collect(Collectors.toList());
    }

}
