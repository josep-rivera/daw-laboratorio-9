package com.rivera.gestion_de_pacientes;

import com.rivera.gestion_de_pacientes.model.AntecedenteMedico;
import com.rivera.gestion_de_pacientes.model.HistoriaClinica;
import com.rivera.gestion_de_pacientes.model.Paciente;
import com.rivera.gestion_de_pacientes.repository.AntecedenteRepository;
import com.rivera.gestion_de_pacientes.repository.HistoriaClinicaRepository;
import com.rivera.gestion_de_pacientes.repository.PacienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    private final PacienteRepository pacienteRepository;
    private final HistoriaClinicaRepository historiaRepository;
    private final AntecedenteRepository antecedenteRepository;

    public DataLoader(PacienteRepository pacienteRepository,
                      HistoriaClinicaRepository historiaRepository,
                      AntecedenteRepository antecedenteRepository) {
        this.pacienteRepository = pacienteRepository;
        this.historiaRepository = historiaRepository;
        this.antecedenteRepository = antecedenteRepository;
    }

    @Override
    public void run(String... args) {
        if (pacienteRepository.count() > 0) {
            return;
        }

        Paciente p1 = new Paciente();
        p1.setDni("12345678");
        p1.setNombres("Juan");
        p1.setApellidos("Pérez");
        p1.setFechaNacimiento(LocalDate.of(1990, 5, 12));
        p1.setSexo("M");
        p1.setDireccion("Av. Siempre Viva 123");
        p1.setTelefono("999888777");
        p1.setCorreo("juan.perez@example.com");
        p1.setEstado("ACTIVO");
        p1.setFechaRegistro(LocalDateTime.now());
        p1 = pacienteRepository.save(p1);

        HistoriaClinica h1 = new HistoriaClinica();
        h1.setPacienteId(p1.getIdPaciente());
        h1.setFechaApertura(LocalDate.now());
        h1.setObservaciones("Historia inicial de Juan Pérez");
        h1 = historiaRepository.save(h1);

        AntecedenteMedico a1 = new AntecedenteMedico();
        a1.setHistoriaId(h1.getIdHistoria());
        a1.setTipo("Alergia");
        a1.setDescripcion("Alergia al polen");
        a1.setFechaRegistro(LocalDateTime.now());

        AntecedenteMedico a2 = new AntecedenteMedico();
        a2.setHistoriaId(h1.getIdHistoria());
        a2.setTipo("Cirugía");
        a2.setDescripcion("Apendicectomía en 2010");
        a2.setFechaRegistro(LocalDateTime.now());

        antecedenteRepository.saveAll(Arrays.asList(a1, a2));

        Paciente p2 = new Paciente();
        p2.setDni("87654321");
        p2.setNombres("María");
        p2.setApellidos("Lopez");
        p2.setFechaNacimiento(LocalDate.of(1985, 11, 3));
        p2.setSexo("F");
        p2.setDireccion("Calle Luna 456");
        p2.setTelefono("988777666");
        p2.setCorreo("maria.lopez@example.com");
        p2.setEstado("ACTIVO");
        p2.setFechaRegistro(LocalDateTime.now());
        p2 = pacienteRepository.save(p2);

        HistoriaClinica h2 = new HistoriaClinica();
        h2.setPacienteId(p2.getIdPaciente());
        h2.setFechaApertura(LocalDate.now());
        h2.setObservaciones("Historia inicial de María Lopez");
        historiaRepository.save(h2);
    }
}


