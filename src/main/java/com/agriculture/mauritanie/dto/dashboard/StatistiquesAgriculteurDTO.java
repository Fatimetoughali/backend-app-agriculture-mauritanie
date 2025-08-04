package com.agriculture.mauritanie.dto.dashboard;

import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
public class StatistiquesAgriculteurDTO {

    // Évolution mensuelle
    private Map<String, Long> evolutionParcellesParMois; // 12 derniers mois
    private Map<String, BigDecimal> evolutionSurfaceParMois;

    // Productivité par culture
    private Map<String, BigDecimal> surfaceMoyenneParCulture;
    private Map<String, Integer> dureeVieParCulture; // Durée moyenne en jours

    // Tendances saisonnières
    private Map<String, Long> plantationsParSaison;
    private Map<String, Long> recoltesParSaison;

    // Performance par région
    private Map<String, BigDecimal> rendementParRegion;

    // Historique des récoltes
    private Map<LocalDate, Long> historiqueRecoltes;

    // Utilisation des ressources
    private BigDecimal tauxUtilisationIrrigation;
    private Map<String, Long> repartitionTypeSol;
}