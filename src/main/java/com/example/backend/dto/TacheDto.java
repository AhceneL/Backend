package com.example.backend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TacheDto {
    private Long id;
    private String titre;
    private String description;
    private String statut;
    private LocalDate dateLimite;
    private Long projetId;
    private String assigneeEmail; // Utiliser l'email du membre assigné
    private List<String> commentaires; // Liste des commentaires
    private String fichier;
}
