package com.rivera.gestion_de_pacientes.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pacientes")
public class Paciente {

    @Id
    private String idPaciente;

    @NotBlank(message = "El DNI es obligatorio")
    private String dni;

    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El sexo es obligatorio")
    private String sexo;

    private String direccion;

    private String telefono;

    @Email(message = "El correo debe ser válido")
    private String correo;

    private String estado = "ACTIVO";

    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Transient
    private List<HistoriaClinica> historiasClinicas;
}