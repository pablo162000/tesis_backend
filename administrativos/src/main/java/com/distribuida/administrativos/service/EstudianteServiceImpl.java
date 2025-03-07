package com.distribuida.administrativos.service;

import com.distribuida.administrativos.repository.IVistaEstudianteRepository;
import com.distribuida.administrativos.repository.modelo.VistaEstudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class EstudianteServiceImpl implements IEstudianteService{


    @Autowired
    private IVistaEstudianteRepository vistaEstudianteRepository;


    @Override
    public VistaEstudiante buscarViewDocentePorId(Integer id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID debe ser un número positivo");
        }

        VistaEstudiante estudiante = this.vistaEstudianteRepository.findById(id);
        if (estudiante == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el estudiante con el ID: " + id);
        }

        return estudiante;
    }

    @Override
    public List<VistaEstudiante> buscarTodosViewDocente() {
        List<VistaEstudiante> estudiantes = this.vistaEstudianteRepository.findAll();
        if (estudiantes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay estudiantes disponibles");
        }
        return estudiantes;
    }

    @Override
    public List<VistaEstudiante> buscarViewDocentePorEstado(Boolean activo) {
        if (activo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado no puede ser nulo");
        }

        List<VistaEstudiante> estudiantes = this.vistaEstudianteRepository.findByEstado(activo);

        if (estudiantes == null || estudiantes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron estudiantes con el estado: " + activo);
        }

        return estudiantes;
    }
}