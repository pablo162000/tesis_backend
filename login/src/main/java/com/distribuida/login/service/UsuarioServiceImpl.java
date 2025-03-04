package com.distribuida.login.service;

import com.distribuida.login.repository.IUsuarioRepository;
import com.distribuida.login.repository.modelo.Usuario;
import com.distribuida.login.service.dto.UsuarioDTO;
import com.distribuida.login.service.dto.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;


    @Autowired
    private Converter converter;

    @Override
    public Boolean activarCuenta(Integer id) {
        return this.usuarioRepository.activarUsuario(id);
    }

    @Override
    public UsuarioDTO buscarPorEmail(String email) {

        return this.converter.toDTO(this.usuarioRepository.buscarPorEmail(email));
    }

    @Override
    public List<UsuarioDTO> buscarTodosUsuaiosEstudiante() {
        List<Usuario> usuarios = usuarioRepository.findAllWithRol("estudiante");

        // Convertir la lista de Usuario a una lista de UsuarioDTO
        return this.converter.toUsuarioDTOList(usuarios);
    }

}
