package com.example.backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TacheDto {
    private Long id;
    private String titre;
    private String description;
    private String statut;
    private LocalDate dateLimite;
    private Long projetId;
    private Long assigneeId;
}
