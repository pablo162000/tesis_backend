package com.tesis.backend_tesis.repository;


import com.tesis.backend_tesis.repository.modelo.Secretaria;

public interface ISecretariaRepository {

    public Secretaria insert(Secretaria secretaria);

    public Secretaria findById(Integer id);

    public Boolean deleteSecretariaByIdUsuario(Integer idUsuario);

    public Secretaria findByIdUsuario(Integer idUsuario);
}
