package com.purchasing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "commandes_achats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeAchat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fournisseur_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "commandes", "historiques"})
    private Fournisseur fournisseur;

    @NotNull(message = "La date est obligatoire")
    @Column(nullable = false)
    private LocalDate date;

    @NotBlank(message = "Le statut est obligatoire")
    @Pattern(regexp = "EN_ATTENTE|EN_COURS|LIVREE|ANNULEE",
             message = "Statut invalide")
    @Column(nullable = false)
    private String statut;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false)
    private Double montant;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("commande")
    private List<LigneCommandeAchat> lignes;
}