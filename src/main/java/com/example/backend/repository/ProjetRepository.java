package com.example.backend.repository;

import com.example.backend.model.Projet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjetRepository extends JpaRepository<Projet, Long> {
    // Récupérer tous les projets d'un membre en fonction de son email
    List<Projet> findByMembres_Email(String email);
}
