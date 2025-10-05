package com.rivera.gestion_de_pacientes.service;

import com.rivera.gestion_de_pacientes.model.Paciente;
import com.rivera.gestion_de_pacientes.model.HistoriaClinica;
import com.rivera.gestion_de_pacientes.model.AntecedenteMedico;
import com.rivera.gestion_de_pacientes.repository.PacienteRepository;
import com.rivera.gestion_de_pacientes.repository.HistoriaClinicaRepository;
import com.rivera.gestion_de_pacientes.repository.AntecedenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public Paciente registrarPaciente(Paciente paciente) {
        // Validar DNI único
        if (pacienteRepository.findByDni(paciente.getDni()).isPresent()) {
            throw new RuntimeException("Ya existe un paciente con ese DNI");
        }

        // Establecer valores por defecto
        if (paciente.getEstado() == null || paciente.getEstado().isEmpty()) {
            paciente.setEstado("ACTIVO");
        }
        if (paciente.getFechaRegistro() == null) {
            paciente.setFechaRegistro(LocalDateTime.now());
        }

        // Guardar paciente
        Paciente pacienteGuardado = pacienteRepository.save(paciente);

        // Crear automáticamente la historia clínica
        HistoriaClinica historia = new HistoriaClinica();
        historia.setPaciente(pacienteGuardado);
        historia.setFechaApertura(LocalDate.now());
        historia.setObservaciones("Historia clínica generada automáticamente");
        historiaRepository.save(historia);

        return pacienteGuardado;
    }

    @Transactional(readOnly = true)
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Paciente buscarPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
        // Forzar la carga de historias clínicas para evitar LazyInitializationException
        if (paciente.getHistoriasClinicas() != null) {
            paciente.getHistoriasClinicas().size();
        }
        return paciente;
    }

    @Transactional(readOnly = true)
    public Paciente buscarPorDni(String dni) {
        Paciente paciente = pacienteRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con DNI: " + dni));
        // Forzar la carga de historias clínicas
        if (paciente.getHistoriasClinicas() != null) {
            paciente.getHistoriasClinicas().size();
        }
        return paciente;
    }

    @Transactional
    public Paciente actualizarPaciente(Long id, Paciente paciente) {
        Paciente pacienteExistente = buscarPorId(id);

        // Actualizar solo los campos modificables
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

    @Transactional
    public void desactivarPaciente(Long id) {
        Paciente paciente = buscarPorId(id);
        paciente.setEstado("INACTIVO");
        pacienteRepository.save(paciente);
    }

    @Transactional(readOnly = true)
    public List<Paciente> listarPacientesActivos() {
        return pacienteRepository.findByEstado("ACTIVO");
    }

    @Transactional(readOnly = true)
    public List<HistoriaClinica> obtenerHistorias(Long idPaciente) {
        // Verificar que el paciente existe
        buscarPorId(idPaciente);
        List<HistoriaClinica> historias = historiaRepository.findByPacienteIdPaciente(idPaciente);
        // Forzar la carga de antecedentes para cada historia
        for (HistoriaClinica historia : historias) {
            if (historia.getAntecedentes() != null) {
                historia.getAntecedentes().size();
            }
        }
        return historias;
    }

    @Transactional
    public AntecedenteMedico registrarAntecedente(Long idHistoria, AntecedenteMedico antecedente) {
        HistoriaClinica historia = historiaRepository.findById(idHistoria)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada con ID: " + idHistoria));

        antecedente.setHistoriaClinica(historia);

        if (antecedente.getFechaRegistro() == null) {
            antecedente.setFechaRegistro(LocalDateTime.now());
        }

        return antecedenteRepository.save(antecedente);
    }

    @Transactional(readOnly = true)
    public List<AntecedenteMedico> obtenerAntecedentes(Long idHistoria) {
        // Verificar que la historia existe
        historiaRepository.findById(idHistoria)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada con ID: " + idHistoria));

        return antecedenteRepository.findByHistoriaClinicaIdHistoria(idHistoria);
    }
}