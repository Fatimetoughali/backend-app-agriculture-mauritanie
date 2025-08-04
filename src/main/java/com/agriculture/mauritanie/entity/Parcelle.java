package com.agriculture.mauritanie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "parcelles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parcelle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de la parcelle est obligatoire")
    @Column(name = "nom_parcelle", nullable = false, length = 100)
    private String nomParcelle;

    @NotNull(message = "La surface est obligatoire")
    @DecimalMin(value = "0.01", message = "La surface doit être positive")
    @Column(name = "surface_hectares", nullable = false, precision = 8, scale = 2)
    private BigDecimal surfaceHectares;

    @NotBlank(message = "Le type de culture est obligatoire")
    @Column(name = "type_culture", nullable = false, length = 100)
    private String typeCulture;

    @Column(length = 100)
    private String commune;

    @Column(length = 100)
    private String region;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "date_plantation")
    private LocalDate datePlantation;

    @Column(name = "date_recolte_prevue")
    private LocalDate dateRecoltePrevue;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_culture")
    private StatutCultureEnum statutCulture = StatutCultureEnum.PLANTE;

    @Column(name = "irrigation")
    private Boolean irrigation = false;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agriculteur_id", nullable = false)
    private Agriculteur agriculteur;

    @CreationTimestamp
    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    @Column(name = "date_modification")
    private LocalDateTime dateModification;
}
