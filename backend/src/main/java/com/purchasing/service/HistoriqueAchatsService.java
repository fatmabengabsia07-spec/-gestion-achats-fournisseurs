package com.purchasing.service;

import com.purchasing.entity.Fournisseur;
import com.purchasing.entity.HistoriqueAchats;
import com.purchasing.exception.ResourceNotFoundException;
import com.purchasing.repository.FournisseurRepository;
import com.purchasing.repository.HistoriqueAchatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HistoriqueAchatsService {

    private final HistoriqueAchatsRepository historiqueRepository;
    private final FournisseurRepository fournisseurRepository;

    public List<HistoriqueAchats> getAll() {
        return historiqueRepository.findAll();
    }

    public HistoriqueAchats getById(Long id) {
        return historiqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historique non trouvé: " + id));
    }

    public List<HistoriqueAchats> getByFournisseur(Long fournisseurId) {
        return historiqueRepository.findByFournisseurId(fournisseurId);
    }

    public HistoriqueAchats create(Long fournisseurId, HistoriqueAchats historique) {

        Fournisseur fournisseur = fournisseurRepository.findById(fournisseurId)
                .orElseThrow(() -> new RuntimeException("Fournisseur introuvable"));

        // 🔥 FIX IMPORTANT
        historique.setFournisseur(fournisseur);

        return historiqueRepository.save(historique);
    }

    public HistoriqueAchats update(Long id, HistoriqueAchats updated) {
        HistoriqueAchats existing = getById(id);
        existing.setProduit(updated.getProduit());
        existing.setQuantite(updated.getQuantite());
        existing.setDelaiLivraison(updated.getDelaiLivraison());
        existing.setDateAchat(updated.getDateAchat());
        return historiqueRepository.save(existing);
    }

    public void delete(Long id) {
        getById(id);
        historiqueRepository.deleteById(id);
    }

    public Double getAvgDelai(Long fournisseurId) {
        return historiqueRepository.getAvgDelaiByFournisseur(fournisseurId);
    }
}