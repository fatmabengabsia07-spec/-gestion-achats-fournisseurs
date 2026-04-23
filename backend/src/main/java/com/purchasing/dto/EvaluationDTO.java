package com.purchasing.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationDTO {
    private Long fournisseurId;
    private String nomFournisseur;
    private Double noteGlobale;
    private Double avgDelaiLivraison;
    private Long nombreCommandes;
    private Double totalAchats;
    private String appreciation; // "Excellent", "Bon", "Moyen", "Insuffisant"
}