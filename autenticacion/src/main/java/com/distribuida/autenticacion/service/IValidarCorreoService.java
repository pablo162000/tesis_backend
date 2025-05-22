package com.distribuida.autenticacion.service;

public interface IValidarCorreoService {

    public String generarTokenCorreo(String correo );
    public String validarTokenCorreo(String token);

}
