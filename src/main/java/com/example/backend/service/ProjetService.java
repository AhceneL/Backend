package com.example.backend.service;

import com.example.backend.dto.ProjetDto;

import java.util.List;

public interface ProjetService {

    // ✅ Création avec utilisateur connecté
    ProjetDto createProjet(ProjetDto dto);

    ProjetDto getProjetById(Long id);

    List<ProjetDto> getAllProjets();

    ProjetDto updateProjet(Long id, ProjetDto dto);

    void deleteProjet(Long id);
}
