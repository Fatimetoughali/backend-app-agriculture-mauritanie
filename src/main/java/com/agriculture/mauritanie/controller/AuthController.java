package com.agriculture.mauritanie.controller;

import com.agriculture.mauritanie.dto.auth.*;
import com.agriculture.mauritanie.service.AuthService;
import com.agriculture.mauritanie.security.JwtTokenProvider;
import com.agriculture.mauritanie.service.CustomUserDetailsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentification", description = "APIs pour l'authentification et l'inscription des utilisateurs")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    @Operation(summary = "Connexion utilisateur", description = "Authentifie un utilisateur avec son téléphone et mot de passe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connexion réussie"),
            @ApiResponse(responseCode = "401", description = "Identifiants invalides"),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides")
    })
    public ResponseEntity<ApiResponseWrapper<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Tentative de connexion pour: {}", request.getTelephone());

        AuthResponse authResponse = authService.login(request);

        return ResponseEntity.ok(ApiResponseWrapper.<AuthResponse>builder()
                .success(true)
                .message("Connexion réussie")
                .data(authResponse)
                .build());
    }

    @PostMapping("/register/agriculteur")
    @Operation(summary = "Inscription agriculteur", description = "Crée un nouveau compte agriculteur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inscription réussie"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "409", description = "Téléphone déjà utilisé")
    })
    public ResponseEntity<ApiResponseWrapper<AuthResponse>> registerAgriculteur(@Valid @RequestBody RegisterAgriculteurRequest request) {
        log.info("Inscription agriculteur: {}", request.getTelephone());

        AuthResponse authResponse = authService.registerAgriculteur(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseWrapper.<AuthResponse>builder()
                        .success(true)
                        .message("Inscription agriculteur réussie")
                        .data(authResponse)
                        .build());
    }

    @PostMapping("/register/acheteur")
    @Operation(summary = "Inscription acheteur", description = "Crée un nouveau compte acheteur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inscription réussie"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "409", description = "Téléphone déjà utilisé")
    })
    public ResponseEntity<ApiResponseWrapper<AuthResponse>> registerAcheteur(@Valid @RequestBody RegisterAcheteurRequest request) {
        log.info("Inscription acheteur: {}", request.getTelephone());

        AuthResponse authResponse = authService.registerAcheteur(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseWrapper.<AuthResponse>builder()
                        .success(true)
                        .message("Inscription acheteur réussie")
                        .data(authResponse)
                        .build());
    }

    @PostMapping("/register/fournisseur")
    @Operation(summary = "Inscription fournisseur", description = "Crée un nouveau compte fournisseur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inscription réussie"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "409", description = "Téléphone déjà utilisé")
    })
    public ResponseEntity<ApiResponseWrapper<AuthResponse>> registerFournisseur(@Valid @RequestBody RegisterFournisseurRequest request) {
        log.info("Inscription fournisseur: {}", request.getTelephone());

        AuthResponse authResponse = authService.registerFournisseur(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseWrapper.<AuthResponse>builder()
                        .success(true)
                        .message("Inscription fournisseur réussie")
                        .data(authResponse)
                        .build());
    }

    @PostMapping("/refresh")
    @Operation(summary = "Rafraîchir le token", description = "Génère un nouveau token à partir d'un refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token rafraîchi avec succès"),
            @ApiResponse(responseCode = "401", description = "Refresh token invalide")
    })
    public ResponseEntity<ApiResponseWrapper<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        log.info("Demande de rafraîchissement de token");

        AuthResponse authResponse = authService.refreshToken(request.getRefreshToken());

        return ResponseEntity.ok(ApiResponseWrapper.<AuthResponse>builder()
                .success(true)
                .message("Token rafraîchi avec succès")
                .data(authResponse)
                .build());
    }

    @GetMapping("/profile")
    @Operation(summary = "Profil utilisateur", description = "Récupère le profil de l'utilisateur connecté")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profil récupéré avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    public ResponseEntity<ApiResponseWrapper<UserProfileResponse>> getUserProfile(Authentication authentication) {
        CustomUserDetailsService.CustomUserPrincipal userPrincipal =
                (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();

        Long userId = userPrincipal.getUserId();
        UserProfileResponse profile = authService.getUserProfile(userId);

        return ResponseEntity.ok(ApiResponseWrapper.<UserProfileResponse>builder()
                .success(true)
                .message("Profil récupéré avec succès")
                .data(profile)
                .build());
    }

    @PostMapping("/logout")
    @Operation(summary = "Déconnexion", description = "Déconnecte l'utilisateur (côté client)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Déconnexion réussie")
    })
    public ResponseEntity<ApiResponseWrapper<String>> logout() {
        return ResponseEntity.ok(ApiResponseWrapper.<String>builder()
                .success(true)
                .message("Déconnexion réussie")
                .data("Vous avez été déconnecté avec succès")
                .build());
    }

    @GetMapping("/validate-token")
    @Operation(summary = "Valider le token", description = "Vérifie si le token JWT est valide")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token valide"),
            @ApiResponse(responseCode = "401", description = "Token invalide")
    })
    public ResponseEntity<ApiResponseWrapper<TokenValidationResponse>> validateToken(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        boolean isValid = jwtTokenProvider.validateToken(token);

        if (isValid) {
            String username = jwtTokenProvider.getUsernameFromToken(token);
            Long userId = jwtTokenProvider.getUserIdFromToken(token);

            TokenValidationResponse response = TokenValidationResponse.builder()
                    .valid(true)
                    .username(username)
                    .userId(userId)
                    .expiresAt(jwtTokenProvider.getExpirationDateFromToken(token))
                    .build();

            return ResponseEntity.ok(ApiResponseWrapper.<TokenValidationResponse>builder()
                    .success(true)
                    .message("Token valide")
                    .data(response)
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseWrapper.<TokenValidationResponse>builder()
                            .success(false)
                            .message("Token invalide")
                            .data(TokenValidationResponse.builder().valid(false).build())
                            .build());
        }
    }
}