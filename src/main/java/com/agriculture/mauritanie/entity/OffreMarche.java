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
@Table(name = "offres_marche")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OffreMarche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le produit est obligatoire")
    @Column(nullable = false, length = 100)
    private String produit;

    @Column(length = 100)
    private String variete;

    @NotNull(message = "La quantité est obligatoire")
    @DecimalMin(value = "0.01", message = "La quantité doit être positive")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UniteEnum unite;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix doit être positif")
    @Column(name = "prix_unitaire", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixUnitaire;

    @Column(name = "prix_negociable")
    private Boolean prixNegociable = true;

    @Column(length = 100)
    private String commune;

    @Column(length = 100)
    private String region;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private QualiteEnum qualite = QualiteEnum.STANDARD;

    @Enumerated(EnumType.STRING)
    private CertificationEnum certification = CertificationEnum.TRADITIONNEL;

    @Column(name = "date_recolte")
    private LocalDate dateRecolte;

    @Column(name = "date_recolte_prevue")
    private LocalDate dateRecoltePrevue;

    @Column(name = "date_expiration")
    private LocalDateTime dateExpiration;

    @Enumerated(EnumType.STRING)
    private StatutOffreEnum statut = StatutOffreEnum.DISPONIBLE;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "nombre_vues")
    private Integer nombreVues = 0;

    // Relation avec Agriculteur
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendeur_id", nullable = false)
    private Agriculteur vendeur;

    @CreationTimestamp
    @Column(name = "date_publication")
    private LocalDateTime datePublication;

    @UpdateTimestamp
    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    // Méthodes utilitaires
    public BigDecimal getMontantTotal() {
        return this.quantite.multiply(this.prixUnitaire);
    }

    public boolean isExpired() {
        return dateExpiration != null && LocalDateTime.now().isAfter(dateExpiration);
    }

    public void incrementerVues() {
        this.nombreVues++;
    }
}