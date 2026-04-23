package com.purchasing.controller;

import com.purchasing.dto.EvaluationDTO;
import com.purchasing.dto.FournisseurDTO;
import com.purchasing.service.FournisseurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fournisseurs")
@RequiredArgsConstructor
@Tag(name = "Fournisseurs", description = "Gestion des fournisseurs")
public class FournisseurController {

    private final FournisseurService fournisseurService;

    @GetMapping
    @Operation(summary = "Lister tous les fournisseurs")
    public ResponseEntity<List<FournisseurDTO>> getAll() {
        return ResponseEntity.ok(fournisseurService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un fournisseur par ID")
    public ResponseEntity<FournisseurDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(fournisseurService.getById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher fournisseurs par nom")
    public ResponseEntity<List<FournisseurDTO>> search(@RequestParam String nom) {
        return ResponseEntity.ok(fournisseurService.searchByNom(nom));
    }

    @GetMapping("/top")
    @Operation(summary = "Top fournisseurs par note minimale")
    public ResponseEntity<List<FournisseurDTO>> getTop(
            @RequestParam(defaultValue = "7.0") Double noteMin) {
        return ResponseEntity.ok(fournisseurService.getTopFournisseurs(noteMin));
    }

    @PostMapping
    @Operation(summary = "Créer un fournisseur")
    public ResponseEntity<FournisseurDTO> create(@Valid @RequestBody FournisseurDTO dto) {
        return new ResponseEntity<>(fournisseurService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un fournisseur")
    public ResponseEntity<FournisseurDTO> update(
            @PathVariable Long id, @Valid @RequestBody FournisseurDTO dto) {
        return ResponseEntity.ok(fournisseurService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un fournisseur")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fournisseurService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/evaluation")
    @Operation(summary = "Évaluer l'efficacité d'un fournisseur")
    public ResponseEntity<EvaluationDTO> evaluer(@PathVariable Long id) {
        return ResponseEntity.ok(fournisseurService.evaluerFournisseur(id));
    }

    @GetMapping("/comparaison")
    @Operation(summary = "Comparer tous les fournisseurs")
    public ResponseEntity<List<EvaluationDTO>> comparer() {
        return ResponseEntity.ok(fournisseurService.comparerFournisseurs());
    }
}