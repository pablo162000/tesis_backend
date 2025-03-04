package com.distribuida.alumno.clients;

import com.distribuida.alumno.service.dto.DocenteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "administrativoRestClient", url = "http://localhost:5050/API/tesis/administrativo")
public interface AdministrativoRestClient {


    @GetMapping("/docentes/{idDocente}")
    DocenteDTO obtenerDocente(@PathVariable Integer idDocente) ;




}
