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
        Projet projet = new Projet();
        projet.setNom(dto.getNom());
        projet.setDescription(dto.getDescription());
        projet.setDateDebut(dto.getDateDebut());
        projet.setDateFin(dto.getDateFin());

        // 🔐 Extraire l’utilisateur connecté à partir du token JWT
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User createur = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utilisateur connecté non trouvé"));
        projet.setCreateur(createur);

        Projet saved = projetRepo.save(projet);
        return toDto(saved);
    }

    @Override
    public ProjetDto getProjetById(Long id) {
        return projetRepo.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NotFoundException("Projet non trouvé"));
    }

    @Override
    public List<ProjetDto> getAllProjets() {
        return projetRepo.findAll().stream()
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

    // 📤 Convertir entité vers DTO
    private ProjetDto toDto(Projet projet) {
        ProjetDto dto = new ProjetDto();
        dto.setId(projet.getId());
        dto.setNom(projet.getNom());
        dto.setDescription(projet.getDescription());
        dto.setDateDebut(projet.getDateDebut());
        dto.setDateFin(projet.getDateFin());
        dto.setCreateurId(projet.getCreateur().getId());
        return dto;
    }
}
