package com.agriculture.mauritanie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_utilisateur", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^\\+222[0-9]{8}$|^[0-9]{8}$", message = "Format téléphone invalide")
    @Column(unique = true, nullable = false, length = 20)
    private String telephone;

    @Column(length = 100)
    private String commune;

    @Column(length = 100)
    private String region;

    @Enumerated(EnumType.STRING)
    @Column(name = "langue_prefere")
    private LangueEnum languePreferee = LangueEnum.FR;

    @Column(name = "mot_de_passe_hash", nullable = false)
    private String motDePasseHash;

    @Enumerated(EnumType.STRING)
    private StatutEnum statut = StatutEnum.ACTIF;

    @CreationTimestamp
    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Column(name = "date_derniere_connexion")
    private LocalDateTime dateDerniereConnexion;
}