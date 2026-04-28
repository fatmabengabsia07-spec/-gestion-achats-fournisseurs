package com.purchasing.service;

import com.purchasing.dto.CommandeAchatDTO;
import com.purchasing.entity.CommandeAchat;
import com.purchasing.entity.Fournisseur;
import com.purchasing.exception.ResourceNotFoundException;
import com.purchasing.repository.CommandeAchatRepository;
import com.purchasing.repository.FournisseurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommandeAchatService {

    private final CommandeAchatRepository commandeRepository;
    private final FournisseurRepository fournisseurRepository;

    public List<CommandeAchatDTO> getAll() {
        return commandeRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CommandeAchatDTO getById(Long id) {
        return toDTO(findById(id));
    }

    public List<CommandeAchatDTO> getByFournisseur(Long fournisseurId) {
        return commandeRepository.findByFournisseurId(fournisseurId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<CommandeAchatDTO> getByStatut(String statut) {
        return commandeRepository.findByStatut(statut).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public CommandeAchatDTO create(CommandeAchatDTO dto) {
        Fournisseur f = fournisseurRepository.findById(dto.getFournisseurId())
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé: " + dto.getFournisseurId()));
        CommandeAchat commande = toEntity(dto, f);
        return toDTO(commandeRepository.save(commande));
    }

    public CommandeAchatDTO update(Long id, CommandeAchatDTO dto) {
        CommandeAchat existing = findById(id);
        Fournisseur f = fournisseurRepository.findById(dto.getFournisseurId())
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé: " + dto.getFournisseurId()));
        existing.setFournisseur(f);
        existing.setDate(dto.getDate());
        existing.setStatut(dto.getStatut());
        existing.setMontant(dto.getMontant());
        return toDTO(commandeRepository.save(existing));
    }

    // Mise à jour du statut uniquement (suivi livraison)
    public CommandeAchatDTO updateStatut(Long id, String statut) {
        CommandeAchat commande = findById(id);
        commande.setStatut(statut);
        return toDTO(commandeRepository.save(commande));
    }

    public void delete(Long id) {
        findById(id);
        commandeRepository.deleteById(id);
    }

    private CommandeAchat findById(Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec id: " + id));
    }

    private CommandeAchatDTO toDTO(CommandeAchat c) {
        return CommandeAchatDTO.builder()
                .id(c.getId())
                .fournisseurId(c.getFournisseur().getId())
                .date(c.getDate())
                .statut(c.getStatut())
                .montant(c.getMontant())
                .build();
    }

    private CommandeAchat toEntity(CommandeAchatDTO dto, Fournisseur f) {
        return CommandeAchat.builder()
                .fournisseur(f).date(dto.getDate())
                .statut(dto.getStatut()).montant(dto.getMontant()).build();
    }
}