package com.example.backend.impl;

import com.example.backend.dto.TacheDto;
import com.example.backend.exceptions.NotFoundException;
import com.example.backend.model.Notification;
import com.example.backend.model.Projet;
import com.example.backend.model.Tache;
import com.example.backend.model.User;
import com.example.backend.repository.NotificationRepository;
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
    private final NotificationRepository notificationRepo;

    // Constructeur avec injection des dépendances
    public TacheServiceImpl(TacheRepository tacheRepo, ProjetRepository projetRepo, UserRepository userRepo, NotificationRepository notificationRepo) {
        this.tacheRepo = tacheRepo;
        this.projetRepo = projetRepo;
        this.userRepo = userRepo;
        this.notificationRepo = notificationRepo;
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

        // Sauvegarder la tâche
        Tache savedTache = tacheRepo.save(tache);

        // Notifier les utilisateurs concernés
        if (assignee != null) {
            sendNotification(assignee.getEmail(), "Vous avez été assigné à la tâche: " + savedTache.getTitre(), "Tâche");
        }

        return toDto(savedTache);
    }

    @Override
    public List<TacheDto> getTachesParProjet(Long projetId) {
        return tacheRepo.findByProjetId(projetId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TacheDto> getTachesParProjetEtMembre(Long projetId, String email) {
        List<Tache> taches = tacheRepo.findByProjetId(projetId);
        return taches.stream()
                .filter(t -> t.getAssigneeEmail().equals(email))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TacheDto> getTachesParAssigneeEmail(String assigneeEmail) {
        return tacheRepo.findByAssigneeEmail(assigneeEmail).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TacheDto getTacheById(Long taskId) {
        Tache tache = tacheRepo.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Tâche non trouvée avec l'ID: " + taskId));
        return toDto(tache);
    }

    @Override
    public TacheDto modifierTache(Long id, TacheDto dto) {
        Tache tache = tacheRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Tâche non trouvée"));

        tache.setTitre(dto.getTitre());
        tache.setDescription(dto.getDescription());
        tache.setStatut(dto.getStatut());
        tache.setDateLimite(dto.getDateLimite());

        if (dto.getCommentaires() != null) {
            for (String commentaire : dto.getCommentaires()) {
                if (!tache.getCommentaires().contains(commentaire)) {
                    tache.getCommentaires().add(commentaire);
                }
            }
        }

        if (dto.getFichier() != null) {
            tache.setFichier(dto.getFichier());
        }

        Tache updatedTache = tacheRepo.save(tache);

        if (tache.getAssigneeEmail() != null) {
            sendNotification(tache.getAssigneeEmail(), "La tâche " + updatedTache.getTitre() + " a été mise à jour.", "Tâche");
        }

        return toDto(updatedTache);
    }

    @Override
    public void supprimerTache(Long id) {
        if (!tacheRepo.existsById(id)) {
            throw new NotFoundException("Tâche à supprimer non trouvée");
        }
        tacheRepo.deleteById(id);
    }

    private TacheDto toDto(Tache t) {
        TacheDto dto = new TacheDto();
        dto.setId(t.getId());
        dto.setTitre(t.getTitre());
        dto.setDescription(t.getDescription());
        dto.setStatut(t.getStatut());
        dto.setDateLimite(t.getDateLimite());
        dto.setProjetId(t.getProjet().getId());
        dto.setAssigneeEmail(t.getAssigneeEmail());  // Utilisation de l'email du membre
        dto.setCommentaires(t.getCommentaires());
        dto.setFichier(t.getFichier());
        return dto;
    }

    // Méthode pour envoyer une notification en utilisant l'email de l'utilisateur assigné
    private void sendNotification(String assigneeEmail, String message, String type) {
        // Créer la notification
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setType(type);  // Type de notification (par exemple, "Tâche", "Projet")
        notification.setAssigneeEmail(assigneeEmail);  // Assigner l'email de l'utilisateur concerné

        // Sauvegarder la notification dans la base de données
        notificationRepo.save(notification);
    }
}
