package com.rivera.gestion_de_pacientes.repository;

import com.rivera.gestion_de_pacientes.model.Paciente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface PacienteRepository extends MongoRepository<Paciente, String> {
    Optional<Paciente> findByDni(String dni);
    List<Paciente> findByEstado(String estado);
}