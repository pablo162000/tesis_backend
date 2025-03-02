package com.distribuida.login.service.dto;

import com.distribuida.login.repository.modelo.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IUsuarioMapper {

    IUsuarioMapper INSTANCE = Mappers.getMapper(IUsuarioMapper.class);

    public Usuario toEntity(UsuarioDTO dto);

    public UsuarioDTO toDTO(Usuario entity);
}
