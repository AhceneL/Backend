package com.example.backend.controller;

import com.example.backend.dto.ProjetDto;
import com.example.backend.service.ProjetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projets")
@CrossOrigin(origins = "http://localhost:4200")
public class ProjetController {

    private final ProjetService projetService;

    public ProjetController(ProjetService projetService) {
        this.projetService = projetService;
    }

    // ✅ Créer un projet (l'utilisateur connecté est détecté via le SecurityContext)
    @PostMapping
    public ResponseEntity<ProjetDto> create(@RequestBody ProjetDto dto) {
        return ResponseEntity.ok(projetService.createProjet(dto));
    }

    // ✅ Récupérer un projet par ID
    @GetMapping("/{id}")
    public ResponseEntity<ProjetDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(projetService.getProjetById(id));
    }

    // ✅ Lister tous les projets
    @GetMapping
    public ResponseEntity<List<ProjetDto>> getAll() {
        return ResponseEntity.ok(projetService.getAllProjets());
    }

    // ✅ Récupérer les projets du créateur connecté
    @GetMapping("/mes-projets")
    public ResponseEntity<List<ProjetDto>> getMesProjets() {
        return ResponseEntity.ok(projetService.getProjetsDuCreateur());
    }

    // ✅ Modifier un projet
    @PutMapping("/{id}")
    public ResponseEntity<ProjetDto> update(@PathVariable Long id, @RequestBody ProjetDto dto) {
        return ResponseEntity.ok(projetService.updateProjet(id, dto));
    }

    // ✅ Supprimer un projet
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projetService.deleteProjet(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Ajouter un membre à un projet
    @PutMapping("/{id}/ajouter-membre")
    public ResponseEntity<Void> addMemberToProject(@PathVariable Long id, @RequestBody Map<String, String> email) {
        projetService.addMemberToProject(id, email.get("email"));
        return ResponseEntity.ok().build();
    }
}
