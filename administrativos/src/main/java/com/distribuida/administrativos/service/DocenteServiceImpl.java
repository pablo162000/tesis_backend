package com.distribuida.administrativos.service;

import com.distribuida.administrativos.clients.LoginRestClient;
import com.distribuida.administrativos.repository.IDocenteRepository;
import com.distribuida.administrativos.repository.IVistaDocenteRepository;
import com.distribuida.administrativos.repository.modelo.Docente;
import com.distribuida.administrativos.repository.modelo.RegistroRequest;
import com.distribuida.administrativos.repository.modelo.VistaDocente;
import com.distribuida.administrativos.service.dto.DocenteDTO;
import com.distribuida.administrativos.service.dto.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class DocenteServiceImpl implements IDocenteService{

    @Autowired
    private IDocenteRepository docenteRepository;


    @Autowired
    private Converter converter;

    @Autowired
    private LoginRestClient loginRestClient;

    @Autowired
    private IVistaDocenteRepository vistaDocenteRepository;

    @Override
    public Boolean guardarDocente(RegistroRequest registroRequest) {

        DocenteDTO registroLogin= this.loginRestClient.registroUsuarioDocente(registroRequest);

        if (registroLogin == null) {
            return false; // Datos inválidos, no se procesa
        }



        try {
            // Convertir DTO a entidad
            Docente docente = this.converter.toEntity(registroLogin);

            // Guardar el docente en la base de datos
            Docente docenteGuardado = this.docenteRepository.insert(docente);

            // Verificar si el docente se guardó correctamente
            return docenteGuardado != null;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false; // Fallo en el proceso
        }
    }

    @Override
    public DocenteDTO buscarPorId(Integer id) {
        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El ID proporcionado no es válido.");
            }

            Docente docente = this.docenteRepository.findById(id);

            if (docente == null) {
                throw new NoSuchElementException("No se encontró una propuesta con el ID: " + id);
            }
            DocenteDTO docenteDTO = this.converter.toDTO(docente);

            return docenteDTO;
        } catch (Exception e) {
            e.printStackTrace(); // Se recomienda usar un logger en lugar de esto
            return null; // O lanzar una excepción personalizada según el caso
        }
    }

    @Override
    public Boolean existeDocente(String ceduula) {
        Docente docente = this.docenteRepository.findByCedula(ceduula);

        if (docente == null) {

            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    @Override
    public VistaDocente buscarViewDocentePorId(Integer id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID debe ser un número positivo");
        }
        return this.vistaDocenteRepository.findById(id);
    }

    @Override
    public List<VistaDocente> buscarTodosViewDocente() {
        return this.vistaDocenteRepository.findAll();
    }

    @Override
    public List<VistaDocente> buscarViewDocentePorEstado(Boolean activo) {
        if (activo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado no puede ser nulo");
        }
        List<VistaDocente> docentes = this.vistaDocenteRepository.findByEstado(activo);
        if (docentes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron docentes con el estado: " + activo);
        }
        return docentes;
    }

}
