package com.distribuida.administrativos.service;

import com.distribuida.administrativos.repository.modelo.Administrativo;
import com.distribuida.administrativos.repository.modelo.RegistroAdministrativoRequest;
import com.distribuida.administrativos.repository.modelo.RegistroRequest;
import com.distribuida.administrativos.service.dto.AdministrativoDTO;

public interface IAdministrativoService {


    public Boolean guardarAdministrativo(RegistroAdministrativoRequest RegistroAdministrativoRequest);

    public AdministrativoDTO buscarPorId(Integer iDU);

    public AdministrativoDTO buscarPorIdUsuario(Integer idUsuario);


}
