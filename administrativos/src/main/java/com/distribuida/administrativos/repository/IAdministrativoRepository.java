package com.distribuida.administrativos.repository;

import com.distribuida.administrativos.repository.modelo.Administrativo;

public interface IAdministrativoRepository {

    public Administrativo insertar(Administrativo administrativo);
    public Administrativo findByIdUsuario (Integer idUsuario);
    public Administrativo existeEstudiante (String correo);
    public Administrativo findById (Integer id);

}
