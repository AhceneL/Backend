package com.example.backend.controller;

import com.example.backend.dto.TacheDto;
import com.example.backend.service.TacheService;
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

    @PostMapping
    public ResponseEntity<TacheDto> ajouterTache(@RequestBody TacheDto dto) {
        return ResponseEntity.ok(tacheService.ajouterTache(dto));
    }

    @GetMapping("/projet/{projetId}")
    public ResponseEntity<List<TacheDto>> getTachesParProjet(@PathVariable Long projetId) {
        return ResponseEntity.ok(tacheService.getTachesParProjet(projetId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TacheDto> modifierTache(@PathVariable Long id, @RequestBody TacheDto dto) {
        return ResponseEntity.ok(tacheService.modifierTache(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerTache(@PathVariable Long id) {
        tacheService.supprimerTache(id);
        return ResponseEntity.noContent().build();
    }
}
