
// Agriculteur.java
package com.agriculture.mauritanie.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@DiscriminatorValue("AGRICULTEUR")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Agriculteur extends User {

    @Column(name = "type_culture", length = 100)
    private String typeCulture;

    @Column(name = "surface_totale")
    private Double surfaceTotale; // en hectares

    @Column(name = "annees_experience")
    private Integer anneesExperience;

    @Column(name = "est_cooperative")
    private Boolean estCooperative = false;

    @Column(name = "cooperative_nom", length = 150)
    private String cooperativeNom;

    @Column(name = "certification_bio")
    private Boolean certificationBio = false;

    @Column(name = "methode_irrigation", length = 50)
    private String methodeIrrigation; // GOUTTE_A_GOUTTE, ASPERSION, GRAVITAIRE, etc.

    @Column(name = "equipements", length = 500)
    private String equipements; // Liste des équipements séparés par virgule

    @Column(name = "production_annuelle")
    private Double productionAnnuelle; // en tonnes

    @Column(name = "objectif_production", length = 200)
    private String objectifProduction; // COMMERCIALE, SUBSISTANCE, MIXTE

    @Column(name = "formation_agricole")
    private Boolean formationAgricole = false;

    @Column(name = "acces_credit")
    private Boolean accesCredit = false;

    @Column(name = "problemes_principaux", length = 500)
    private String problemesPrincipaux; // EAU, SEMENCES, MALADIES, MARCHE, etc.
}
