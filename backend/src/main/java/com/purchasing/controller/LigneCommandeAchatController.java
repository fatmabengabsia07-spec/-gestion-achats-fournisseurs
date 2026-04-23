package com.purchasing.controller;

import com.purchasing.entity.LigneCommandeAchat;
import com.purchasing.service.LigneCommandeAchatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lignes-commandes")
@RequiredArgsConstructor
@Tag(name = "Lignes de commande", description = "Gestion des lignes de commandes d'achat")
public class LigneCommandeAchatController {

    private final LigneCommandeAchatService ligneService;

    @GetMapping
    public ResponseEntity<List<LigneCommandeAchat>> getAll() {
        return ResponseEntity.ok(ligneService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LigneCommandeAchat> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ligneService.getById(id));
    }

    @GetMapping("/commande/{commandeId}")
    @Operation(summary = "Lignes d'une commande")
    public ResponseEntity<List<LigneCommandeAchat>> getByCommande(@PathVariable Long commandeId) {
        return ResponseEntity.ok(ligneService.getByCommande(commandeId));
    }

    @PostMapping("/commande/{commandeId}")
    @Operation(summary = "Ajouter une ligne à une commande")
    public ResponseEntity<LigneCommandeAchat> create(
            @PathVariable Long commandeId,
            @Valid @RequestBody LigneCommandeAchat ligne) {
        return new ResponseEntity<>(ligneService.create(commandeId, ligne), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LigneCommandeAchat> update(
            @PathVariable Long id, @Valid @RequestBody LigneCommandeAchat ligne) {
        return ResponseEntity.ok(ligneService.update(id, ligne));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ligneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}