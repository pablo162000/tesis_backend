package com.distribuida.alumno.service;

import com.distribuida.alumno.clients.AdministrativoRestClient;
import com.distribuida.alumno.repository.IPropuestaRepository;
import com.distribuida.alumno.repository.modelo.Archivo;
import com.distribuida.alumno.repository.modelo.EstadoValidacion;
import com.distribuida.alumno.repository.modelo.Propuesta;
import com.distribuida.alumno.service.dto.DocenteDTO;
import com.distribuida.alumno.service.dto.PropuestaDTO;
import com.distribuida.alumno.service.dto.utils.Converter;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;


@Service
public class PropuestaServiceImpl implements IPropuestaService {

    @Autowired
    private IPropuestaRepository propuestaRepository;

    @Autowired
    private Converter converter;

    @Autowired
    private AdministrativoRestClient administrativoRestClient;

    @Autowired
    private IArchivoService archivoService;

    @Override
    public Propuesta guardar(Propuesta propuesta) {
        return this.propuestaRepository.crear(propuesta);
    }



    @Override
    public Propuesta buscarPorId(Integer id) {

        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El ID proporcionado no es válido.");
            }

            Propuesta propuesta = this.propuestaRepository.buscarPorId(id);

            if (propuesta == null) {
                throw new NoSuchElementException("No se encontró una propuesta con el ID: " + id);
            }

            return propuesta;
        } catch (Exception e) {
            e.printStackTrace(); // Se recomienda usar un logger en lugar de esto
            return null; // O lanzar una excepción personalizada según el caso
        }
    }

    @Override
    public List<PropuestaDTO> buscarPorIdEstudiante(Integer idEstudiante) {
        List<Propuesta> propuestas = this.propuestaRepository.findByEstudianteId(idEstudiante);

        // Convertir las entidades a DTOs usando el mapper inyectado
        return this.converter.toDTOList(propuestas);  // Usar el mapper inyectado
    }

    @Override
    public Boolean validarPropuesta(Integer idPropuesta, Integer idDocenteDirector,Boolean estadoValidacion){
        // Verificar si el parámetro estadoValidacion es nulo
        if (estadoValidacion == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La respuesta de validación no puede ser nula.");
        }

        // Obtener la propuesta por ID
        Propuesta propuestaExistente = this.propuestaRepository.buscarPorId(idPropuesta);

        if (propuestaExistente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró la propuesta con el ID: " + idPropuesta);
        }

        try {
            DocenteDTO docenteExistente = this.administrativoRestClient.obtenerDocente(idDocenteDirector);

            if (docenteExistente == null) {
                // Si el docente es null (aunque Feign debería lanzarlo como excepción), lanzar una excepción HTTP Not Found
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró un docente director con el ID: " + idDocenteDirector);
            }


        } catch (FeignException.NotFound e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró un docente director con el ID: " + idDocenteDirector);
        }


        // Actualizar el estado de validación
        propuestaExistente.setValidacion(estadoValidacion ? EstadoValidacion.VALIDADO : EstadoValidacion.NO_VALIDADO);
        propuestaExistente.setIdDocenteDirectorCarrera(idDocenteDirector);

        // Guardar los cambios
        return this.propuestaRepository.update(propuestaExistente);
    }


    @Override
    public Boolean asignarRevisor(Integer idPropuesta, Integer idDocente, String tipoRevisor) {
        // Validar entrada
        if (idPropuesta == null || idDocente == null || tipoRevisor == null || tipoRevisor.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los parámetros no pueden ser nulos o vacíos.");
        }

        // Obtener la propuesta por ID
        Propuesta propuestaExistente = this.propuestaRepository.buscarPorId(idPropuesta);
        if (propuestaExistente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró una propuesta con el ID: " + idPropuesta);
        }

        if (propuestaExistente.getValidacion().equals(EstadoValidacion.NO_VALIDADO) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La propuesta con el ID: " + idPropuesta + " no está validada.");
        }else if (propuestaExistente.getValidacion().equals(EstadoValidacion.NO_REVISADO)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La propuesta con el ID: " + idPropuesta + " no está revisada.");

        }

        try {
            DocenteDTO docenteExistente = this.administrativoRestClient.obtenerDocente(idDocente);

            if (docenteExistente == null) {
                // Si el docente es null (aunque Feign debería lanzarlo como excepción), lanzar una excepción HTTP Not Found
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró un docente con el ID: " + idDocente);
            }


        } catch (FeignException.NotFound e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró un docente con el ID: " + idDocente);
        }


        // Validar que el docente no sea asignado como primer y segundo revisor a la vez
        if (("primer".equals(tipoRevisor) && idDocente.equals(propuestaExistente.getIdDocenteSegundoRevisor())) ||
                ("segundo".equals(tipoRevisor) && idDocente.equals(propuestaExistente.getIdDocentePrimerRevisor()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El mismo docente no puede ser asignado como primer y segundo revisor.");
        }

        // Asignar el docente según el tipo de revisor
        boolean cambioRealizado = false;
        if ("primer".equals(tipoRevisor) && !idDocente.equals(propuestaExistente.getIdDocentePrimerRevisor())) {
            propuestaExistente.setIdDocentePrimerRevisor(idDocente);
            cambioRealizado = true;
        } else if ("segundo".equals(tipoRevisor) && !idDocente.equals(propuestaExistente.getIdDocenteSegundoRevisor())) {
            propuestaExistente.setIdDocenteSegundoRevisor(idDocente);
            cambioRealizado = true;
        }

        // Si hubo cambios, actualizar la base de datos
        if (!cambioRealizado) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se realizaron cambios, ya estaba asignado.");
        }

        return propuestaRepository.update(propuestaExistente);
    }

    @Override
    public List<PropuestaDTO> buscarTodaspropuestas() {
        List<Propuesta> propuestas = this.propuestaRepository.finall();

        // Convertir la lista de Usuario a una lista de UsuarioDTO
        return this.converter.toDTOList(propuestas);
    }

    @Override
    public Boolean actualizar(Propuesta propuesta) {
        return this.propuestaRepository.update(propuesta);
    }

    @Override
    public Boolean puedeEnviarPropuestas(Integer idEstudiante) {

        List<Propuesta> propuestasValidas = propuestaRepository.findPropuestasBy(idEstudiante, EstadoValidacion.NO_REVISADO, EstadoValidacion.NO_VALIDADO);

        // Si existen propuestas con estas condiciones, el estudiante no puede enviar una nueva propuesta
        return propuestasValidas.isEmpty();
    }

    @Override
    public String procesarArchivoRevisores(Integer idPropuesta, Integer idAdministrativo, MultipartFile archivo)  throws IOException {
        // Validaciones de parámetros nulos
        if (idPropuesta == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID de la propuesta no puede ser nulo.");
        }
        if (idAdministrativo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del administrativo no puede ser nulo.");
        }
        if (archivo == null || archivo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha seleccionado ningún archivo.");
        }

        // Obtener la propuesta
        Propuesta propuestaExistente = this.buscarPorId(idPropuesta);
        if (propuestaExistente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la propuesta con el ID: " + idPropuesta);
        }

        // Validar el estado de la propuesta
        if (propuestaExistente.getValidacion().equals(EstadoValidacion.NO_VALIDADO)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La propuesta con el ID: " + idPropuesta + " no está validada.");
        }
        if (propuestaExistente.getValidacion().equals(EstadoValidacion.NO_REVISADO)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La propuesta con el ID: " + idPropuesta + " no está revisada.");
        }

        // Validar que el archivo sea PDF
        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se permiten archivos en formato PDF.");
        }

        LocalDateTime fechaActual = LocalDateTime.now();
        // Guardar archivo con el ID del revisor
        Archivo archivoGuardado = this.archivoService.guardar(archivo, idAdministrativo, "administrativo");
        propuestaExistente.setArchivoDesignacionRevisores(archivoGuardado);
        propuestaExistente.setFechaDesignacionRevisores(fechaActual);

        // Actualizar la propuesta
        if (this.actualizar(propuestaExistente)) {
            return "Se asignó el archivo de designación de revisores.";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo asignar el archivo de designación de revisores.");
        }
    }

    @Override
    public String calificarPropuesta(Integer idPropuesta, Double nota, String tipoRevisor, String observaciones, MultipartFile archivo) throws IOException {
        // Validaciones de parámetros nulos
        if (Objects.isNull(idPropuesta)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID de la propuesta no puede ser nulo.");
        }
        if (nota == null || Double.isNaN(nota) || nota < 0 || nota > 20) {
            System.out.println("Nota inválida: " + nota);  // Verifica el valor de la nota
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La nota debe estar entre 0 y 20.");
        }
        if (Objects.isNull(archivo) || archivo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha seleccionado ningún archivo.");
        }

        // Obtener la propuesta
        Propuesta propuestaExistente = propuestaRepository.buscarPorId(idPropuesta);
        if (propuestaExistente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la propuesta.");
        }

        // Obtener el ID del revisor
        Integer idRevisor = obtenerIdRevisor(propuestaExistente, tipoRevisor);
        if (idRevisor == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontró el revisor correspondiente en la propuesta.");
        }

        // Validar el tipo de archivo
        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se permiten archivos en formato PDF.");
        }

        // Guardar el archivo
        Archivo archivoGuardado = archivoService.guardar(archivo, idRevisor, "administrativo");
        if (archivoGuardado == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el archivo.");
        }

        // Actualizar la propuesta
        actualizarCalificacion(propuestaExistente, tipoRevisor, nota, observaciones, archivoGuardado);
        propuestaRepository.update(propuestaExistente);

        return "Calificación registrada correctamente.";
    }

    @Override
    public String aprobarPropuesta(Integer idPropuesta, Integer idDirector, String observaciones, MultipartFile archivo) throws IOException {
        if (Objects.isNull(idPropuesta)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID de la propuesta no puede ser nulo.");
        }

        Propuesta propuesta = propuestaRepository.buscarPorId(idPropuesta);
        if (propuesta == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha encontrado la propuesta.");
        }
        if (archivo == null || archivo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha seleccionado ningún archivo.");
        }

        if (Objects.isNull(propuesta.getNotaPrimerRevisor())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El primer revisor aún no ha calificado.");
        }

        if (Objects.isNull(propuesta.getNotaSegundoRevisor())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El segundo revisor aún no ha calificado.");
        }

        Double notaFinal = (propuesta.getNotaPrimerRevisor() + propuesta.getNotaSegundoRevisor()) / 2.0;

        if (notaFinal <= 11) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El promedio simple de las calificaciones es menor a 11, se obtuvo: " + notaFinal);
        }

        // Guardar el archivo
        Archivo archivoGuardado = archivoService.guardar(archivo, idDirector, "administrativo");

        // Actualizar la propuesta
        propuesta.setEstadoAprobacion(Boolean.TRUE);
        propuesta.setFechaAprobacion(LocalDateTime.now());
        propuesta.setArchivoDesignacionTutor(archivoGuardado);
        propuesta.setObservacion(observaciones);
        this.propuestaRepository.update(propuesta);

        return "Aprobación registrada correctamente.";
    }

    private Integer obtenerIdRevisor(Propuesta propuesta, String tipoRevisor) {
        if ("primer".equalsIgnoreCase(tipoRevisor)) {
            return propuesta.getIdDocentePrimerRevisor();
        } else if ("segundo".equalsIgnoreCase(tipoRevisor)) {
            return propuesta.getIdDocenteSegundoRevisor();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipo de revisor debe ser 'primer' o 'segundo'.");
    }

    private void actualizarCalificacion(Propuesta propuesta, String tipoRevisor, Double nota, String observaciones, Archivo archivoGuardado) {
        LocalDateTime fechaActual = LocalDateTime.now();
        if ("primer".equalsIgnoreCase(tipoRevisor)) {
            propuesta.setNotaPrimerRevisor(nota);
            propuesta.setFechaPrimerRevisor(fechaActual);
            propuesta.setArchivoRubricaPrimerRevisor(archivoGuardado);
            propuesta.setObservacionDocentePrimerRevisor(observaciones);
        } else if ("segundo".equalsIgnoreCase(tipoRevisor)) {
            propuesta.setNotaSegundoRevisor(nota);
            propuesta.setFechaSegundoRevisor(fechaActual);
            propuesta.setArchivoRubricaSegundoRevisor(archivoGuardado);
            propuesta.setObservacionDocenteSegundoRevisor(observaciones);
        }
    }

}
