package com.rivera.gestion_de_pacientes.service;

import com.rivera.gestion_de_pacientes.model.Paciente;
import com.rivera.gestion_de_pacientes.model.HistoriaClinica;
import com.rivera.gestion_de_pacientes.model.AntecedenteMedico;
import com.rivera.gestion_de_pacientes.repository.PacienteRepository;
import com.rivera.gestion_de_pacientes.repository.HistoriaClinicaRepository;
import com.rivera.gestion_de_pacientes.repository.AntecedenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private HistoriaClinicaRepository historiaRepository;

    @Autowired
    private AntecedenteRepository antecedenteRepository;

    public Paciente registrarPaciente(Paciente paciente) {
        if (pacienteRepository.findByDni(paciente.getDni()).isPresent()) {
            throw new RuntimeException("Ya existe un paciente con ese DNI");
        }

        if (paciente.getEstado() == null || paciente.getEstado().isEmpty()) {
            paciente.setEstado("ACTIVO");
        }
        if (paciente.getFechaRegistro() == null) {
            paciente.setFechaRegistro(LocalDateTime.now());
        }

        Paciente pacienteGuardado = pacienteRepository.save(paciente);

        HistoriaClinica historia = new HistoriaClinica();
        historia.setPacienteId(pacienteGuardado.getIdPaciente());
        historia.setFechaApertura(LocalDate.now());
        historia.setObservaciones("Historia clínica generada automáticamente");
        historiaRepository.save(historia);

        return pacienteGuardado;
    }

    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    public Paciente buscarPorId(String id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
    }

    public Paciente buscarPorDni(String dni) {
        return pacienteRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con DNI: " + dni));
    }

    public Paciente actualizarPaciente(String id, Paciente paciente) {
        Paciente pacienteExistente = buscarPorId(id);

        if (paciente.getNombres() != null) {
            pacienteExistente.setNombres(paciente.getNombres());
        }
        if (paciente.getApellidos() != null) {
            pacienteExistente.setApellidos(paciente.getApellidos());
        }
        if (paciente.getDireccion() != null) {
            pacienteExistente.setDireccion(paciente.getDireccion());
        }
        if (paciente.getTelefono() != null) {
            pacienteExistente.setTelefono(paciente.getTelefono());
        }
        if (paciente.getCorreo() != null) {
            pacienteExistente.setCorreo(paciente.getCorreo());
        }

        return pacienteRepository.save(pacienteExistente);
    }

    public void desactivarPaciente(String id) {
        Paciente paciente = buscarPorId(id);
        paciente.setEstado("INACTIVO");
        pacienteRepository.save(paciente);
    }

    public List<Paciente> listarPacientesActivos() {
        return pacienteRepository.findByEstado("ACTIVO");
    }

    public List<HistoriaClinica> obtenerHistorias(String idPaciente) {
        buscarPorId(idPaciente);
        return historiaRepository.findByPacienteId(idPaciente);
    }

    public AntecedenteMedico registrarAntecedente(String idHistoria, AntecedenteMedico antecedente) {
        historiaRepository.findById(idHistoria)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada con ID: " + idHistoria));

        antecedente.setHistoriaId(idHistoria);

        if (antecedente.getFechaRegistro() == null) {
            antecedente.setFechaRegistro(LocalDateTime.now());
        }

        return antecedenteRepository.save(antecedente);
    }

    public List<AntecedenteMedico> obtenerAntecedentes(String idHistoria) {
        historiaRepository.findById(idHistoria)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada con ID: " + idHistoria));

        return antecedenteRepository.findByHistoriaId(idHistoria);
    }
}