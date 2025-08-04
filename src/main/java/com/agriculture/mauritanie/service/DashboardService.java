// DashboardService.java
package com.agriculture.mauritanie.service;

import com.agriculture.mauritanie.dto.dashboard.DashboardAgriculteurDTO;
import com.agriculture.mauritanie.dto.dashboard.StatistiquesAgriculteurDTO;
import com.agriculture.mauritanie.dto.parcelle.ParcelleResumeDTO;
import com.agriculture.mauritanie.entity.Agriculteur;
import com.agriculture.mauritanie.entity.Parcelle;
import com.agriculture.mauritanie.entity.StatutCultureEnum;
import com.agriculture.mauritanie.repository.ParcelleRepository;
import com.agriculture.mauritanie.repository.UserRepository;
import com.agriculture.mauritanie.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final ParcelleRepository parcelleRepository;
    private final UserRepository userRepository;
    private final ParcelleService parcelleService;

    /**
     * Générer le dashboard complet pour un agriculteur
     */
    public DashboardAgriculteurDTO getDashboard(Long agriculteurId) {
        log.info("Génération du dashboard pour l'agriculteur: {}", agriculteurId);

        Agriculteur agriculteur = getAgriculteur(agriculteurId);

        // Statistiques de base
        Long nombreTotalParcelles = parcelleRepository.countByAgriculteur(agriculteur);
        BigDecimal surfaceTotale = parcelleRepository.sumSurfaceByAgriculteur(agriculteur);
        if (surfaceTotale == null) surfaceTotale = BigDecimal.ZERO;

        // Cultures actives (pas récoltées ou en repos)
        Long culturesActives = parcelleRepository.countByAgriculteurAndStatut(
                agriculteur, StatutCultureEnum.EN_CROISSANCE) +
                parcelleRepository.countByAgriculteurAndStatut(
                        agriculteur, StatutCultureEnum.EN_FLORAISON) +
                parcelleRepository.countByAgriculteurAndStatut(
                        agriculteur, StatutCultureEnum.MATURATION);

        Long culturesEnRecolte = parcelleRepository.countByAgriculteurAndStatut(
                agriculteur, StatutCultureEnum.PRET_A_RECOLTER);

        // Répartitions
        Map<String, Long> parcellesParTypeCulture = buildMapFromObjectArray(
                parcelleRepository.countByAgriculteurGroupByTypeCulture(agriculteur));

        Map<String, BigDecimal> surfaceParTypeCulture = buildBigDecimalMapFromObjectArray(
                parcelleRepository.sumSurfaceByAgriculteurGroupByTypeCulture(agriculteur));

        Map<String, Long> parcellesParStatut = buildStatutMapFromObjectArray(
                parcelleRepository.countByAgriculteurGroupByStatut(agriculteur));

        Map<String, Long> parcellesParRegion = buildMapFromObjectArray(
                parcelleRepository.countByAgriculteurGroupByRegion(agriculteur));

        Map<String, Long> parcellesParCommune = buildMapFromObjectArray(
                parcelleRepository.countByAgriculteurGroupByCommune(agriculteur));

        // Prochaines échéances (30 prochains jours)
        LocalDate maintenant = LocalDate.now();
        LocalDate dans30Jours = maintenant.plusDays(30);
        List<Parcelle> prochainesRecoltesEntities = parcelleRepository.findProchainesRecoltesByAgriculteur(
                agriculteur, maintenant, dans30Jours);
        List<ParcelleResumeDTO> prochainesRecoltes = prochainesRecoltesEntities.stream()
                .map(this::mapToResumeDTO)
                .collect(Collectors.toList());

        // Cultures en retard
        List<StatutCultureEnum> statutsTermines = Arrays.asList(
                StatutCultureEnum.RECOLTE, StatutCultureEnum.EN_REPOS
        );
        List<Parcelle> culturesEnRetardEntities = parcelleRepository.findCulturesEnRetardByAgriculteur(
                agriculteur, maintenant, statutsTermines);
        List<ParcelleResumeDTO> culturesEnRetard = culturesEnRetardEntities.stream()
                .map(this::mapToResumeDTO)
                .collect(Collectors.toList());

        // Dernières modifications (5 dernières)
        List<Parcelle> dernieresModifEntities = parcelleRepository.findRecentlyModifiedByAgriculteur(
                agriculteur, PageRequest.of(0, 5));
        List<ParcelleResumeDTO> dernieresModifications = dernieresModifEntities.stream()
                .map(this::mapToResumeDTO)
                .collect(Collectors.toList());

        // Statistiques irrigation
        Long parcellesAvecIrrigation = parcelleRepository.countByAgriculteurAndIrrigationTrue(agriculteur);
        Long parcellesSansIrrigation = parcelleRepository.countByAgriculteurAndIrrigationFalse(agriculteur);
        BigDecimal pourcentageIrrigation = calculatePourcentageIrrigation(
                parcellesAvecIrrigation, nombreTotalParcelles);

        return DashboardAgriculteurDTO.builder()
                .nombreTotalParcelles(nombreTotalParcelles)
                .surfaceTotaleHectares(surfaceTotale)
                .nombreCulturesActives(culturesActives)
                .nombreCulturesEnRecolte(culturesEnRecolte)
                .parcellesParTypeCulture(parcellesParTypeCulture)
                .surfaceParTypeCulture(surfaceParTypeCulture)
                .parcellesParStatut(parcellesParStatut)
                .prochainesRecoltes(prochainesRecoltes)
                .culturesEnRetard(culturesEnRetard)
                .dernieresModifications(dernieresModifications)
                .parcellesParRegion(parcellesParRegion)
                .parcellesParCommune(parcellesParCommune)
                .parcellesAvecIrrigation(parcellesAvecIrrigation)
                .parcellesSansIrrigation(parcellesSansIrrigation)
                .pourcentageIrrigation(pourcentageIrrigation)
                .build();
    }

    /**
     * Générer les statistiques détaillées pour un agriculteur
     */
    public StatistiquesAgriculteurDTO getStatistiques(Long agriculteurId) {
        log.info("Génération des statistiques pour l'agriculteur: {}", agriculteurId);

        Agriculteur agriculteur = getAgriculteur(agriculteurId);

        // Évolution sur 12 mois
        Map<String, Long> evolutionParcellesParMois = calculateEvolutionParcelles(agriculteur);
        Map<String, BigDecimal> evolutionSurfaceParMois = calculateEvolutionSurface(agriculteur);

        // Productivité par culture
        Map<String, BigDecimal> surfaceMoyenneParCulture = calculateSurfaceMoyenneParCulture(agriculteur);
        Map<String, Integer> dureeVieParCulture = calculateDureeVieParCulture(agriculteur);

        // Tendances saisonnières
        Map<String, Long> plantationsParSaison = calculatePlantationsParSaison(agriculteur);
        Map<String, Long> recoltesParSaison = calculateRecoltesParSaison(agriculteur);

        // Performance par région
        Map<String, BigDecimal> rendementParRegion = calculateRendementParRegion(agriculteur);

        // Historique des récoltes (6 derniers mois)
        Map<LocalDate, Long> historiqueRecoltes = calculateHistoriqueRecoltes(agriculteur);

        // Utilisation des ressources
        BigDecimal tauxUtilisationIrrigation = calculateTauxIrrigation(agriculteur);

        return StatistiquesAgriculteurDTO.builder()
                .evolutionParcellesParMois(evolutionParcellesParMois)
                .evolutionSurfaceParMois(evolutionSurfaceParMois)
                .surfaceMoyenneParCulture(surfaceMoyenneParCulture)
                .dureeVieParCulture(dureeVieParCulture)
                .plantationsParSaison(plantationsParSaison)
                .recoltesParSaison(recoltesParSaison)
                .rendementParRegion(rendementParRegion)
                .historiqueRecoltes(historiqueRecoltes)
                .tauxUtilisationIrrigation(tauxUtilisationIrrigation)
                .build();
    }

    // Méthodes utilitaires privées

    private Agriculteur getAgriculteur(Long agriculteurId) {
        return (Agriculteur) userRepository.findById(agriculteurId)
                .filter(user -> user instanceof Agriculteur)
                .orElseThrow(() -> new ResourceNotFoundException("Agriculteur non trouvé"));
    }

    private Map<String, Long> buildMapFromObjectArray(List<Object[]> results) {
        return results.stream()
                .collect(Collectors.toMap(
                        row -> row[0] != null ? row[0].toString() : "Non spécifié",
                        row -> ((Number) row[1]).longValue(),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new));
    }

    private Map<String, BigDecimal> buildBigDecimalMapFromObjectArray(List<Object[]> results) {
        return results.stream()
                .collect(Collectors.toMap(
                        row -> row[0] != null ? row[0].toString() : "Non spécifié",
                        row -> (BigDecimal) row[1],
                        (existing, replacement) -> existing,
                        LinkedHashMap::new));
    }

    private Map<String, Long> buildStatutMapFromObjectArray(List<Object[]> results) {
        return results.stream()
                .collect(Collectors.toMap(
                        row -> ((StatutCultureEnum) row[0]).getLibelle(),
                        row -> ((Number) row[1]).longValue(),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new));
    }

    private ParcelleResumeDTO mapToResumeDTO(Parcelle parcelle) {
        return ParcelleResumeDTO.builder()
                .id(parcelle.getId())
                .nomParcelle(parcelle.getNomParcelle())
                .surfaceHectares(parcelle.getSurfaceHectares())
                .typeCulture(parcelle.getTypeCulture())
                .commune(parcelle.getCommune())
                .statutCulture(parcelle.getStatutCulture())
                .statutCultureLibelle(parcelle.getStatutCulture().getLibelle())
                .datePlantation(parcelle.getDatePlantation())
                .dateRecoltePrevue(parcelle.getDateRecoltePrevue())
                .irrigation(parcelle.getIrrigation())
                .joursAvantRecolte(calculateJoursAvantRecolte(parcelle.getDateRecoltePrevue()))
                .build();
    }

    private Integer calculateJoursAvantRecolte(LocalDate dateRecoltePrevue) {
        if (dateRecoltePrevue == null) return null;

        LocalDate today = LocalDate.now();
        return (int) ChronoUnit.DAYS.between(today, dateRecoltePrevue);
    }

    private BigDecimal calculatePourcentageIrrigation(Long avecIrrigation, Long total) {
        if (total == 0) return BigDecimal.ZERO;

        return BigDecimal.valueOf(avecIrrigation)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
    }

    // Méthodes de calcul des statistiques avancées

    private Map<String, Long> calculateEvolutionParcelles(Agriculteur agriculteur) {
        Map<String, Long> evolution = new LinkedHashMap<>();
        LocalDate maintenant = LocalDate.now();

        for (int i = 11; i >= 0; i--) {
            LocalDate debutMois = maintenant.minusMonths(i).withDayOfMonth(1);
            LocalDate finMois = debutMois.plusMonths(1).minusDays(1);

            List<Parcelle> parcellesDuMois = parcelleRepository.findByAgriculteurAndDatePlantationBetween(
                    agriculteur, debutMois, finMois);

            String moisAnnee = debutMois.format(DateTimeFormatter.ofPattern("MM/yyyy"));
            evolution.put(moisAnnee, (long) parcellesDuMois.size());
        }

        return evolution;
    }

    private Map<String, BigDecimal> calculateEvolutionSurface(Agriculteur agriculteur) {
        Map<String, BigDecimal> evolution = new LinkedHashMap<>();
        LocalDate maintenant = LocalDate.now();

        for (int i = 11; i >= 0; i--) {
            LocalDate debutMois = maintenant.minusMonths(i).withDayOfMonth(1);
            LocalDate finMois = debutMois.plusMonths(1).minusDays(1);

            List<Parcelle> parcellesDuMois = parcelleRepository.findByAgriculteurAndDatePlantationBetween(
                    agriculteur, debutMois, finMois);

            BigDecimal surfaceTotale = parcellesDuMois.stream()
                    .map(Parcelle::getSurfaceHectares)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            String moisAnnee = debutMois.format(DateTimeFormatter.ofPattern("MM/yyyy"));
            evolution.put(moisAnnee, surfaceTotale);
        }

        return evolution;
    }

    private Map<String, BigDecimal> calculateSurfaceMoyenneParCulture(Agriculteur agriculteur) {
        Map<String, Long> counts = buildMapFromObjectArray(
                parcelleRepository.countByAgriculteurGroupByTypeCulture(agriculteur));
        Map<String, BigDecimal> surfaces = buildBigDecimalMapFromObjectArray(
                parcelleRepository.sumSurfaceByAgriculteurGroupByTypeCulture(agriculteur));

        Map<String, BigDecimal> moyennes = new LinkedHashMap<>();
        for (String culture : counts.keySet()) {
            BigDecimal surfaceTotale = surfaces.getOrDefault(culture, BigDecimal.ZERO);
            Long nombre = counts.get(culture);

            if (nombre > 0) {
                BigDecimal moyenne = surfaceTotale.divide(BigDecimal.valueOf(nombre), 2, RoundingMode.HALF_UP);
                moyennes.put(culture, moyenne);
            }
        }

        return moyennes;
    }

    private Map<String, Integer> calculateDureeVieParCulture(Agriculteur agriculteur) {
        // Simplifié - calcul basé sur des moyennes standard
        Map<String, Integer> durees = new HashMap<>();
        durees.put("Maraîchage", 90);
        durees.put("Céréales", 120);
        durees.put("Légumineuses", 100);
        durees.put("Arboriculture", 365);
        return durees;
    }

    private Map<String, Long> calculatePlantationsParSaison(Agriculteur agriculteur) {
        List<Parcelle> parcelles = parcelleRepository.findByAgriculteurOrderByDateCreationDesc(agriculteur);

        Map<String, Long> parSaison = new HashMap<>();
        parSaison.put("Saison sèche", 0L);
        parSaison.put("Saison des pluies", 0L);

        for (Parcelle parcelle : parcelles) {
            if (parcelle.getDatePlantation() != null) {
                int mois = parcelle.getDatePlantation().getMonthValue();
                if (mois >= 6 && mois <= 10) {  // Juin à Octobre = saison des pluies
                    parSaison.put("Saison des pluies", parSaison.get("Saison des pluies") + 1);
                } else {
                    parSaison.put("Saison sèche", parSaison.get("Saison sèche") + 1);
                }
            }
        }

        return parSaison;
    }

    private Map<String, Long> calculateRecoltesParSaison(Agriculteur agriculteur) {
        // Calcul similaire basé sur les dates de récolte prévues/effectives
        return calculatePlantationsParSaison(agriculteur); // Simplifié pour l'exemple
    }

    private Map<String, BigDecimal> calculateRendementParRegion(Agriculteur agriculteur) {
        Map<String, BigDecimal> rendements = new HashMap<>();
        Map<String, BigDecimal> surfaces = buildBigDecimalMapFromObjectArray(
                parcelleRepository.sumSurfaceByAgriculteurGroupByRegion(agriculteur));

        // Rendement fictif pour démonstration
        for (String region : surfaces.keySet()) {
            rendements.put(region, surfaces.get(region).multiply(BigDecimal.valueOf(1.5)));
        }

        return rendements;
    }

    private Map<LocalDate, Long> calculateHistoriqueRecoltes(Agriculteur agriculteur) {
        Map<LocalDate, Long> historique = new LinkedHashMap<>();
        LocalDate maintenant = LocalDate.now();

        // Simulation des 6 derniers mois
        for (int i = 5; i >= 0; i--) {
            LocalDate mois = maintenant.minusMonths(i).withDayOfMonth(1);
            historique.put(mois, (long) (Math.random() * 10)); // Données simulées
        }

        return historique;
    }

    private BigDecimal calculateTauxIrrigation(Agriculteur agriculteur) {
        Long total = parcelleRepository.countByAgriculteur(agriculteur);
        Long avecIrrigation = parcelleRepository.countByAgriculteurAndIrrigationTrue(agriculteur);

        return calculatePourcentageIrrigation(avecIrrigation, total);
    }
}