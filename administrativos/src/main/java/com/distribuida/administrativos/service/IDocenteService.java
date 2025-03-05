package com.distribuida.administrativos.service;

import com.distribuida.administrativos.repository.modelo.Docente;
import com.distribuida.administrativos.repository.modelo.RegistroRequest;
import com.distribuida.administrativos.service.dto.DocenteDTO;

public interface IDocenteService {

    public Boolean guardarDocente (RegistroRequest registroRequest);

    public DocenteDTO buscarPorId (Integer id);

    public Boolean existeEstudiante(String cedula);
}
