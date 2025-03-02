package com.distribuida.login.clients;


import com.distribuida.login.service.dto.EstudianteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "estudianteClient", url = "http://localhost:4040/API/tesis/alumno/estudiantes")
public interface EstudianteRestClient {


    @PostMapping("/crear")  // Asegúrate de que esta ruta existe en el servicio backend
    Boolean crearEstudiante(@RequestBody EstudianteDTO estudiante);

    @GetMapping("/{id}")  // Ruta para consultar un Usuario por su ID
    EstudianteDTO obtenerEstudiantePorIdUsuario(@PathVariable("id") Integer id);


}
