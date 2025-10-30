package com.rivera.gestion_de_pacientes.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "antecedentes")
public class AntecedenteMedico {

    @Id
    private String idAntecedente;

    private String historiaId;

    private String tipo;

    private String descripcion;

    private LocalDateTime fechaRegistro = LocalDateTime.now();
}