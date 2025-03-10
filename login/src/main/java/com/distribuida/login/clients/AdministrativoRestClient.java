package com.distribuida.login.clients;


import com.distribuida.login.service.dto.DocenteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "administrativoRestClient", url = "http://localhost:5051/API/tesis/administrativo")
public interface AdministrativoRestClient {


    @GetMapping("/docentes/{idDocente}")
    DocenteDTO obtenerDocente(@PathVariable Integer idDocente) ;

    @GetMapping("/docentes/existe")
    Boolean existencia(@RequestParam String cedula) ;



}
