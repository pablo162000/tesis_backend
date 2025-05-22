package com.distribuida.autenticacion.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.List;

public class JwUtil {

    private static final String SECRET_KEY = "calveSuperScreta1267@34hjdsd##Fkjfnb@"; // Cambia esto por una clave más segura

    private static final String SECRET_KEY_SESION = "PW#dkfjdsEIDK_KDsdf6786FHSDF-$FBJDFBA-9847838-#calveSuperScreta1267@34hjdsd##Fkjfnb@"; // Cambia esto por una clave más segura



    // Método para generar el token
    public static String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 300000)) // Expira en 5min
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public static String generateTokenSesion(String username, List<String> roles) {

        System.out.println("Generando token sesion en jutil " + username );
        System.out.println("Generando token sesion en jutil" + roles );

        return JWT.create()
                .withSubject(username)
                .withClaim("roles", roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + 600000))
                .sign(Algorithm.HMAC256(SECRET_KEY_SESION));
    }


    public static DecodedJWT verifyTokenSesion(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(SECRET_KEY_SESION))
                    .build()
                    .verify(token);
        } catch (Exception e) {
            return null; // Si el token es inválido o ha expirado, retornamos null
        }
    }



    // Método para verificar el token
    public static DecodedJWT verifyToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build()
                    .verify(token);
        } catch (Exception e) {
            return null; // Si el token es inválido o ha expirado, retornamos null
        }
    }

}