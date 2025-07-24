package com.distribuida.autenticacion.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
public class JwUtil {


    @Value("${security.token.key}")
    private String secretKey;

    @Value("${security.token.sesion}")
    private String secretKeySesion;


    private static String SECRET_KEY;
    private static String SECRET_KEY_SESION;


    @PostConstruct
    public void init() {

        SECRET_KEY_SESION = secretKeySesion;

        SECRET_KEY = secretKey;
    }


    // Metodo para generar el token validar correo
    public static String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(36))) // Expira en 5min
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    // Metodo generar token desde el login
    public static String generateTokenSesion(String username, List<String> roles) {


        return JWT.create()
                .withSubject(username)
                .withClaim("roles", roles)
                .withExpiresAt(new Date(System.currentTimeMillis() +  TimeUnit.MINUTES.toMillis(45)))
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