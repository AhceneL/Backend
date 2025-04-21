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

        // Trouver le membre assigné par email
        User assignee = null;
        if (dto.getAssigneeEmail() != null) {
            assignee = userRepo.findByEmail(dto.getAssigneeEmail())  // Recherche par email
                    .orElseThrow(() -> new NotFoundException("Utilisateur assigné non trouvé"));
        }

        // Créer la tâche
        Tache tache = new Tache();
        tache.setTitre(dto.getTitre());
        tache.setDescription(dto.getDescription());
        tache.setStatut(dto.getStatut());
        tache.setDateLimite(dto.getDateLimite());
        tache.setProjet(projet);
        tache.setAssigneeEmail(dto.getAssigneeEmail());  // Assigner le membre via son email

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
    public List<TacheDto> getTachesParProjetEtMembre(Long projetId, String email) {
        // Récupérer toutes les tâches du projet et filtrer par l'email de l'assignee
        List<Tache> taches = tacheRepo.findByProjetId(projetId);
        return taches.stream()
                .filter(t -> t.getAssigneeEmail().equals(email))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TacheDto> getTachesParAssigneeEmail(String assigneeEmail) {
        // Récupérer toutes les tâches assignées à l'email
        return tacheRepo.findByAssigneeEmail(assigneeEmail).stream()
                .map(this::toDto) // Convertir en DTO
                .collect(Collectors.toList());
    }

    @Override
    public TacheDto getTacheById(Long taskId) {
        Tache tache = tacheRepo.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Tâche non trouvée avec l'ID: " + taskId));
        return toDto(tache);  // Retourne le DTO de la tâche
    }
    @Override
    public TacheDto modifierTache(Long id, TacheDto dto) {
        // Trouver la tâche par ID
        Tache tache = tacheRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Tâche non trouvée"));

        // Modifier les champs de la tâche
        tache.setTitre(dto.getTitre());
        tache.setDescription(dto.getDescription());
        tache.setStatut(dto.getStatut());
        tache.setDateLimite(dto.getDateLimite());

        // Mettre à jour les commentaires si présents
        if (dto.getCommentaires() != null) {
            // Ajouter uniquement les commentaires qui n'existent pas déjà
            for (String commentaire : dto.getCommentaires()) {
                if (!tache.getCommentaires().contains(commentaire)) {
                    tache.getCommentaires().add(commentaire);
                }
            }
        }

        // Mettre à jour le fichier si présent
        if (dto.getFichier() != null) {
            tache.setFichier(dto.getFichier());
        }

        // Enregistrer les modifications dans la base de données
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
        dto.setAssigneeEmail(t.getAssigneeEmail()); // Utilisation de l'email du membre
        dto.setCommentaires(t.getCommentaires());
        dto.setFichier(t.getFichier());
        return dto;
    }
}
