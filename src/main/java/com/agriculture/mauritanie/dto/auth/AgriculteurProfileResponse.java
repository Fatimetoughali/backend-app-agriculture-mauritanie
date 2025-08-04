package com.agriculture.mauritanie.dto.auth;

import com.agriculture.mauritanie.entity.LangueEnum;
import com.agriculture.mauritanie.entity.StatutEnum;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
public class AgriculteurProfileResponse {
    // Données communes
    private Long id;
    private String nom;
    private String telephone;
    private String commune;
    private String region;
    private LangueEnum languePreferee;
    private StatutEnum statut;
    private LocalDateTime dateCreation;
    private LocalDateTime dateDerniereConnexion;

    // Données spécifiques agriculteur
    private String typeCulture;
    private Double surfaceTotale;
    private Integer anneesExperience;
    private Boolean estCooperative;
    private String cooperativeNom;
    private Boolean certificationBio;
    private String methodeIrrigation;
    private String equipements;
    private Double productionAnnuelle;
    private String objectifProduction;
    private Boolean formationAgricole;
    private Boolean accesCredit;
    private String problemesPrincipaux;
}
