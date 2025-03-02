package com.distribuida.administrativos.service;

import com.distribuida.administrativos.repository.modelo.Administrativo;

public interface IAdministrativoService {


    public Administrativo insertar(Administrativo administrativo);

    public Administrativo buscarPorIdUsuario(Integer iDUsuario);

    public Boolean activarCuentaEstudiante(Integer iDUsuarioEstudiante);

}
