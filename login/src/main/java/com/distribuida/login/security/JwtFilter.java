package com.distribuida.login.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends UsernamePasswordAuthenticationFilter {


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // Lógica para verificar el token JWT
        String token = getTokenFromRequest(request);
        if (token != null) {
            DecodedJWT decodedJWT = JwUtil.verifyToken(token); // Verificar el token
            if (decodedJWT != null) {
                // Aquí puedes agregar lógica para establecer el usuario autenticado en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(null); // Personaliza esto según tu lógica
            }
        }
        chain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}