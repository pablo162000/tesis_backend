package com.tesis.backend_tesis.repository;

import com.tesis.backend_tesis.repository.modelo.Revision;

import java.util.List;

public interface IRevisionRepository {

    public Revision insert(Revision revision);
    public Revision findById(Integer id);
    public Boolean update(Revision revision);
    public Boolean delete(Integer idRevision);
    public List<Revision> findByIdPropuesta(Integer idPropuesta);


}
