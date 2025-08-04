package com.agriculture.mauritanie.dto.auth;

import com.agriculture.mauritanie.entity.LangueEnum;
import com.agriculture.mauritanie.entity.StatutEnum;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
public class AcheteurProfileResponse {
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

    // Données spécifiques acheteur
    private String entreprise;
    private String typeAchat;
    private String produitsRecherches;
    private Double capaciteStockage;
    private String zoneAchat;
    private String frequenceAchat;
    private String modePaiement;
    private Integer delaiPaiement;
    private String exigencesQualite;
    private Boolean certificationRequise;
    private Double volumeMensuel;
    private Double prixMaximum;
    private Boolean transportAssure;
    private String numeroLicense;
    private String secteurActivite;
}
