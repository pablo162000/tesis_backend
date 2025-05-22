package com.tesis.backend_tesis.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.List;

public class JwUtil {

    private static final String SECRET_KEY_SESION = "PW#dkfjdsEIDK_KDsdf6786FHSDF-$FBJDFBA-9847838-#calveSuperScreta1267@34hjdsd##Fkjfnb@"; // Cambia esto por una clave más segura



    public static DecodedJWT verifyTokenSesion(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(SECRET_KEY_SESION))
                    .build()
                    .verify(token);
        } catch (Exception e) {
            return null; // Si el token es inválido o ha expirado, retornamos null
        }
    }


}