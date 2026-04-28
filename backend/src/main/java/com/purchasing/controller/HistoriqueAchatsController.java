package com.purchasing.controller;

import com.purchasing.entity.HistoriqueAchats;
import com.purchasing.service.HistoriqueAchatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historiques")
@RequiredArgsConstructor
@Tag(name = "Historique des achats")
public class HistoriqueAchatsController {

    private final HistoriqueAchatsService historiqueService;

    @GetMapping
    public ResponseEntity<List<HistoriqueAchats>> getAll() {
        return ResponseEntity.ok(historiqueService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoriqueAchats> getById(@PathVariable Long id) {
        return ResponseEntity.ok(historiqueService.getById(id));
    }

    @GetMapping("/fournisseur/{fournisseurId}")
    @Operation(summary = "Historique d'un fournisseur")
    public ResponseEntity<List<HistoriqueAchats>> getByFournisseur(
            @PathVariable Long fournisseurId) {
        return ResponseEntity.ok(historiqueService.getByFournisseur(fournisseurId));
    }

    @GetMapping("/fournisseur/{fournisseurId}/delai-moyen")
    @Operation(summary = "Délai moyen d'un fournisseur")
    public ResponseEntity<Double> getAvgDelai(@PathVariable Long fournisseurId) {
        return ResponseEntity.ok(historiqueService.getAvgDelai(fournisseurId));
    }

    @PostMapping("/fournisseur/{fournisseurId}")
    @Operation(summary = "Ajouter un historique d'achat")
    public ResponseEntity<HistoriqueAchats> create(
            @PathVariable Long fournisseurId,
            @RequestBody HistoriqueAchats historique) {  // ← @Valid supprimé
        return new ResponseEntity<>(
            historiqueService.create(fournisseurId, historique),
            HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoriqueAchats> update(
            @PathVariable Long id,
            @RequestBody HistoriqueAchats historique) {  // ← @Valid supprimé
        return ResponseEntity.ok(historiqueService.update(id, historique));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        historiqueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}