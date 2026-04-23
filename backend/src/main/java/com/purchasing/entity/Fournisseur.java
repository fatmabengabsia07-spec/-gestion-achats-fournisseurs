package com.purchasing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "fournisseurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "Le contact est obligatoire")
    @Email(message = "Le contact doit être un email valide")
    @Column(nullable = false)
    private String contact;

    @NotNull(message = "La qualité de service est obligatoire")
    @Min(value = 1) @Max(value = 5)
    @Column(name = "qualite_service")
    private Integer qualiteService;

    @DecimalMin("0.0") @DecimalMax("10.0")
    private Double note;

    @JsonIgnore
    @OneToMany(mappedBy = "fournisseur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommandeAchat> commandes;

    @JsonIgnore
    @OneToMany(mappedBy = "fournisseur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistoriqueAchats> historiques;
}