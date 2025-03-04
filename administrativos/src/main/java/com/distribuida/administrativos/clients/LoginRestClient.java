package com.distribuida.administrativos.clients;

import com.distribuida.administrativos.repository.modelo.RegistroRequest;
import com.distribuida.administrativos.service.dto.DocenteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "loginClient", url = "http://localhost:8080/API/tesis/auth")
public interface LoginRestClient {

    @PostMapping("/registro/docente")  // Ruta para consultar un Usuario por su ID
    DocenteDTO registroUsuarioDocente(@RequestBody RegistroRequest registroRequest);
}
