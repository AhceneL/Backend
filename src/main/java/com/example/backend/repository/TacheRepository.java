package com.example.backend.repository;

import com.example.backend.model.Tache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TacheRepository extends JpaRepository<Tache, Long> {

    // 🔍 Récupère toutes les tâches d'un projet
    List<Tache> findByProjetId(Long projetId);

    // 🔍 Récupère toutes les tâches d'un utilisateur assigné par email
    List<Tache> findByAssigneeEmail(String assigneeEmail);  // Nouvelle méthode pour rechercher par email
}
