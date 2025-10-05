package com.rivera.gestion_de_pacientes.controller;

import com.rivera.gestion_de_pacientes.model.Paciente;
import com.rivera.gestion_de_pacientes.model.HistoriaClinica;
import com.rivera.gestion_de_pacientes.model.AntecedenteMedico;
import com.rivera.gestion_de_pacientes.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    // Registrar paciente
    @PostMapping
    public ResponseEntity<Paciente> registrar(@Valid @RequestBody Paciente paciente) {
        try {
            Paciente nuevo = pacienteService.registrarPaciente(paciente);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Listar todos los pacientes
    @GetMapping
    public ResponseEntity<List<Paciente>> listar() {
        List<Paciente> pacientes = pacienteService.listarPacientes();
        return new ResponseEntity<>(pacientes, HttpStatus.OK);
    }

    // Listar solo activos
    @GetMapping("/activos")
    public ResponseEntity<List<Paciente>> listarActivos() {
        List<Paciente> pacientes = pacienteService.listarPacientesActivos();
        return new ResponseEntity<>(pacientes, HttpStatus.OK);
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscarPorId(@PathVariable Long id) {
        try {
            Paciente paciente = pacienteService.buscarPorId(id);
            return new ResponseEntity<>(paciente, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Buscar por DNI
    @GetMapping("/dni/{dni}")
    public ResponseEntity<Paciente> buscarPorDni(@PathVariable String dni) {
        try {
            Paciente paciente = pacienteService.buscarPorDni(dni);
            return new ResponseEntity<>(paciente, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar paciente
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> actualizar(@PathVariable Long id, @Valid @RequestBody Paciente paciente) {
        try {
            Paciente actualizado = pacienteService.actualizarPaciente(id, paciente);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Desactivar paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> desactivar(@PathVariable Long id) {
        try {
            pacienteService.desactivarPaciente(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtener historias clínicas de un paciente
    @GetMapping("/{id}/historias")
    public ResponseEntity<List<HistoriaClinica>> obtenerHistorias(@PathVariable Long id) {
        List<HistoriaClinica> historias = pacienteService.obtenerHistorias(id);
        return new ResponseEntity<>(historias, HttpStatus.OK);
    }

    // Registrar antecedente médico
    @PostMapping("/historias/{idHistoria}/antecedentes")
    public ResponseEntity<AntecedenteMedico> registrarAntecedente(
            @PathVariable Long idHistoria,
            @RequestBody AntecedenteMedico antecedente) {
        try {
            AntecedenteMedico nuevo = pacienteService.registrarAntecedente(idHistoria, antecedente);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Obtener antecedentes de una historia
    @GetMapping("/historias/{idHistoria}/antecedentes")
    public ResponseEntity<List<AntecedenteMedico>> obtenerAntecedentes(@PathVariable Long idHistoria) {
        List<AntecedenteMedico> antecedentes = pacienteService.obtenerAntecedentes(idHistoria);
        return new ResponseEntity<>(antecedentes, HttpStatus.OK);
    }
}