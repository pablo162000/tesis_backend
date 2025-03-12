package com.distribuida.administrativos.repository;

import com.distribuida.administrativos.repository.modelo.Administrativo;
import com.distribuida.administrativos.repository.modelo.Docente;

public interface IAdministrativoRepository {

    public Administrativo insertr(Administrativo administrativo);
    public Administrativo findByIdUsuario(Integer idUsuario);
    public Administrativo findById (Integer id);

}
