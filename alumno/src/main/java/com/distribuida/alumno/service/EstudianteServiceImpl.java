package com.distribuida.alumno.service;

import com.distribuida.alumno.repository.IEstudianteRepository;
import com.distribuida.alumno.repository.modelo.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EstudianteServiceImpl implements IEstudianteService {

    @Autowired
    private IEstudianteRepository estudianteRepository;

    @Override
    public Estudiante insertar(Estudiante estudiante) {
        return this.estudianteRepository.insertar(estudiante);
    }


    @Override
    public Estudiante buscarPorId(Integer id) {
        return this.estudianteRepository.findById(id);
    }

    @Override
    public Estudiante buscarPorIdUsuario(Integer iDUsuario) {
        return this.estudianteRepository.findByIdUsuario(iDUsuario);
    }
    @Override
    public Boolean esCorreoValido(String correo) {
        return correo != null && correo.toLowerCase().endsWith("@uce.edu.ec");
    }

    @Override
    public Boolean existeEstudiante(String ceduula) {
        Estudiante estudiante = this.estudianteRepository.findByCedula(ceduula);
        return estudiante != null; // Si el estudiante existe, devuelve true; si es null, devuelve false
    }
}
