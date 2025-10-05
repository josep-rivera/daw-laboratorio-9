package com.rivera.gestion_de_pacientes.repository;

import com.rivera.gestion_de_pacientes.model.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long> {
    List<HistoriaClinica> findByPacienteIdPaciente(Long idPaciente);
}