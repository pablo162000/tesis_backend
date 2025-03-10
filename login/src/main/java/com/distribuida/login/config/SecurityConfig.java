package com.distribuida.login.config;


import com.distribuida.login.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactivar CSRF si usas JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No usar sesiones
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/registro", "/auth/validacion-correo/**","/usuarios/**").permitAll() // Permitir rutas públicas
                        .anyRequest().authenticated() // Proteger todo lo demás
                )
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class); // Agregar el filtro JWT

        return http.build();
    }

}
