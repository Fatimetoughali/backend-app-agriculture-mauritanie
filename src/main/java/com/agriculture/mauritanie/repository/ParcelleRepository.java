// ParcelleRepository.java
package com.agriculture.mauritanie.repository;

import com.agriculture.mauritanie.entity.Parcelle;
import com.agriculture.mauritanie.entity.Agriculteur;
import com.agriculture.mauritanie.entity.StatutCultureEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParcelleRepository extends JpaRepository<Parcelle, Long> {

    // Recherche par agriculteur
    List<Parcelle> findByAgriculteurOrderByDateCreationDesc(Agriculteur agriculteur);

    Page<Parcelle> findByAgriculteur(Agriculteur agriculteur, Pageable pageable);

    // Vérifier qu'une parcelle appartient bien à un agriculteur
    Optional<Parcelle> findByIdAndAgriculteur(Long id, Agriculteur agriculteur);

    // Recherche par critères
    List<Parcelle> findByAgriculteurAndTypeCulture(Agriculteur agriculteur, String typeCulture);

    List<Parcelle> findByAgriculteurAndStatutCulture(Agriculteur agriculteur, StatutCultureEnum statutCulture);

    List<Parcelle> findByAgriculteurAndRegion(Agriculteur agriculteur, String region);

    List<Parcelle> findByAgriculteurAndCommune(Agriculteur agriculteur, String commune);

    // Statistiques pour le dashboard
    @Query("SELECT COUNT(p) FROM Parcelle p WHERE p.agriculteur = :agriculteur")
    Long countByAgriculteur(@Param("agriculteur") Agriculteur agriculteur);

    @Query("SELECT SUM(p.surfaceHectares) FROM Parcelle p WHERE p.agriculteur = :agriculteur")
    BigDecimal sumSurfaceByAgriculteur(@Param("agriculteur") Agriculteur agriculteur);

    @Query("SELECT COUNT(p) FROM Parcelle p WHERE p.agriculteur = :agriculteur AND p.statutCulture = :statut")
    Long countByAgriculteurAndStatut(@Param("agriculteur") Agriculteur agriculteur, @Param("statut") StatutCultureEnum statut);

    // Statistiques par type de culture
    @Query("SELECT p.typeCulture, COUNT(p) FROM Parcelle p WHERE p.agriculteur = :agriculteur GROUP BY p.typeCulture")
    List<Object[]> countByAgriculteurGroupByTypeCulture(@Param("agriculteur") Agriculteur agriculteur);

    @Query("SELECT p.typeCulture, SUM(p.surfaceHectares) FROM Parcelle p WHERE p.agriculteur = :agriculteur GROUP BY p.typeCulture")
    List<Object[]> sumSurfaceByAgriculteurGroupByTypeCulture(@Param("agriculteur") Agriculteur agriculteur);

    // Statistiques par statut
    @Query("SELECT p.statutCulture, COUNT(p) FROM Parcelle p WHERE p.agriculteur = :agriculteur GROUP BY p.statutCulture")
    List<Object[]> countByAgriculteurGroupByStatut(@Param("agriculteur") Agriculteur agriculteur);

    // Statistiques par région/commune
    @Query("SELECT p.region, COUNT(p) FROM Parcelle p WHERE p.agriculteur = :agriculteur AND p.region IS NOT NULL GROUP BY p.region")
    List<Object[]> countByAgriculteurGroupByRegion(@Param("agriculteur") Agriculteur agriculteur);

    @Query("SELECT p.commune, COUNT(p) FROM Parcelle p WHERE p.agriculteur = :agriculteur AND p.commune IS NOT NULL GROUP BY p.commune")
    List<Object[]> countByAgriculteurGroupByCommune(@Param("agriculteur") Agriculteur agriculteur);

    // Surfaces par région/commune (requêtes manquantes)
    @Query("SELECT p.region, SUM(p.surfaceHectares) FROM Parcelle p WHERE p.agriculteur = :agriculteur AND p.region IS NOT NULL GROUP BY p.region")
    List<Object[]> sumSurfaceByAgriculteurGroupByRegion(@Param("agriculteur") Agriculteur agriculteur);

    @Query("SELECT p.commune, SUM(p.surfaceHectares) FROM Parcelle p WHERE p.agriculteur = :agriculteur AND p.commune IS NOT NULL GROUP BY p.commune")
    List<Object[]> sumSurfaceByAgriculteurGroupByCommune(@Param("agriculteur") Agriculteur agriculteur);

    // Prochaines récoltes (dans les N prochains jours)
    @Query("SELECT p FROM Parcelle p WHERE p.agriculteur = :agriculteur " +
            "AND p.dateRecoltePrevue BETWEEN :dateDebut AND :dateFin " +
            "ORDER BY p.dateRecoltePrevue ASC")
    List<Parcelle> findProchainesRecoltesByAgriculteur(
            @Param("agriculteur") Agriculteur agriculteur,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);

    // Cultures en retard (date de récolte dépassée)
    @Query("SELECT p FROM Parcelle p WHERE p.agriculteur = :agriculteur " +
            "AND p.dateRecoltePrevue < :dateActuelle " +
            "AND p.statutCulture NOT IN :statutsTermines " +
            "ORDER BY p.dateRecoltePrevue ASC")
    List<Parcelle> findCulturesEnRetardByAgriculteur(
            @Param("agriculteur") Agriculteur agriculteur,
            @Param("dateActuelle") LocalDate dateActuelle,
            @Param("statutsTermines") List<StatutCultureEnum> statutsTermines);

    // Parcelles modifiées récemment
    @Query("SELECT p FROM Parcelle p WHERE p.agriculteur = :agriculteur " +
            "ORDER BY p.dateModification DESC")
    List<Parcelle> findRecentlyModifiedByAgriculteur(@Param("agriculteur") Agriculteur agriculteur, Pageable pageable);

    // Statistiques d'irrigation
    @Query("SELECT COUNT(p) FROM Parcelle p WHERE p.agriculteur = :agriculteur AND p.irrigation = true")
    Long countByAgriculteurAndIrrigationTrue(@Param("agriculteur") Agriculteur agriculteur);

    @Query("SELECT COUNT(p) FROM Parcelle p WHERE p.agriculteur = :agriculteur AND p.irrigation = false")
    Long countByAgriculteurAndIrrigationFalse(@Param("agriculteur") Agriculteur agriculteur);

    // Recherche par dates
    List<Parcelle> findByAgriculteurAndDatePlantationBetween(
            Agriculteur agriculteur, LocalDate dateDebut, LocalDate dateFin);

    List<Parcelle> findByAgriculteurAndDateRecoltePrevueBetween(
            Agriculteur agriculteur, LocalDate dateDebut, LocalDate dateFin);

    // Recherche textuelle
    @Query("SELECT p FROM Parcelle p WHERE p.agriculteur = :agriculteur " +
            "AND (LOWER(p.nomParcelle) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(p.typeCulture) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(p.notes) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Parcelle> searchByAgriculteurAndTerm(
            @Param("agriculteur") Agriculteur agriculteur,
            @Param("searchTerm") String searchTerm);

    // Vérification d'unicité du nom pour un agriculteur
    boolean existsByAgriculteurAndNomParcelleIgnoreCase(Agriculteur agriculteur, String nomParcelle);

    boolean existsByAgriculteurAndNomParcelleIgnoreCaseAndIdNot(
            Agriculteur agriculteur, String nomParcelle, Long id);
}