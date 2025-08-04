package com.agriculture.mauritanie.dto.auth;

import com.agriculture.mauritanie.entity.LangueEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RegisterAcheteurRequest {

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

    // Champs spécifiques acheteur
    private String entreprise;
    private String typeAchat;
    private String produitsRecherches;

    @Min(value = 0, message = "La capacité de stockage doit être positive")
    private Double capaciteStockage;

    private String zoneAchat;
    private String frequenceAchat;
    private String modePaiement;

    @Min(value = 0, message = "Le délai de paiement doit être positif")
    private Integer delaiPaiement;

    private String exigencesQualite;
    private Boolean certificationRequise = false;

    @Min(value = 0, message = "Le volume mensuel doit être positif")
    private Double volumeMensuel;

    @Min(value = 0, message = "Le prix maximum doit être positif")
    private Double prixMaximum;

    private Boolean transportAssure = false;
    private String numeroLicense;
    private String secteurActivite;
}