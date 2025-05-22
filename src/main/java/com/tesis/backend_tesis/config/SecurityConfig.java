package com.tesis.backend_tesis.config;

import com.tesis.backend_tesis.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactivar CSRF si usas JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No usar sesiones
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll() // Permitir rutas públicas
                               .anyRequest().authenticated() // Proteger todo lo demás

                )
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class); // Agregar el filtro JWT

        return http.build();
    }


}
