package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Column(columnDefinition = "text")
    private String description;

    @Column(length = 50)
    private String statut = "en_attente"; // par défaut

    private LocalDate dateLimite;

    @ManyToOne
    @JoinColumn(name = "projet_id", nullable = false)
    private Projet projet;

    @Column(name = "assignee_email")
    private String assigneeEmail;

    @ElementCollection
    @CollectionTable(name = "commentaires", joinColumns = @JoinColumn(name = "tache_id"))
    @Column(name = "commentaire")
    private List<String> commentaires = new ArrayList<>();

    @Column(name = "fichier")
    private String fichier;
}
