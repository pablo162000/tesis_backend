package com.distribuida.administrativos.service;

import com.distribuida.administrativos.repository.IAdministrativoRepository;
import com.distribuida.administrativos.repository.modelo.Administrativo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministrativoServiceImpl implements IAdministrativoService {

    @Autowired
    private IAdministrativoRepository administrativoRepository;

    @Override
    public Administrativo insertar(Administrativo administrativo) {
        return this.administrativoRepository.insertar(administrativo);
    }

    @Override
    public Administrativo buscarPorIdUsuario(Integer iDUsuario) {
        return this.administrativoRepository.findById(iDUsuario);
    }

    @Override
    public Boolean activarCuentaEstudiante(Integer iDUsuarioEstudiante) {
        return null;
    }

}
