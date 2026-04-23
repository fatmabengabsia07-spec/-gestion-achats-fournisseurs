package com.purchasing.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeAchatDTO {

    private Long id;

    @NotNull(message = "L'ID du fournisseur est requis")
    private Long fournisseurId;

    @NotNull
    private LocalDate date;

    @NotBlank
    @Pattern(regexp = "EN_ATTENTE|EN_COURS|LIVREE|ANNULEE")
    private String statut;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private Double montant;
}