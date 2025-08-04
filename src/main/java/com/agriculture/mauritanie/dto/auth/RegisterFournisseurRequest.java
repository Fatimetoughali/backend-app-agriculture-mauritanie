package com.agriculture.mauritanie.dto.auth;

import com.agriculture.mauritanie.entity.LangueEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RegisterFournisseurRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^\\+222[0-9]{8}$|^[0-9]{8}$", message = "Format téléphone invalide")
    private String telephone;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;

    private String commune;
    private String region;
    private LangueEnum languePreferee = LangueEnum.FR;

    // Champs spécifiques fournisseur
    @NotBlank(message = "L'entreprise est obligatoire")
    private String entreprise;

    private String typeProduits;
    private String marquesRepresentees;
    private String zoneLivraison;
    private String licenceNumero;
    private Boolean agrementMinistere = false;
    private Boolean serviceConseil = false;
    private Boolean serviceApresVente = false;
    private Boolean formationTechnique = false;
    private Boolean creditClient = false;

    @Min(value = 0, message = "Le délai de livraison doit être positif")
    private Integer delaiLivraison;

    @Min(value = 0, message = "La garantie doit être positive")
    private Integer garantieProduits;

    private Boolean stockDisponible = true;
    private String prixCatalogue;
    private String conditionsPaiement;
    private String certifications;
    private String siteWeb;
    private String adressePhysique;
    private String horairesOuverture;
}
