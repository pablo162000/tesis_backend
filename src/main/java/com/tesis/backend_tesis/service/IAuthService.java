package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.repository.modelo.AuthResponse;
import com.tesis.backend_tesis.repository.modelo.LoginRequest;
import com.tesis.backend_tesis.repository.modelo.RegistroRequest;

public interface IAuthService {

    //Integer registroEstudiante(RegistroRequest registroRequest);
    //AuthResponse loginUsuario(LoginRequest loginRequest);

    public Boolean registroNuevoEstudiante(RegistroRequest registroRequest);

    public Boolean registroNuevoUsuario(RegistroRequest registroRequest);

    public AuthResponse login(LoginRequest loginRequest);
    public AuthResponse seleccionarRol(Integer idUsuario, String rolSeleccionado);
}
