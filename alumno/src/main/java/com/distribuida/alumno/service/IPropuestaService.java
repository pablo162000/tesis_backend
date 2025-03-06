package com.distribuida.alumno.service;

import com.distribuida.alumno.repository.modelo.Propuesta;
import com.distribuida.alumno.service.dto.PropuestaDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IPropuestaService {

    public Propuesta guardar(Propuesta propuesta);

    public Propuesta buscarPorId(Integer id);

    public List<PropuestaDTO> buscarPorIdEstudiante(Integer idEstudiante);

    public Boolean validarPropuesta(Integer idPropuesta, Integer idDocenteDirector, Boolean estadoValidacion);

    public Boolean asignarRevisor(Integer idPropuesta, Integer idDocente, String tipooRevisor);

    public List<PropuestaDTO> buscarTodaspropuestas();

    public Boolean actualizar(Propuesta propuesta);

    public Boolean puedeEnviarPropuestas(Integer idEstudiante);

    public String procesarArchivoRevisores(Integer idPropuesta, Integer idAdministrativo, MultipartFile archivo) throws IOException;

    public String calificarPropuesta(Integer idPropuesta, Double nota, String tipoRevisor, String observaciones, MultipartFile archivo) throws IOException;

    public String aprobarPropuesta(Integer idPropuesta, Integer idDirector, String observaciones,MultipartFile archivo) throws IOException ;

    }

