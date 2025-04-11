package com.example.backend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProjetDto {
    private Long id;
    private String nom;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Long createurId;

    //  Liste des e-mails des membres à ajouter
    private List<String> membresEmails;
}
