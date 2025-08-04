package com.agriculture.mauritanie.dto.auth;

import com.agriculture.mauritanie.entity.LangueEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

@Data
public class RegisterAgriculteurRequest {

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

    // Champs spécifiques agriculteur
    private String typeCulture;

    @Min(value = 0, message = "La surface doit être positive")
    private Double surfaceTotale;

    @Min(value = 0, message = "L'expérience doit être positive")
    private Integer anneesExperience;

    private Boolean estCooperative = false;
    private String cooperativeNom;
    private Boolean certificationBio = false;
    private String methodeIrrigation;
    private String equipements;
    private Double productionAnnuelle;
    private String objectifProduction;
    private Boolean formationAgricole = false;
    private Boolean accesCredit = false;
    private String problemesPrincipaux;
}