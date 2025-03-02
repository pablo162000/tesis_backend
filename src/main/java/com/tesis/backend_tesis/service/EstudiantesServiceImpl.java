package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.repository.IEstudiantesRepository;
import com.tesis.backend_tesis.repository.modelo.Estudiantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstudiantesServiceImpl implements IEstudiantesService {

    @Autowired
    IEstudiantesRepository estudiantesRepository;

    @Override
    public Estudiantes insertar(Estudiantes estudiantes) {
        return this.estudiantesRepository.insertar(estudiantes);
    }

    @Override
    public Estudiantes existeConEmail(String correo) {
        return this.estudiantesRepository.existeEstudiante(correo);
    }

    @Override
    public Estudiantes buscarPorId(Integer id) {
        return this.estudiantesRepository.findById(id);
    }
}
