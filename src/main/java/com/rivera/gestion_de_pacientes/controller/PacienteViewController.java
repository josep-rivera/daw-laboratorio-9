package com.rivera.gestion_de_pacientes.controller;

import com.rivera.gestion_de_pacientes.model.Paciente;
import com.rivera.gestion_de_pacientes.model.HistoriaClinica;
import com.rivera.gestion_de_pacientes.model.AntecedenteMedico;
import com.rivera.gestion_de_pacientes.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/pacientes")
public class PacienteViewController {

    @Autowired
    private PacienteService pacienteService;

    // Página principal - Lista de pacientes
    @GetMapping
    public String listarPacientes(Model model) {
        model.addAttribute("pacientes", pacienteService.listarPacientesActivos());
        model.addAttribute("titulo", "Gestión de Pacientes");
        return "index";
    }

    // Mostrar formulario de nuevo paciente
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("paciente", new Paciente());
        model.addAttribute("titulo", "Registrar Nuevo Paciente");
        model.addAttribute("accion", "nuevo");
        return "paciente_form";
    }

    // Guardar nuevo paciente
    @PostMapping("/guardar")
    public String guardarPaciente(@Valid @ModelAttribute("paciente") Paciente paciente,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        // Validar errores
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Registrar Nuevo Paciente");
            model.addAttribute("accion", "nuevo");
            return "paciente_form";
        }

        try {
            pacienteService.registrarPaciente(paciente);
            redirectAttributes.addFlashAttribute("success", "Paciente registrado exitosamente");
            return "redirect:/pacientes";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("titulo", "Registrar Nuevo Paciente");
            model.addAttribute("accion", "nuevo");
            return "paciente_form";
        }
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Paciente paciente = pacienteService.buscarPorId(id);
            model.addAttribute("paciente", paciente);
            model.addAttribute("titulo", "Editar Paciente");
            model.addAttribute("accion", "editar");
            return "paciente_form";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Paciente no encontrado");
            return "redirect:/pacientes";
        }
    }

    // Actualizar paciente
    @PostMapping("/actualizar/{id}")
    public String actualizarPaciente(@PathVariable String id,
                                     @Valid @ModelAttribute("paciente") Paciente paciente,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Editar Paciente");
            model.addAttribute("accion", "editar");
            return "paciente_form";
        }

        try {
            pacienteService.actualizarPaciente(id, paciente);
            redirectAttributes.addFlashAttribute("success", "Paciente actualizado exitosamente");
            return "redirect:/pacientes";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("titulo", "Editar Paciente");
            model.addAttribute("accion", "editar");
            return "paciente_form";
        }
    }

    // Ver detalle del paciente
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Paciente paciente = pacienteService.buscarPorId(id);
            model.addAttribute("paciente", paciente);
            model.addAttribute("historias", pacienteService.obtenerHistorias(id));
            model.addAttribute("titulo", "Detalle del Paciente");
            return "detalle_paciente";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Paciente no encontrado");
            return "redirect:/pacientes";
        }
    }

    // Desactivar paciente
    @GetMapping("/desactivar/{id}")
    public String desactivarPaciente(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            pacienteService.desactivarPaciente(id);
            redirectAttributes.addFlashAttribute("success", "Paciente desactivado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo desactivar el paciente");
        }
        return "redirect:/pacientes";
    }

    // Ver antecedentes de una historia clínica
    @GetMapping("/historia/{idHistoria}/antecedentes")
    public String verAntecedentes(@PathVariable String idHistoria, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("antecedentes", pacienteService.obtenerAntecedentes(idHistoria));
            model.addAttribute("idHistoria", idHistoria);
            model.addAttribute("titulo", "Antecedentes Médicos");
            return "antecedentes";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Historia clínica no encontrada");
            return "redirect:/pacientes";
        }
    }

    // Mostrar formulario para nuevo antecedente
    @GetMapping("/historia/{idHistoria}/antecedentes/nuevo")
    public String mostrarFormularioAntecedente(@PathVariable String idHistoria, Model model) {
        model.addAttribute("antecedente", new AntecedenteMedico());
        model.addAttribute("idHistoria", idHistoria);
        model.addAttribute("titulo", "Registrar Antecedente Médico");
        return "antecedente_form";
    }

    // Guardar nuevo antecedente
    @PostMapping("/historia/{idHistoria}/antecedentes/guardar")
    public String guardarAntecedente(@PathVariable String idHistoria,
                                     @Valid @ModelAttribute("antecedente") AntecedenteMedico antecedente,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        if (result.hasErrors()) {
            model.addAttribute("idHistoria", idHistoria);
            model.addAttribute("titulo", "Registrar Antecedente Médico");
            return "antecedente_form";
        }

        try {
            pacienteService.registrarAntecedente(idHistoria, antecedente);
            redirectAttributes.addFlashAttribute("success", "Antecedente registrado exitosamente");
            return "redirect:/pacientes/historia/" + idHistoria + "/antecedentes";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("idHistoria", idHistoria);
            model.addAttribute("titulo", "Registrar Antecedente Médico");
            return "antecedente_form";
        }
    }

    // Búsqueda por DNI
    @GetMapping("/buscar")
    public String buscarPorDni(@RequestParam(required = false) String dni, Model model) {
        if (dni != null && !dni.isEmpty()) {
            try {
                Paciente paciente = pacienteService.buscarPorDni(dni);
                return "redirect:/pacientes/detalle/" + paciente.getIdPaciente();
            } catch (RuntimeException e) {
                model.addAttribute("error", "No se encontró ningún paciente con DNI: " + dni);
            }
        }
        model.addAttribute("pacientes", pacienteService.listarPacientesActivos());
        model.addAttribute("titulo", "Gestión de Pacientes");
        return "index";
    }
}