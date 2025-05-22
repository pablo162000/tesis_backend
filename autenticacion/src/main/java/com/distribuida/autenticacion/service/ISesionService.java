package com.distribuida.autenticacion.service;

import java.util.List;

public interface ISesionService {

    public String generarToken (String username, List<String> roles);
}
