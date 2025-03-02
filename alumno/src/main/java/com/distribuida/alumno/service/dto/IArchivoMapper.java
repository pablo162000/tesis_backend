package com.distribuida.alumno.service.dto;


import com.distribuida.alumno.repository.modelo.Archivo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IArchivoMapper {

    IArchivoMapper INSTANCE = Mappers.getMapper(IArchivoMapper.class);

    public Archivo toEntity(ArchivoDTO dto);

    public ArchivoDTO toDTO(Archivo entity);

}
