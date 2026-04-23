package com.purchasing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "lignes_commandes_achats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneCommandeAchat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id", nullable = false)
    @NotNull(message = "La commande est obligatoire")
    private CommandeAchat commande;

    @NotBlank(message = "Le produit est obligatoire")
    @Size(max = 200, message = "Le produit ne doit pas dépasser 200 caractères")
    @Column(nullable = false)
    private String produit;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être au moins 1")
    @Column(nullable = false)
    private Integer quantite;

    @NotNull(message = "Le prix unitaire est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix unitaire doit être positif")
    @Column(name = "prix_unitaire", nullable = false)
    private Double prixUnitaire;
}