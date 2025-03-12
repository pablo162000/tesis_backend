package com.distribuida.administrativos.controller;

import com.distribuida.administrativos.repository.modelo.RegistroAdministrativoRequest;
import com.distribuida.administrativos.repository.modelo.RegistroRequest;
import com.distribuida.administrativos.repository.modelo.VistaDocente;
import com.distribuida.administrativos.repository.modelo.VistaEstudiante;
import com.distribuida.administrativos.service.IAdministrativoService;
import com.distribuida.administrativos.service.IDocenteService;
import com.distribuida.administrativos.service.IEstudianteService;
import com.distribuida.administrativos.service.dto.AdministrativoDTO;
import com.distribuida.administrativos.service.dto.DocenteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("admin")
public class AdministrativoRestController {

    @Autowired
    private IAdministrativoService administrativoService;


    @PostMapping("/secredireccion")
    public ResponseEntity<Boolean> registroUsuarioDocente(@RequestBody RegistroAdministrativoRequest registroAdministrativoRequest) {
        Boolean resultado = this.administrativoService.guardarAdministrativo(registroAdministrativoRequest);

        if (Boolean.TRUE.equals(resultado)) {
            return ResponseEntity.ok(Boolean.TRUE);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Boolean.FALSE);
        }
    }

    @GetMapping("/secredireccion/{idUsuario}")
    public ResponseEntity<AdministrativoDTO> registroUsuarioDocente(@PathVariable Integer idUsuario) {
        AdministrativoDTO administrativoDTO = this.administrativoService.buscarPorIdUsuario(idUsuario);

        if (administrativoDTO == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(administrativoDTO);
    }



}
