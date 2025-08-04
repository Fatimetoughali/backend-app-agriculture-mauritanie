package com.agriculture.mauritanie.dto.auth;

import com.agriculture.mauritanie.entity.LangueEnum;
import com.agriculture.mauritanie.entity.StatutEnum;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
public class FournisseurProfileResponse {
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

    // Données spécifiques fournisseur
    private String entreprise;
    private String typeProduits;
    private String marquesRepresentees;
    private String zoneLivraison;
    private String licenceNumero;
    private Boolean agrementMinistere;
    private Boolean serviceConseil;
    private Boolean serviceApresVente;
    private Boolean formationTechnique;
    private Boolean creditClient;
    private Integer delaiLivraison;
    private Integer garantieProduits;
    private Boolean stockDisponible;
    private String prixCatalogue;
    private String conditionsPaiement;
    private String certifications;
    private String siteWeb;
    private String adressePhysique;
    private String horairesOuverture;
}