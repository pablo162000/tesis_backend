package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.EvaluacionRevisor;

import java.util.List;

public interface IEvaluacionRevisoresRepository {

    public EvaluacionRevisor insert(EvaluacionRevisor evaluacionRevisor);

    public List<EvaluacionRevisor> findByIdDocente(Integer idDocente);

    public EvaluacionRevisor update(EvaluacionRevisor evaluacionRevisor);

    public List<EvaluacionRevisor> findByIdRevision(Integer idRevision);



}
