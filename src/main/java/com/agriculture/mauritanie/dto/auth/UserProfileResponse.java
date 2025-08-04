package com.agriculture.mauritanie.dto.auth;

import com.agriculture.mauritanie.entity.LangueEnum;
import com.agriculture.mauritanie.entity.StatutEnum;
import com.agriculture.mauritanie.entity.TypeUtilisateurEnum;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
public class UserProfileResponse {
    private Long id;
    private String nom;
    private String telephone;
    private String commune;
    private String region;
    private LangueEnum languePreferee;
    private StatutEnum statut;
    private TypeUtilisateurEnum typeUtilisateur;
    private LocalDateTime dateCreation;
    private LocalDateTime dateDerniereConnexion;

    // Champs spécifiques selon le type
    private Object specificData; // Contiendra les données spécifiques (agriculteur, acheteur, etc.)
}