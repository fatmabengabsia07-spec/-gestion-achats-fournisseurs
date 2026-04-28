package com.purchasing.service;

import com.purchasing.entity.CommandeAchat;
import com.purchasing.entity.LigneCommandeAchat;
import com.purchasing.exception.ResourceNotFoundException;
import com.purchasing.repository.CommandeAchatRepository;
import com.purchasing.repository.LigneCommandeAchatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LigneCommandeAchatService {

    private final LigneCommandeAchatRepository ligneRepository;
    private final CommandeAchatRepository commandeRepository;

    public List<LigneCommandeAchat> getAll() {
        return ligneRepository.findAll();
    }

    public LigneCommandeAchat getById(Long id) {
        return ligneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ligne non trouvée: " + id));
    }

    public List<LigneCommandeAchat> getByCommande(Long commandeId) {
        return ligneRepository.findByCommandeId(commandeId);
    }

    public LigneCommandeAchat create(Long commandeId, LigneCommandeAchat ligne) {
        CommandeAchat commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée: " + commandeId));
        ligne.setCommande(commande);
        return ligneRepository.save(ligne);
    }

    public LigneCommandeAchat update(Long id, LigneCommandeAchat updated) {
        LigneCommandeAchat existing = getById(id);
        existing.setProduit(updated.getProduit());
        existing.setQuantite(updated.getQuantite());
        existing.setPrixUnitaire(updated.getPrixUnitaire());
        return ligneRepository.save(existing);
    }

    public void delete(Long id) {
        getById(id);
        ligneRepository.deleteById(id);
    }
}