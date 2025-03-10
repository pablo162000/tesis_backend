package com.distribuida.login.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

public class JwUtil {

    private static final String SECRET_KEY = "calveSuperScreta1267@34hjdsd"; // Cambia esto por una clave más segura

    // Método para generar el token
    public static String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000)) // Expira en 1 hora
                .sign(Algorithm.HMAC256(SECRET_KEY));
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