package com.example.backend.controller;

import com.example.backend.dto.TacheDto;
import com.example.backend.service.TacheService;
import com.example.backend.exceptions.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taches")
@CrossOrigin(origins = "http://localhost:4200")
public class TacheController {

    private final TacheService tacheService;

    public TacheController(TacheService tacheService) {
        this.tacheService = tacheService;
    }

    /**
     * Ajouter une tâche.
     * Cette méthode crée une tâche et affecte un membre en utilisant son email.
     * @param dto - Les données de la tâche à créer
     * @return ResponseEntity avec le DTO de la tâche ajoutée
     */
    @PostMapping
    public ResponseEntity<TacheDto> ajouterTache(@RequestBody TacheDto dto) {
        // Log des données reçues pour la tâche
        System.out.println("Données reçues pour ajouter une tâche : " + dto);

        try {
            TacheDto createdTask = tacheService.ajouterTache(dto);
            return ResponseEntity.ok(createdTask);
        } catch (NotFoundException e) {
            // Gérer le cas où un membre ou un projet est introuvable
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            // Gestion d'autres erreurs
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Récupérer les tâches d'un projet spécifique.
     * @param projetId - ID du projet pour lequel les tâches doivent être récupérées
     * @return ResponseEntity avec une liste des tâches associées au projet
     */
    @GetMapping("/projet/{projetId}")
    public ResponseEntity<List<TacheDto>> getTachesParProjet(@PathVariable Long projetId) {
        try {
            List<TacheDto> tasks = tacheService.getTachesParProjet(projetId);
            return ResponseEntity.ok(tasks);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(null); // Projet non trouvé
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Erreur interne du serveur
        }
    }

    /**
     * Modifier une tâche existante.
     * @param id - ID de la tâche à modifier
     * @param dto - Nouvelles données de la tâche
     * @return ResponseEntity avec le DTO de la tâche modifiée
     */
    @PutMapping("/{id}")
    public ResponseEntity<TacheDto> modifierTache(@PathVariable Long id, @RequestBody TacheDto dto) {
        try {
            TacheDto updatedTask = tacheService.modifierTache(id, dto);
            return ResponseEntity.ok(updatedTask);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(null); // Tâche non trouvée
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Erreur interne du serveur
        }
    }

    /**
     * Supprimer une tâche existante.
     * @param id - ID de la tâche à supprimer
     * @return ResponseEntity avec un statut de succès
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerTache(@PathVariable Long id) {
        try {
            tacheService.supprimerTache(id);
            return ResponseEntity.noContent().build(); // Succès de la suppression
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).build(); // Tâche à supprimer non trouvée
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // Erreur interne du serveur
        }
    }
}
