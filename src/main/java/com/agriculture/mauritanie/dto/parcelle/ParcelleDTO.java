package com.agriculture.mauritanie.dto.parcelle;

import com.agriculture.mauritanie.entity.StatutCultureEnum;
import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ParcelleDTO {
    private Long id;
    private String nomParcelle;
    private BigDecimal surfaceHectares;
    private String typeCulture;
    private String commune;
    private String region;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDate datePlantation;
    private LocalDate dateRecoltePrevue;
    private StatutCultureEnum statutCulture;
    private String statutCultureLibelle; // Pour l'affichage
    private Boolean irrigation;
    private String notes;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

    // Champs calculés
    private Integer joursAvantRecolte; // Nombre de jours avant la récolte prévue
    private Integer joursDeplantation; // Nombre de jours depuis la plantation
    private String phaseActuelle; // Phase actuelle basée sur les dates
}