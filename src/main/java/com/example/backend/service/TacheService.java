package com.example.backend.service;

import com.example.backend.dto.TacheDto;

import java.util.List;

public interface TacheService {
    TacheDto ajouterTache(TacheDto dto);
    List<TacheDto> getTachesParProjet(Long projetId);
    TacheDto modifierTache(Long id, TacheDto dto);
    void supprimerTache(Long id);
}
