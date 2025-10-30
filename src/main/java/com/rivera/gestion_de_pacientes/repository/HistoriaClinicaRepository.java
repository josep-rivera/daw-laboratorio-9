package com.rivera.gestion_de_pacientes.repository;

import com.rivera.gestion_de_pacientes.model.HistoriaClinica;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoriaClinicaRepository extends MongoRepository<HistoriaClinica, String> {
    List<HistoriaClinica> findByPacienteId(String pacienteId);
}