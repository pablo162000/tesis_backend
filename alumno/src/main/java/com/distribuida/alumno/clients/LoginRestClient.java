package com.distribuida.alumno.clients;


import com.distribuida.alumno.service.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "loginClient", url = "http://localhost:8080/API/tesis/usuarios")
public interface LoginRestClient {

    @GetMapping("/correo/{correo}")  // Ruta para consultar un Usuario por su ID
    UsuarioDTO existeUsuarioPorCorreo(@PathVariable("correo") String correo);
}
