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
    public Estudiante buscarPorCedula(String cedula) {
         return this.estudianteRepository.findByCedulaUsuario(cedula);
    }

    @Override
    public Estudiante buscarPorIdUsuario(Integer iDUsuario) {
        return this.estudianteRepository.findByIdUsuario(iDUsuario);
    }
    @Override
    public boolean esCorreoValido(String correo) {
        return correo != null && correo.toLowerCase().endsWith("@uce.edu.ec");
    }
}
