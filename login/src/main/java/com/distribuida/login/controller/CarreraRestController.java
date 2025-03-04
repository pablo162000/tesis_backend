package com.distribuida.login.controller;

import com.distribuida.login.service.ICarreraService;
import com.distribuida.login.service.dto.CarreraDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/carreras")
public class CarreraRestController {

    @Autowired
    private ICarreraService carreraService;


    @GetMapping
    public ResponseEntity<?> obtenerTodasCarreras() {
        try {

            // Buscar las carreras
            List<CarreraDTO> carrerasDto = this.carreraService.buscarTodos();

            // Validar si se encontraron propuestas
            if (carrerasDto.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron carreras");
            }

            // Retornar las propuestas encontradas
            return ResponseEntity.ok(carrerasDto);

        } catch (Exception e) {
            // Manejo de errores inesperados
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error al obtener las carreras: " + e.getMessage());
        }
}



}
