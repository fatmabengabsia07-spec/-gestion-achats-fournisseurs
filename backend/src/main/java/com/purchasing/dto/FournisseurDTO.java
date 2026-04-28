package com.purchasing.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FournisseurDTO {

    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100)
    private String nom;

    @NotBlank(message = "Le contact est obligatoire")
    @Email(message = "Email invalide")
    private String contact;

    @NotNull
    @Min(1) @Max(5)
    private Integer qualiteService;

    @DecimalMin("0.0") @DecimalMax("10.0")
    private Double note;
}