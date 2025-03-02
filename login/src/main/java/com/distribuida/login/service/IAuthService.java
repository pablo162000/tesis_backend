package com.distribuida.login.service;

import com.distribuida.login.repository.modelo.AuthResponse;
import com.distribuida.login.repository.modelo.LoginRequest;
import com.distribuida.login.repository.modelo.RegistroRequest;

public interface IAuthService {
    Boolean registroEstudiante(RegistroRequest registroRequest);
    AuthResponse loginUsuario(LoginRequest loginRequest);
    public boolean esCorreoValido(String correo);

}
