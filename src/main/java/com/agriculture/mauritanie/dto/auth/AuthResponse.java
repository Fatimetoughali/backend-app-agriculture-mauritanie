package com.agriculture.mauritanie.dto.auth;

import com.agriculture.mauritanie.entity.TypeUtilisateurEnum;
import com.agriculture.mauritanie.entity.LangueEnum;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String refreshToken;
    private Long userId;
    private String nom;
    private String telephone;
    private TypeUtilisateurEnum typeUtilisateur;
    private LangueEnum languePreferee;
    private String commune;
    private String region;
    private LocalDateTime dateExpiration;
    private String message;
}
