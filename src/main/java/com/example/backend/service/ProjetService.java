package com.example.backend.service;

import com.example.backend.dto.ProjetDto;

import java.util.List;

public interface ProjetService {

    // ✅ Création avec utilisateur connecté
    ProjetDto createProjet(ProjetDto dto);

    ProjetDto getProjetById(Long id);

    List<ProjetDto> getAllProjets();

    // ✅ Projets créés par l'utilisateur connecté
    List<ProjetDto> getProjetsDuCreateur();

    ProjetDto updateProjet(Long id, ProjetDto dto);

    void deleteProjet(Long id);

    // ✅ Ajouter un membre à un projet
    void addMemberToProject(Long projetId, String email);
    List<ProjetDto> getProjetsParMembre(String email);
}
