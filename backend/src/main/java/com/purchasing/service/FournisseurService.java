package com.purchasing.service;

import com.purchasing.dto.EvaluationDTO;
import com.purchasing.dto.FournisseurDTO;
import com.purchasing.entity.Fournisseur;
import com.purchasing.exception.ResourceNotFoundException;
import com.purchasing.repository.CommandeAchatRepository;
import com.purchasing.repository.FournisseurRepository;
import com.purchasing.repository.HistoriqueAchatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FournisseurService {

    private final FournisseurRepository fournisseurRepository;
    private final HistoriqueAchatsRepository historiqueRepository;
    private final CommandeAchatRepository commandeRepository;

    public List<FournisseurDTO> getAll() {
        return fournisseurRepository.findAll().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public FournisseurDTO getById(Long id) {
        return toDTO(findById(id));
    }

    public FournisseurDTO create(FournisseurDTO dto) {
        Fournisseur entity = toEntity(dto);
        return toDTO(fournisseurRepository.save(entity));
    }

    public FournisseurDTO update(Long id, FournisseurDTO dto) {
        Fournisseur existing = findById(id);
        existing.setNom(dto.getNom());
        existing.setContact(dto.getContact());
        existing.setQualiteService(dto.getQualiteService());
        existing.setNote(dto.getNote());
        return toDTO(fournisseurRepository.save(existing));
    }

    public void delete(Long id) {
        findById(id);
        fournisseurRepository.deleteById(id);
    }

    public List<FournisseurDTO> searchByNom(String nom) {
        return fournisseurRepository.findByNomContainingIgnoreCase(nom).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<FournisseurDTO> getTopFournisseurs(Double noteMin) {
        return fournisseurRepository.findTopFournisseurs(noteMin).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    // === Évaluation des fournisseurs selon leur efficacité ===
    public EvaluationDTO evaluerFournisseur(Long id) {
        Fournisseur f = findById(id);

        Double avgDelai = historiqueRepository.getAvgDelaiByFournisseur(id);
        Double totalAchats = commandeRepository.getTotalMontantByFournisseur(id);
        Long nbCommandes = (long) commandeRepository.findByFournisseurId(id).size();

        // Calcul note globale : combinaison qualite service + note + délai
        double noteCalc = calculerNoteGlobale(f.getQualiteService(), f.getNote(), avgDelai);

        String appreciation = getAppreciation(noteCalc);

        return EvaluationDTO.builder()
                .fournisseurId(id)
                .nomFournisseur(f.getNom())
                .noteGlobale(noteCalc)
                .avgDelaiLivraison(avgDelai != null ? avgDelai : 0.0)
                .nombreCommandes(nbCommandes)
                .totalAchats(totalAchats != null ? totalAchats : 0.0)
                .appreciation(appreciation)
                .build();
    }

    // === Comparaison des offres fournisseurs ===
    public List<EvaluationDTO> comparerFournisseurs() {
        return fournisseurRepository.findAllOrderByNoteDesc().stream()
                .map(f -> evaluerFournisseur(f.getId()))
                .collect(Collectors.toList());
    }

    private double calculerNoteGlobale(Integer qualite, Double note, Double avgDelai) {
        double score = 0.0;
        if (qualite != null) score += (qualite / 5.0) * 40; // 40% du score
        if (note != null)    score += (note / 10.0) * 40;   // 40% du score
        if (avgDelai != null && avgDelai > 0)
            score += Math.max(0, (1 - avgDelai / 30.0)) * 20; // 20% : moins le délai est long, mieux c'est
        return Math.round(score * 100.0) / 100.0;
    }

    private String getAppreciation(double note) {
        if (note >= 75) return "Excellent";
        if (note >= 50) return "Bon";
        if (note >= 25) return "Moyen";
        return "Insuffisant";
    }

    private Fournisseur findById(Long id) {
        return fournisseurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé avec id: " + id));
    }

    private FournisseurDTO toDTO(Fournisseur f) {
        return FournisseurDTO.builder()
                .id(f.getId()).nom(f.getNom()).contact(f.getContact())
                .qualiteService(f.getQualiteService()).note(f.getNote()).build();
    }

    private Fournisseur toEntity(FournisseurDTO dto) {
        return Fournisseur.builder()
                .nom(dto.getNom()).contact(dto.getContact())
                .qualiteService(dto.getQualiteService()).note(dto.getNote()).build();
    }
}