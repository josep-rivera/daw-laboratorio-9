package com.rivera.gestion_de_pacientes.repository;

import com.rivera.gestion_de_pacientes.model.AntecedenteMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AntecedenteRepository extends JpaRepository<AntecedenteMedico, Long> {
    List<AntecedenteMedico> findByHistoriaClinicaIdHistoria(Long idHistoria);
}