package com.agriculture.mauritanie.dto.dashboard;

import com.agriculture.mauritanie.dto.parcelle.ParcelleResumeDTO;
import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DashboardAgriculteurDTO {

    // Statistiques générales
    private Long nombreTotalParcelles;
    private BigDecimal surfaceTotaleHectares;
    private Long nombreCulturesActives;
    private Long nombreCulturesEnRecolte;

    // Répartition par type de culture
    private Map<String, Long> parcellesParTypeCulture;
    private Map<String, BigDecimal> surfaceParTypeCulture;

    // Répartition par statut
    private Map<String, Long> parcellesParStatut;

    // Prochaines échéances
    private List<ParcelleResumeDTO> prochainesRecoltes; // Dans les 30 prochains jours
    private List<ParcelleResumeDTO> culturesEnRetard; // Dépassées

    // Activité récente
    private List<ParcelleResumeDTO> dernieresModifications;

    // Statistiques par région/commune
    private Map<String, Long> parcellesParRegion;
    private Map<String, Long> parcellesParCommune;

    // Informations d'irrigation
    private Long parcellesAvecIrrigation;
    private Long parcellesSansIrrigation;
    private BigDecimal pourcentageIrrigation;
}