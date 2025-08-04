package com.agriculture.mauritanie.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@DiscriminatorValue("FOURNISSEUR")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Fournisseur extends User {

    @Column(name = "entreprise", nullable = false, length = 150)
    private String entreprise;

    @Column(name = "type_produits", length = 200)
    private String typeProduits; // SEMENCES, ENGRAIS, PESTICIDES, OUTILS, EQUIPEMENTS

    @Column(name = "marques_representees", length = 300)
    private String marquesRepresentees; // Marques des produits représentées

    @Column(name = "zone_livraison", length = 200)
    private String zoneLivraison; // Zones de livraison

    @Column(name = "licence_numero", length = 100)
    private String licenceNumero;

    @Column(name = "agrement_ministere")
    private Boolean agrementMinistere = false;

    @Column(name = "service_conseil")
    private Boolean serviceConseil = false; // Offre des services de conseil

    @Column(name = "service_apres_vente")
    private Boolean serviceApresVente = false;

    @Column(name = "formation_technique")
    private Boolean formationTechnique = false; // Propose des formations

    @Column(name = "credit_client")
    private Boolean creditClient = false; // Offre du crédit aux clients

    @Column(name = "delai_livraison")
    private Integer delaiLivraison; // Délai de livraison en jours

    @Column(name = "garantie_produits")
    private Integer garantieProduits; // Durée de garantie en mois

    @Column(name = "stock_disponible")
    private Boolean stockDisponible = true;

    @Column(name = "prix_catalogue", length = 500)
    private String prixCatalogue; // Lien vers catalogue ou description des prix

    @Column(name = "conditions_paiement", length = 200)
    private String conditionsPaiement; // CONDITIONS de paiement

    @Column(name = "certifications", length = 300)
    private String certifications; // Certifications ISO, etc.

    @Column(name = "site_web", length = 200)
    private String siteWeb;

    @Column(name = "adresse_physique", length = 300)
    private String adressePhysique;

    @Column(name = "horaires_ouverture", length = 100)
    private String horairesOuverture;
}