package com.example.backend.impl;

import com.example.backend.dto.ProjetDto;
import com.example.backend.exceptions.NotFoundException;
import com.example.backend.model.Projet;
import com.example.backend.model.User;
import com.example.backend.repository.ProjetRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.ProjetService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjetServiceImpl implements ProjetService {

    private final ProjetRepository projetRepo;
    private final UserRepository userRepo;

    public ProjetServiceImpl(ProjetRepository projetRepo, UserRepository userRepo) {
        this.projetRepo = projetRepo;
        this.userRepo = userRepo;
    }

    @Override
    public ProjetDto createProjet(ProjetDto dto) {
        System.out.println("📥 Reçu projet: " + dto);

        Projet projet = new Projet();
        projet.setNom(dto.getNom());
        projet.setDescription(dto.getDescription());
        projet.setDateDebut(dto.getDateDebut());
        projet.setDateFin(dto.getDateFin());

        try {
            // 🔐 Extraire l’utilisateur connecté
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println("🔑 Email extrait du token : " + email);

            User createur = userRepo.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("Utilisateur connecté non trouvé"));

            projet.setCreateur(createur);

            // ✅ Ajouter les membres
            List<User> membres = new ArrayList<>();
            if (dto.getMembresEmails() != null) {
                for (String emailMembre : dto.getMembresEmails()) {
                    User membre = userRepo.findByEmail(emailMembre.trim())
                            .orElseThrow(() -> new NotFoundException("Membre avec l'e-mail " + emailMembre + " non trouvé."));
                    membres.add(membre);
                }
            }
            projet.setMembres(membres);

            // 🔍 Debug : afficher les membres
            System.out.println("👥 Membres à enregistrer :");
            projet.getMembres().forEach(u -> System.out.println(" - " + u.getEmail()));

            Projet saved = projetRepo.save(projet);
            System.out.println("✅ Projet sauvegardé avec ID : " + saved.getId());

            return toDto(saved);

        } catch (Exception e) {
            System.out.println("❌ ERREUR lors de la création du projet : " + e.getMessage());
            throw e;
        }
    }

    @Override
    public ProjetDto getProjetById(Long id) {
        return projetRepo.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NotFoundException("Projet non trouvé"));
    }
    @Override
    public List<ProjetDto> getProjetsParMembre(String email) {
        List<Projet> projets = projetRepo.findByMembres_Email(email);  // Utiliser le repository pour récupérer les projets associés au membre
        return projets.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<ProjetDto> getAllProjets() {
        return projetRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjetDto> getProjetsDuCreateur() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User createur = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utilisateur connecté non trouvé"));

        return projetRepo.findAll().stream()
                .filter(p -> p.getCreateur().getId().equals(createur.getId()))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProjetDto updateProjet(Long id, ProjetDto dto) {
        Projet projet = projetRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Projet non trouvé"));

        projet.setNom(dto.getNom());
        projet.setDescription(dto.getDescription());
        projet.setDateDebut(dto.getDateDebut());
        projet.setDateFin(dto.getDateFin());

        Projet updated = projetRepo.save(projet);
        return toDto(updated);
    }

    @Override
    public void deleteProjet(Long id) {
        if (!projetRepo.existsById(id)) {
            throw new NotFoundException("Projet à supprimer non trouvé");
        }
        projetRepo.deleteById(id);
    }

    public void addMemberToProject(Long projetId, String email) {
        Projet projet = projetRepo.findById(projetId)
                .orElseThrow(() -> new NotFoundException("Projet non trouvé"));

        User membre = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Membre non trouvé avec l'email : " + email));

        projet.getMembres().add(membre); // Ajouter le membre à la liste des membres du projet
        projetRepo.save(projet); // Sauvegarder les modifications du projet
    }

    // 📤 Convertir entité vers DTO
    private ProjetDto toDto(Projet projet) {
        ProjetDto dto = new ProjetDto();
        dto.setId(projet.getId());
        dto.setNom(projet.getNom());
        dto.setDescription(projet.getDescription());
        dto.setDateDebut(projet.getDateDebut());
        dto.setDateFin(projet.getDateFin());
        dto.setCreateurId(projet.getCreateur().getId());
        dto.setCreateurEmail(projet.getCreateur().getEmail());

        // Ajouter la liste des emails des membres
        dto.setMembresEmails(projet.getMembres().stream()
                .map(User::getEmail)
                .collect(Collectors.toList()));
        // Récupérer les emails des membres
        dto.setMembresEmails(projet.getMembres().stream()
                .map(membre -> membre.getEmail())
                .collect(Collectors.toList()));
        return dto;
    }

}
