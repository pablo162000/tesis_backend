package com.distribuida.administrativos.service;

import com.distribuida.administrativos.clients.LoginRestClient;
import com.distribuida.administrativos.repository.IAdministrativoRepository;
import com.distribuida.administrativos.repository.modelo.Administrativo;
import com.distribuida.administrativos.repository.modelo.Docente;
import com.distribuida.administrativos.repository.modelo.RegistroAdministrativoRequest;
import com.distribuida.administrativos.repository.modelo.RegistroRequest;
import com.distribuida.administrativos.service.dto.AdministrativoDTO;
import com.distribuida.administrativos.service.dto.DocenteDTO;
import com.distribuida.administrativos.service.dto.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AdministrativoServiceImpl implements IAdministrativoService {

    @Autowired
    private IAdministrativoRepository administrativoRepository;

    @Autowired
    private LoginRestClient loginRestClient;

    @Autowired
    private Converter converter;


    @Override
    public Boolean guardarAdministrativo(RegistroAdministrativoRequest registroAdministrativoRequest) {
        AdministrativoDTO registroLogin= this.loginRestClient.registroUsuarioAdministrativo(registroAdministrativoRequest);

        if (registroLogin == null) {
            return false; // Datos inválidos, no se procesa
        }

        try {
            // Convertir DTO a entidad
            Administrativo administrativo = this.converter.toEntity(registroLogin);

            // Guardar el docente en la base de datos
            Administrativo administrativoGuardado = this.administrativoRepository.insertr(administrativo);

            // Verificar si el docente se guardó correctamente
            return administrativoGuardado != null;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false; // Fallo en el proceso
        }
    }

    @Override
    public AdministrativoDTO buscarPorId(Integer iDU) {
        return null;
    }

    @Override
    public AdministrativoDTO buscarPorIdUsuario(Integer idUsuario) {
        try {
            if (idUsuario == null || idUsuario <= 0) {
                throw new IllegalArgumentException("El ID proporcionado no es válido.");
            }

            Administrativo administrativo = this.administrativoRepository.findByIdUsuario(idUsuario);

            if (administrativo == null) {
                throw new NoSuchElementException("No se encontró un administrativo con el ID: " + idUsuario);
            }
            AdministrativoDTO administrativoDTO = this.converter.toDTO(administrativo);

            return administrativoDTO;
        } catch (Exception e) {
            e.printStackTrace(); // Se recomienda usar un logger en lugar de esto
            return null; // O lanzar una excepción personalizada según el caso
        }
    }
}
