package com.distribuida.administrativos.service;

import com.distribuida.administrativos.repository.modelo.RegistroRequest;
import com.distribuida.administrativos.repository.modelo.VistaDocente;
import com.distribuida.administrativos.service.dto.DocenteDTO;

import java.util.List;

public interface IDocenteService {

    public Boolean guardarDocente (RegistroRequest registroRequest);

    public DocenteDTO buscarPorId(Integer id);

    public Boolean existeDocente(String cedula);


    public VistaDocente buscarViewDocentePorId(Integer id);
    public List<VistaDocente> buscarTodosViewDocente();
    public List<VistaDocente> buscarViewDocentePorEstado(Boolean activo);



}
