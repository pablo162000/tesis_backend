package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.repository.modelo.AuthResponse;
import com.tesis.backend_tesis.repository.modelo.LoginRequest;
import com.tesis.backend_tesis.repository.modelo.RegistroRequest;

public interface IAuthService {



    public Boolean registroNuevoUsuario(RegistroRequest registroRequest);

    public AuthResponse login(LoginRequest loginRequest);

}
