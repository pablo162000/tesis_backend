package com.distribuida.alumno.service.dto;

import com.distribuida.alumno.repository.modelo.Estudiante;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IEstudianteMapper {

    IEstudianteMapper INSTANCE = Mappers.getMapper(IEstudianteMapper.class);

    public Estudiante toEntity(EstudianteDTO dto);

    public EstudianteDTO toDTO(Estudiante entity);
}
