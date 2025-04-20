package com.example.backend.impl;

import com.example.backend.dto.TacheDto;
import com.example.backend.exceptions.NotFoundException;
import com.example.backend.model.Projet;
import com.example.backend.model.Tache;
import com.example.backend.model.User;
import com.example.backend.repository.ProjetRepository;
import com.example.backend.repository.TacheRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.TacheService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TacheServiceImpl implements TacheService {

    private final TacheRepository tacheRepo;
    private final ProjetRepository projetRepo;
    private final UserRepository userRepo;

    public TacheServiceImpl(TacheRepository tacheRepo, ProjetRepository projetRepo, UserRepository userRepo) {
        this.tacheRepo = tacheRepo;
        this.projetRepo = projetRepo;
        this.userRepo = userRepo;
    }

    @Override
    public TacheDto ajouterTache(TacheDto dto) {
        // Trouver le projet par ID
        Projet projet = projetRepo.findById(dto.getProjetId())
                .orElseThrow(() -> new NotFoundException("Projet non trouvé"));

        // Trouver le membre assigné par ID
        User assignee = null;
        if (dto.getAssigneeId() != null) {
            assignee = userRepo.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new NotFoundException("Utilisateur assigné non trouvé"));
        }

        // Créer la tâche
        Tache tache = new Tache();
        tache.setTitre(dto.getTitre());
        tache.setDescription(dto.getDescription());
        tache.setStatut(dto.getStatut());
        tache.setDateLimite(dto.getDateLimite());
        tache.setProjet(projet);
        tache.setAssignee(assignee);

        // Sauvegarder la tâche et retourner le DTO
        return toDto(tacheRepo.save(tache));
    }

    @Override
    public List<TacheDto> getTachesParProjet(Long projetId) {
        // Récupérer toutes les tâches du projet et les convertir en DTO
        return tacheRepo.findByProjetId(projetId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TacheDto modifierTache(Long id, TacheDto dto) {
        // Trouver la tâche par ID
        Tache tache = tacheRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Tâche non trouvée"));

        // Modifier la tâche avec les nouvelles données
        tache.setTitre(dto.getTitre());
        tache.setDescription(dto.getDescription());
        tache.setStatut(dto.getStatut());
        tache.setDateLimite(dto.getDateLimite());

        // Si un assignee (membre assigné) est spécifié, vérifier son existence
        if (dto.getAssigneeId() != null) {
            User assignee = userRepo.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new NotFoundException("Utilisateur assigné non trouvé"));
            tache.setAssignee(assignee);
        }

        // Sauvegarder et retourner la tâche mise à jour
        return toDto(tacheRepo.save(tache));
    }

    @Override
    public void supprimerTache(Long id) {
        // Vérifier si la tâche existe avant de la supprimer
        if (!tacheRepo.existsById(id)) {
            throw new NotFoundException("Tâche à supprimer non trouvée");
        }
        tacheRepo.deleteById(id); // Supprimer la tâche
    }

    private TacheDto toDto(Tache t) {
        // Convertir une entité Tache en DTO pour la réponse
        TacheDto dto = new TacheDto();
        dto.setId(t.getId());
        dto.setTitre(t.getTitre());
        dto.setDescription(t.getDescription());
        dto.setStatut(t.getStatut());
        dto.setDateLimite(t.getDateLimite());
        dto.setProjetId(t.getProjet().getId());
        dto.setAssigneeId(t.getAssignee() != null ? t.getAssignee().getId() : null);
        return dto;
    }
}
