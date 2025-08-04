// ParcelleService.java
package com.agriculture.mauritanie.service;

import com.agriculture.mauritanie.dto.parcelle.*;
import com.agriculture.mauritanie.entity.Parcelle;
import com.agriculture.mauritanie.entity.Agriculteur;
import com.agriculture.mauritanie.entity.StatutCultureEnum;
import com.agriculture.mauritanie.repository.ParcelleRepository;
import com.agriculture.mauritanie.repository.UserRepository;
import com.agriculture.mauritanie.exception.ResourceNotFoundException;
import com.agriculture.mauritanie.exception.DuplicateResourceException;
import com.agriculture.mauritanie.exception.ValidationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParcelleService {

    private final ParcelleRepository parcelleRepository;
    private final UserRepository userRepository;

    /**
     * Créer une nouvelle parcelle
     */
    @Transactional
    public ParcelleDTO creerParcelle(Long agriculteurId, CreateParcelleRequest request) {
        log.info("Création d'une nouvelle parcelle pour l'agriculteur: {}", agriculteurId);

        Agriculteur agriculteur = getAgriculteur(agriculteurId);

        // Vérifier l'unicité du nom de parcelle pour cet agriculteur
        if (parcelleRepository.existsByAgriculteurAndNomParcelleIgnoreCase(agriculteur, request.getNomParcelle())) {
            throw new DuplicateResourceException("Une parcelle avec ce nom existe déjà");
        }

        // Validation des dates
        validateDates(request.getDatePlantation(), request.getDateRecoltePrevue());

        Parcelle parcelle = new Parcelle();
        mapToEntity(parcelle, request);
        parcelle.setAgriculteur(agriculteur);

        Parcelle savedParcelle = parcelleRepository.save(parcelle);
        log.info("Parcelle créée avec succès: {}", savedParcelle.getId());

        return mapToDTO(savedParcelle);
    }

    /**
     * Obtenir toutes les parcelles d'un agriculteur
     */
    public List<ParcelleResumeDTO> getMesParcelles(Long agriculteurId) {
        log.info("Récupération des parcelles pour l'agriculteur: {}", agriculteurId);

        Agriculteur agriculteur = getAgriculteur(agriculteurId);
        List<Parcelle> parcelles = parcelleRepository.findByAgriculteurOrderByDateCreationDesc(agriculteur);

        return parcelles.stream()
                .map(this::mapToResumeDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtenir les parcelles avec pagination
     */
    public Page<ParcelleResumeDTO> getMesParcellesPaginated(Long agriculteurId, int page, int size) {
        Agriculteur agriculteur = getAgriculteur(agriculteurId);
        Pageable pageable = PageRequest.of(page, size);

        return parcelleRepository.findByAgriculteur(agriculteur, pageable)
                .map(this::mapToResumeDTO);
    }

    /**
     * Obtenir une parcelle par ID
     */
    public ParcelleDTO getParcelle(Long agriculteurId, Long parcelleId) {
        log.info("Récupération de la parcelle {} pour l'agriculteur: {}", parcelleId, agriculteurId);

        Agriculteur agriculteur = getAgriculteur(agriculteurId);
        Parcelle parcelle = parcelleRepository.findByIdAndAgriculteur(parcelleId, agriculteur)
                .orElseThrow(() -> new ResourceNotFoundException("Parcelle non trouvée"));

        return mapToDTO(parcelle);
    }

    /**
     * Modifier une parcelle
     */
    @Transactional
    public ParcelleDTO modifierParcelle(Long agriculteurId, Long parcelleId, UpdateParcelleRequest request) {
        log.info("Modification de la parcelle {} pour l'agriculteur: {}", parcelleId, agriculteurId);

        Agriculteur agriculteur = getAgriculteur(agriculteurId);
        Parcelle parcelle = parcelleRepository.findByIdAndAgriculteur(parcelleId, agriculteur)
                .orElseThrow(() -> new ResourceNotFoundException("Parcelle non trouvée"));

        // Vérifier l'unicité du nom si il a été modifié
        if (!parcelle.getNomParcelle().equalsIgnoreCase(request.getNomParcelle()) &&
                parcelleRepository.existsByAgriculteurAndNomParcelleIgnoreCaseAndIdNot(
                        agriculteur, request.getNomParcelle(), parcelleId)) {
            throw new DuplicateResourceException("Une parcelle avec ce nom existe déjà");
        }

        // Validation des dates
        validateDates(request.getDatePlantation(), request.getDateRecoltePrevue());

        mapToEntity(parcelle, request);
        Parcelle savedParcelle = parcelleRepository.save(parcelle);

        log.info("Parcelle modifiée avec succès: {}", savedParcelle.getId());
        return mapToDTO(savedParcelle);
    }

    /**
     * Supprimer une parcelle
     */
    @Transactional
    public void supprimerParcelle(Long agriculteurId, Long parcelleId) {
        log.info("Suppression de la parcelle {} pour l'agriculteur: {}", parcelleId, agriculteurId);

        Agriculteur agriculteur = getAgriculteur(agriculteurId);
        Parcelle parcelle = parcelleRepository.findByIdAndAgriculteur(parcelleId, agriculteur)
                .orElseThrow(() -> new ResourceNotFoundException("Parcelle non trouvée"));

        parcelleRepository.delete(parcelle);
        log.info("Parcelle supprimée avec succès: {}", parcelleId);
    }

    /**
     * Rechercher des parcelles par critères
     */
    public List<ParcelleResumeDTO> rechercherParcelles(Long agriculteurId, String typeCulture,
                                                       StatutCultureEnum statut, String region, String commune) {
        Agriculteur agriculteur = getAgriculteur(agriculteurId);
        List<Parcelle> parcelles;

        if (typeCulture != null && !typeCulture.isEmpty()) {
            parcelles = parcelleRepository.findByAgriculteurAndTypeCulture(agriculteur, typeCulture);
        } else if (statut != null) {
            parcelles = parcelleRepository.findByAgriculteurAndStatutCulture(agriculteur, statut);
        } else if (region != null && !region.isEmpty()) {
            parcelles = parcelleRepository.findByAgriculteurAndRegion(agriculteur, region);
        } else if (commune != null && !commune.isEmpty()) {
            parcelles = parcelleRepository.findByAgriculteurAndCommune(agriculteur, commune);
        } else {
            parcelles = parcelleRepository.findByAgriculteurOrderByDateCreationDesc(agriculteur);
        }

        return parcelles.stream()
                .map(this::mapToResumeDTO)
                .collect(Collectors.toList());
    }

    /**
     * Recherche textuelle
     */
    public List<ParcelleResumeDTO> rechercherParTexte(Long agriculteurId, String searchTerm) {
        Agriculteur agriculteur = getAgriculteur(agriculteurId);
        List<Parcelle> parcelles = parcelleRepository.searchByAgriculteurAndTerm(agriculteur, searchTerm);

        return parcelles.stream()
                .map(this::mapToResumeDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mettre à jour le statut d'une culture
     */
    @Transactional
    public ParcelleDTO mettreAJourStatut(Long agriculteurId, Long parcelleId, StatutCultureEnum nouveauStatut) {
        log.info("Mise à jour du statut de la parcelle {} vers {}", parcelleId, nouveauStatut);

        Agriculteur agriculteur = getAgriculteur(agriculteurId);
        Parcelle parcelle = parcelleRepository.findByIdAndAgriculteur(parcelleId, agriculteur)
                .orElseThrow(() -> new ResourceNotFoundException("Parcelle non trouvée"));

        parcelle.setStatutCulture(nouveauStatut);
        Parcelle savedParcelle = parcelleRepository.save(parcelle);

        return mapToDTO(savedParcelle);
    }

    // Méthodes utilitaires privées

    private Agriculteur getAgriculteur(Long agriculteurId) {
        return (Agriculteur) userRepository.findById(agriculteurId)
                .filter(user -> user instanceof Agriculteur)
                .orElseThrow(() -> new ResourceNotFoundException("Agriculteur non trouvé"));
    }

    private void validateDates(LocalDate datePlantation, LocalDate dateRecoltePrevue) {
        if (datePlantation != null && dateRecoltePrevue != null) {
            if (dateRecoltePrevue.isBefore(datePlantation)) {
                throw new ValidationException("La date de récolte prévue ne peut pas être antérieure à la date de plantation",
                        Map.of("dateRecoltePrevue", "Date de récolte invalide"));
            }
        }
    }

    private void mapToEntity(Parcelle parcelle, CreateParcelleRequest request) {
        parcelle.setNomParcelle(request.getNomParcelle());
        parcelle.setSurfaceHectares(request.getSurfaceHectares());
        parcelle.setTypeCulture(request.getTypeCulture());
        parcelle.setCommune(request.getCommune());
        parcelle.setRegion(request.getRegion());
        parcelle.setLatitude(request.getLatitude());
        parcelle.setLongitude(request.getLongitude());
        parcelle.setDatePlantation(request.getDatePlantation());
        parcelle.setDateRecoltePrevue(request.getDateRecoltePrevue());
        parcelle.setStatutCulture(request.getStatutCulture());
        parcelle.setIrrigation(request.getIrrigation());
        parcelle.setNotes(request.getNotes());
    }

    private void mapToEntity(Parcelle parcelle, UpdateParcelleRequest request) {
        parcelle.setNomParcelle(request.getNomParcelle());
        parcelle.setSurfaceHectares(request.getSurfaceHectares());
        parcelle.setTypeCulture(request.getTypeCulture());
        parcelle.setCommune(request.getCommune());
        parcelle.setRegion(request.getRegion());
        parcelle.setLatitude(request.getLatitude());
        parcelle.setLongitude(request.getLongitude());
        parcelle.setDatePlantation(request.getDatePlantation());
        parcelle.setDateRecoltePrevue(request.getDateRecoltePrevue());
        if (request.getStatutCulture() != null) {
            parcelle.setStatutCulture(request.getStatutCulture());
        }
        if (request.getIrrigation() != null) {
            parcelle.setIrrigation(request.getIrrigation());
        }
        parcelle.setNotes(request.getNotes());
    }

    private ParcelleDTO mapToDTO(Parcelle parcelle) {
        return ParcelleDTO.builder()
                .id(parcelle.getId())
                .nomParcelle(parcelle.getNomParcelle())
                .surfaceHectares(parcelle.getSurfaceHectares())
                .typeCulture(parcelle.getTypeCulture())
                .commune(parcelle.getCommune())
                .region(parcelle.getRegion())
                .latitude(parcelle.getLatitude())
                .longitude(parcelle.getLongitude())
                .datePlantation(parcelle.getDatePlantation())
                .dateRecoltePrevue(parcelle.getDateRecoltePrevue())
                .statutCulture(parcelle.getStatutCulture())
                .statutCultureLibelle(parcelle.getStatutCulture().getLibelle())
                .irrigation(parcelle.getIrrigation())
                .notes(parcelle.getNotes())
                .dateCreation(parcelle.getDateCreation())
                .dateModification(parcelle.getDateModification())
                .joursAvantRecolte(calculateJoursAvantRecolte(parcelle.getDateRecoltePrevue()))
                .joursDeplantation(calculateJoursDepuisPlantation(parcelle.getDatePlantation()))
                .phaseActuelle(calculatePhaseActuelle(parcelle))
                .build();
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
        long jours = ChronoUnit.DAYS.between(today, dateRecoltePrevue);
        return (int) jours;
    }

    private Integer calculateJoursDepuisPlantation(LocalDate datePlantation) {
        if (datePlantation == null) return null;

        LocalDate today = LocalDate.now();
        long jours = ChronoUnit.DAYS.between(datePlantation, today);
        return Math.max(0, (int) jours);
    }

    private String calculatePhaseActuelle(Parcelle parcelle) {
        if (parcelle.getDatePlantation() == null) {
            return "Non planifée";
        }

        LocalDate today = LocalDate.now();
        long joursDepuisPlantation = ChronoUnit.DAYS.between(parcelle.getDatePlantation(), today);

        if (joursDepuisPlantation < 0) {
            return "Plantation prévue";
        } else if (joursDepuisPlantation <= 7) {
            return "Plantation récente";
        } else if (joursDepuisPlantation <= 30) {
            return "Croissance précoce";
        } else if (joursDepuisPlantation <= 90) {
            return "Développement";
        } else {
            return "Maturation";
        }
    }
}