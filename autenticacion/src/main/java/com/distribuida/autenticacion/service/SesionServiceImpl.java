package com.distribuida.autenticacion.service;

import com.distribuida.autenticacion.security.JwUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SesionServiceImpl implements ISesionService{

    @Override
    public String generarToken(String username, List<String> roles) {

        if(username == null || username.isEmpty() || roles == null || roles.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Faltan valores.");
        }

        String token = JwUtil.generateTokenSesion(username, roles);
        return token;
    }
}
