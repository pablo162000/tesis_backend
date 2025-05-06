package com.tesis.backend_tesis.service;

import com.tesis.backend_tesis.repository.modelo.Propuesta;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface IPropuestaService {


    public String guardar(String tipo,
                             String tema,
                             String categoria,
                             String primerCorreo,
                             String segundoCorreo,
                             String tercerCorreo,
                             Integer idDocenteTutor,
                             MultipartFile archivo) throws IOException;

    public Propuesta buscar(Integer id);

    public Boolean puedeEnviarPropuestas(Integer idEstudiante,String tipo);
    public Boolean puedeEnviarPropuestasTipoCategoria(Integer idEstudiante, String tipo, String categoria);
    public Boolean puedeEnviarPropuestasMultimodal(Integer idEstudiante1,Integer idEstudiante2,Integer idEstudiante3, String tipo);
    public Boolean puedeEnviarPropuestasUnimodal(Integer idEstudiante1,Integer idEstudiante2, String tipo);

    public Boolean diferentesCarreras(Integer idEstudiante1,Integer idEstudiante2, Integer idEstudiante3);
    //public List<PropuestaDTO> buscarPorIdEstudiante(Integer idEstudiante);
    public Boolean validarPropuesta(Integer idPropuesta,
                                    Boolean estadoValidacion,
                                    String obsercvaciones,
                                    Integer idUsuarioSecretaria,
                                    String taskID);
    public Boolean asignarRevisor(Integer idPropuesta,
                                  Integer idDocente1,
                                  Integer idDocente2,
                                  MultipartFile rubrica,
                                  MultipartFile oficio,
                                  String taskID);

    public Boolean calificarPropuestaRevisor(Integer idPropuesta,
                                             Double nota,
                                             String observaciones,
                                             Integer idDocente,
                                             MultipartFile rubrica,
                                             String taskID)throws IOException;

    public Boolean aprobarPropuesta(Integer idPropuesta,
                                    String observaciones,
                                    Integer idTutor,
                                    MultipartFile archivo,
                                    String taskID) throws IOException ;

    public Boolean negacionPropuesta(Integer idPropuesta,
                                    String observaciones,
                                    String taskID);

    public void recordatorioRevisores(Integer idPropuesta,
                                         Integer idDocente);

}
