package com.purchasing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "historique_achats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriqueAchats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fournisseur_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "commandes", "historiques"})
    private Fournisseur fournisseur;

    @NotBlank(message = "Le produit est obligatoire")
    @Size(max = 200)
    @Column(nullable = false)
    private String produit;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1)
    @Column(nullable = false)
    private Integer quantite;

    @NotNull(message = "Le délai de livraison est obligatoire")
    @Min(value = 0, message = "Le délai doit être positif")
    @Column(name = "delai_livraison")
    private Integer delaiLivraison;

    @Column(name = "date_achat")
    private LocalDate dateAchat;
}