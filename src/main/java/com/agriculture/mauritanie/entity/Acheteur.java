package com.agriculture.mauritanie.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@DiscriminatorValue("ACHETEUR")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Acheteur extends User {

    @Column(name = "entreprise", length = 150)
    private String entreprise;

    @Column(name = "type_achat", length = 50)
    private String typeAchat; // GROS, DETAIL, EXPORT, TRANSFORMATION

    @Column(name = "produits_recherches", length = 500)
    private String produitsRecherches; // Liste des produits séparés par virgule

    @Column(name = "capacite_stockage")
    private Double capaciteStockage; // en tonnes

    @Column(name = "zone_achat", length = 200)
    private String zoneAchat; // Zones géographiques d'achat

    @Column(name = "frequence_achat", length = 50)
    private String frequenceAchat; // QUOTIDIEN, HEBDOMADAIRE, MENSUEL, SAISONNIER

    @Column(name = "mode_paiement", length = 100)
    private String modePaiement; // ESPECES, VIREMENT, MOBILE_MONEY, CREDIT

    @Column(name = "delai_paiement")
    private Integer delaiPaiement; // en jours

    @Column(name = "exigences_qualite", length = 500)
    private String exigencesQualite;

    @Column(name = "certification_requise")
    private Boolean certificationRequise = false;

    @Column(name = "volume_mensuel")
    private Double volumeMensuel; // Volume d'achat mensuel en tonnes

    @Column(name = "prix_maximum")
    private Double prixMaximum; // Prix maximum par kg/tonne

    @Column(name = "transport_assure")
    private Boolean transportAssure = false;

    @Column(name = "numero_license", length = 100)
    private String numeroLicense; // Licence commerciale

    @Column(name = "secteur_activite", length = 100)
    private String secteurActivite; // ALIMENTAIRE, EXPORT, TRANSFORMATION, DISTRIBUTION
}