package com.example.backend.service;

import com.example.backend.dto.TacheDto;

import java.util.List;

public interface TacheService {
    TacheDto ajouterTache(TacheDto dto);
    List<TacheDto> getTachesParProjet(Long projetId);
    List<TacheDto> getTachesParAssigneeEmail(String assigneeEmail);  // Nouvelle méthode pour récupérer les tâches par email
    TacheDto getTacheById(Long taskId);
    TacheDto modifierTache(Long id, TacheDto dto);
    void supprimerTache(Long id);
    List<TacheDto> getTachesParProjetEtMembre(Long projetId, String email);
}
