package com.rivera.gestion_de_pacientes.repository;

import com.rivera.gestion_de_pacientes.model.AntecedenteMedico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AntecedenteRepository extends MongoRepository<AntecedenteMedico, String> {
    List<AntecedenteMedico> findByHistoriaId(String historiaId);
}