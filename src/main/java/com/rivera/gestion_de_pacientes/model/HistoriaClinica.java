package com.rivera.gestion_de_pacientes.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "historias")
public class HistoriaClinica {

    @Id
    private String idHistoria;

    private String pacienteId;

    private LocalDate fechaApertura = LocalDate.now();

    private String observaciones;

    @Transient
    private List<AntecedenteMedico> antecedentes;
}