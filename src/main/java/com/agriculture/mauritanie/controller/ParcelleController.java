// ParcelleController.java (Version corrigée)
package com.agriculture.mauritanie.controller;

import com.agriculture.mauritanie.dto.auth.ApiResponseWrapper;
import com.agriculture.mauritanie.dto.dashboard.DashboardAgriculteurDTO;
import com.agriculture.mauritanie.dto.dashboard.StatistiquesAgriculteurDTO;
import com.agriculture.mauritanie.dto.parcelle.*;
import com.agriculture.mauritanie.entity.StatutCultureEnum;
import com.agriculture.mauritanie.service.DashboardService;
import com.agriculture.mauritanie.service.ParcelleService;
import com.agriculture.mauritanie.service.CustomUserDetailsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agriculteur")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Espace Agriculteur", description = "APIs pour la gestion des parcelles et tableau de bord agriculteur")
public class ParcelleController {

    private final ParcelleService parcelleService;
    private final DashboardService dashboardService;

    // =============================================
    // GESTION DES PARCELLES
    // =============================================

    @PostMapping("/parcelles")
    @Operation(summary = "Ajouter une parcelle",
            description = "Permet à un agriculteur d'ajouter une nouvelle parcelle à son exploitation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Parcelle créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "409", description = "Une parcelle avec ce nom existe déjà"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<ApiResponseWrapper<ParcelleDTO>> ajouterParcelle(
            @Valid @RequestBody CreateParcelleRequest request,
            Authentication authentication) {

        Long agriculteurId = getCurrentAgriculteurId(authentication);
        log.info("Création d'une parcelle pour l'agriculteur: {}", agriculteurId);

        ParcelleDTO parcelle = parcelleService.creerParcelle(agriculteurId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseWrapper.<ParcelleDTO>builder()
                        .success(true)
                        .message("Parcelle créée avec succès")
                        .data(parcelle)
                        .build());
    }

    @GetMapping("/parcelles")
    @Operation(summary = "Lister mes parcelles",
            description = "Récupère toutes les parcelles de l'agriculteur connecté")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<ApiResponseWrapper<List<ParcelleResumeDTO>>> getMesParcelles(
            Authentication authentication) {

        Long agriculteurId = getCurrentAgriculteurId(authentication);
        log.info("Récupération des parcelles pour l'agriculteur: {}", agriculteurId);

        List<ParcelleResumeDTO> parcelles = parcelleService.getMesParcelles(agriculteurId);

        return ResponseEntity.ok(ApiResponseWrapper.<List<ParcelleResumeDTO>>builder()
                .success(true)
                .message("Parcelles récupérées avec succès")
                .data(parcelles)
                .build());
    }

    @GetMapping("/parcelles/paginated")
    @Operation(summary = "Lister mes parcelles avec pagination",
            description = "Récupère les parcelles avec pagination")
    public ResponseEntity<ApiResponseWrapper<Page<ParcelleResumeDTO>>> getMesParcellesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        Long agriculteurId = getCurrentAgriculteurId(authentication);
        Page<ParcelleResumeDTO> parcelles = parcelleService.getMesParcellesPaginated(agriculteurId, page, size);

        return ResponseEntity.ok(ApiResponseWrapper.<Page<ParcelleResumeDTO>>builder()
                .success(true)
                .message("Parcelles récupérées avec succès")
                .data(parcelles)
                .build());
    }

    @GetMapping("/parcelles/{id}")
    @Operation(summary = "Détails d'une parcelle",
            description = "Récupère les détails complets d'une parcelle spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parcelle trouvée"),
            @ApiResponse(responseCode = "404", description = "Parcelle non trouvée"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<ApiResponseWrapper<ParcelleDTO>> getParcelle(
            @Parameter(description = "ID de la parcelle") @PathVariable Long id,
            Authentication authentication) {

        Long agriculteurId = getCurrentAgriculteurId(authentication);
        log.info("Récupération de la parcelle {} pour l'agriculteur: {}", id, agriculteurId);

        ParcelleDTO parcelle = parcelleService.getParcelle(agriculteurId, id);

        return ResponseEntity.ok(ApiResponseWrapper.<ParcelleDTO>builder()
                .success(true)
                .message("Parcelle récupérée avec succès")
                .data(parcelle)
                .build());
    }

    @PutMapping("/parcelles/{id}")
    @Operation(summary = "Modifier une parcelle",
            description = "Modifie les informations d'une parcelle existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parcelle modifiée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Parcelle non trouvée"),
            @ApiResponse(responseCode = "409", description = "Nom de parcelle déjà utilisé")
    })
    public ResponseEntity<ApiResponseWrapper<ParcelleDTO>> modifierParcelle(
            @Parameter(description = "ID de la parcelle") @PathVariable Long id,
            @Valid @RequestBody UpdateParcelleRequest request,
            Authentication authentication) {

        Long agriculteurId = getCurrentAgriculteurId(authentication);
        log.info("Modification de la parcelle {} pour l'agriculteur: {}", id, agriculteurId);

        ParcelleDTO parcelle = parcelleService.modifierParcelle(agriculteurId, id, request);

        return ResponseEntity.ok(ApiResponseWrapper.<ParcelleDTO>builder()
                .success(true)
                .message("Parcelle modifiée avec succès")
                .data(parcelle)
                .build());
    }

    @DeleteMapping("/parcelles/{id}")
    @Operation(summary = "Supprimer une parcelle",
            description = "Supprime définitivement une parcelle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parcelle supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Parcelle non trouvée")
    })
    public ResponseEntity<ApiResponseWrapper<String>> supprimerParcelle(
            @Parameter(description = "ID de la parcelle") @PathVariable Long id,
            Authentication authentication) {

        Long agriculteurId = getCurrentAgriculteurId(authentication);
        log.info("Suppression de la parcelle {} pour l'agriculteur: {}", id, agriculteurId);

        parcelleService.supprimerParcelle(agriculteurId, id);

        return ResponseEntity.ok(ApiResponseWrapper.<String>builder()
                .success(true)
                .message("Parcelle supprimée avec succès")
                .data("Parcelle supprimée définitivement")
                .build());
    }

    @PutMapping("/parcelles/{id}/statut")
    @Operation(summary = "Mettre à jour le statut d'une culture",
            description = "Change le statut d'une culture (planté, en croissance, récolté, etc.)")
    public ResponseEntity<ApiResponseWrapper<ParcelleDTO>> mettreAJourStatut(
            @Parameter(description = "ID de la parcelle") @PathVariable Long id,
            @Parameter(description = "Nouveau statut") @RequestParam StatutCultureEnum statut,
            Authentication authentication) {

        Long agriculteurId = getCurrentAgriculteurId(authentication);
        log.info("Mise à jour du statut de la parcelle {} vers {}", id, statut);

        ParcelleDTO parcelle = parcelleService.mettreAJourStatut(agriculteurId, id, statut);

        return ResponseEntity.ok(ApiResponseWrapper.<ParcelleDTO>builder()
                .success(true)
                .message("Statut mis à jour avec succès")
                .data(parcelle)
                .build());
    }

    // =============================================
    // RECHERCHE ET FILTRES
    // =============================================

    @GetMapping("/parcelles/recherche")
    @Operation(summary = "Rechercher des parcelles",
            description = "Recherche des parcelles par critères (type de culture, statut, région, etc.)")
    public ResponseEntity<ApiResponseWrapper<List<ParcelleResumeDTO>>> rechercherParcelles(
            @Parameter(description = "Type de culture") @RequestParam(required = false) String typeCulture,
            @Parameter(description = "Statut de la culture") @RequestParam(required = false) StatutCultureEnum statut,
            @Parameter(description = "Région") @RequestParam(required = false) String region,
            @Parameter(description = "Commune") @RequestParam(required = false) String commune,
            Authentication authentication) {

        Long agriculteurId = getCurrentAgriculteurId(authentication);
        log.info("Recherche de parcelles pour l'agriculteur: {} avec critères", agriculteurId);

        List<ParcelleResumeDTO> parcelles = parcelleService.rechercherParcelles(
                agriculteurId, typeCulture, statut, region, commune);

        return ResponseEntity.ok(ApiResponseWrapper.<List<ParcelleResumeDTO>>builder()
                .success(true)
                .message("Recherche effectuée avec succès")
                .data(parcelles)
                .build());
    }

    @GetMapping("/parcelles/recherche-texte")
    @Operation(summary = "Recherche textuelle",
            description = "Recherche dans le nom, type de culture et notes des parcelles")
    public ResponseEntity<ApiResponseWrapper<List<ParcelleResumeDTO>>> rechercherParTexte(
            @Parameter(description = "Terme de recherche") @RequestParam String q,
            Authentication authentication) {

        Long agriculteurId = getCurrentAgriculteurId(authentication);
        log.info("Recherche textuelle '{}' pour l'agriculteur: {}", q, agriculteurId);

        List<ParcelleResumeDTO> parcelles = parcelleService.rechercherParTexte(agriculteurId, q);

        return ResponseEntity.ok(ApiResponseWrapper.<List<ParcelleResumeDTO>>builder()
                .success(true)
                .message("Recherche effectuée avec succès")
                .data(parcelles)
                .build());
    }

    // =============================================
    // TABLEAU DE BORD ET STATISTIQUES
    // =============================================

    @GetMapping("/dashboard")
    @Operation(summary = "Tableau de bord agriculteur",
            description = "Récupère le tableau de bord personnalisé avec statistiques et alertes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard récupéré avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<ApiResponseWrapper<DashboardAgriculteurDTO>> getDashboard(
            Authentication authentication) {

        Long agriculteurId = getCurrentAgriculteurId(authentication);
        log.info("Génération du dashboard pour l'agriculteur: {}", agriculteurId);

        DashboardAgriculteurDTO dashboard = dashboardService.getDashboard(agriculteurId);

        return ResponseEntity.ok(ApiResponseWrapper.<DashboardAgriculteurDTO>builder()
                .success(true)
                .message("Dashboard généré avec succès")
                .data(dashboard)
                .build());
    }

    @GetMapping("/statistiques")
    @Operation(summary = "Statistiques détaillées",
            description = "Récupère les statistiques avancées de l'exploitation agricole")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques récupérées avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<ApiResponseWrapper<StatistiquesAgriculteurDTO>> getStatistiques(
            Authentication authentication) {

        Long agriculteurId = getCurrentAgriculteurId(authentication);
        log.info("Génération des statistiques pour l'agriculteur: {}", agriculteurId);

        StatistiquesAgriculteurDTO statistiques = dashboardService.getStatistiques(agriculteurId);

        return ResponseEntity.ok(ApiResponseWrapper.<StatistiquesAgriculteurDTO>builder()
                .success(true)
                .message("Statistiques générées avec succès")
                .data(statistiques)
                .build());
    }

    // =============================================
    // ENDPOINTS UTILITAIRES
    // =============================================

    @GetMapping("/parcelles/types-culture")
    @Operation(summary = "Types de culture disponibles",
            description = "Récupère la liste des types de culture utilisés par l'agriculteur")
    public ResponseEntity<ApiResponseWrapper<List<String>>> getTypesCulture(Authentication authentication) {
        Long agriculteurId = getCurrentAgriculteurId(authentication);

        // Pour l'instant, retourner une liste statique
        List<String> typesCulture = List.of(
                "Maraîchage", "Céréales", "Légumineuses", "Arboriculture",
                "Élevage", "Cultures fourragères", "Cultures industrielles"
        );

        return ResponseEntity.ok(ApiResponseWrapper.<List<String>>builder()
                .success(true)
                .message("Types de culture récupérés")
                .data(typesCulture)
                .build());
    }

    @GetMapping("/parcelles/statuts")
    @Operation(summary = "Statuts de culture disponibles",
            description = "Récupère tous les statuts possibles pour une culture")
    public ResponseEntity<ApiResponseWrapper<List<StatutCultureInfo>>> getStatutsCulture() {

        List<StatutCultureInfo> statuts = Arrays.stream(StatutCultureEnum.values())
                .map(statut -> new StatutCultureInfo(statut.name(), statut.getLibelle()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponseWrapper.<List<StatutCultureInfo>>builder()
                .success(true)
                .message("Statuts de culture récupérés")
                .data(statuts)
                .build());
    }

    // =============================================
    // MÉTHODES UTILITAIRES
    // =============================================

    private Long getCurrentAgriculteurId(Authentication authentication) {
        CustomUserDetailsService.CustomUserPrincipal userPrincipal =
                (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        return userPrincipal.getUserId();
    }

    // Classe interne pour les informations de statut
    public static class StatutCultureInfo {
        public final String code;
        public final String libelle;

        public StatutCultureInfo(String code, String libelle) {
            this.code = code;
            this.libelle = libelle;
        }
    }
}