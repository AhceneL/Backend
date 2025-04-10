package com.example.backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjetDto {
    private Long id;
    private String nom;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Long createurId;
}
