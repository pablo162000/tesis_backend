package com.distribuida.login.service;

import com.distribuida.login.repository.modelo.AuthResponse;
import com.distribuida.login.repository.modelo.LoginRequest;
import com.distribuida.login.repository.modelo.RegistroAdministrativoRequest;
import com.distribuida.login.repository.modelo.RegistroRequest;
import com.distribuida.login.service.dto.AdministrativoDTO;
import com.distribuida.login.service.dto.DocenteDTO;

public interface IAuthService {
    public Boolean registroEstudiante(RegistroRequest registroRequest);
    public AuthResponse loginUsuario(LoginRequest loginRequest);
    public boolean esCorreoValido(String correo);
    public DocenteDTO registroDocente(RegistroRequest registroRequest);
    public Boolean validarCorreo(String correo);
    public Boolean validarCorreoDocente(String correo, String password);
    public AdministrativoDTO registroAdministrativo(RegistroAdministrativoRequest registroAdministrativoRequest);


    }
