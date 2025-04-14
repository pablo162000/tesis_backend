package com.distribuida.autenticacion.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.distribuida.autenticacion.security.JwUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ValidarCorreoServiceImpl implements IValidarCorreoService{

    @Override
    public String generarTokenCorreo(String correo ) {


        if(correo.isEmpty() || correo == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Falta el correo del usuario.");
        }
/*
        if(!correo.toLowerCase().endsWith("@uce.edu.ec")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El dominio de correo no es valido.");

        }

 */


        String token = JwUtil.generateToken(correo.toLowerCase());

        return token;
    }

    @Override
    public String validarTokenCorreo(String token) {

        if(token.isEmpty() || token == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Falta el token para poder validar correo.");
        }

        DecodedJWT decodedJWT = JwUtil.verifyToken(token);

        if (decodedJWT == null){

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "El token expiró, no es válido.");
        }

        String correo = decodedJWT.getSubject();

        return correo;

    }


}
