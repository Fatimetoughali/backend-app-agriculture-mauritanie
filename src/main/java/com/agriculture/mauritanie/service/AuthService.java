// AuthService.java (Version mise à jour avec tous les champs)
package com.agriculture.mauritanie.service;

import com.agriculture.mauritanie.dto.auth.*;
import com.agriculture.mauritanie.entity.*;
import com.agriculture.mauritanie.repository.UserRepository;
import com.agriculture.mauritanie.security.JwtTokenProvider;
import com.agriculture.mauritanie.exception.AuthenticationException;
import com.agriculture.mauritanie.exception.ResourceNotFoundException;
import com.agriculture.mauritanie.exception.DuplicateResourceException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Authentification d'un utilisateur
     */
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Tentative de connexion pour le téléphone: {}", request.getTelephone());

        User user = userRepository.findByTelephoneAndStatut(request.getTelephone(), StatutEnum.ACTIF)
                .orElseThrow(() -> new AuthenticationException("Identifiants invalides"));

        if (!passwordEncoder.matches(request.getMotDePasse(), user.getMotDePasseHash())) {
            log.warn("Tentative de connexion échouée pour: {}", request.getTelephone());
            throw new AuthenticationException("Identifiants invalides");
        }

        // Mise à jour de la dernière connexion
        user.setDateDerniereConnexion(LocalDateTime.now());
        userRepository.save(user);

        return generateAuthResponse(user, "Connexion réussie");
    }

    /**
     * Inscription d'un agriculteur (avec tous les champs)
     */
    @Transactional
    public AuthResponse registerAgriculteur(RegisterAgriculteurRequest request) {
        log.info("Inscription d'un agriculteur: {}", request.getTelephone());

        validateUniquePhone(request.getTelephone());

        Agriculteur agriculteur = new Agriculteur();
        mapCommonFields(agriculteur, request.getNom(), request.getTelephone(),
                request.getMotDePasse(), request.getCommune(), request.getRegion(),
                request.getLanguePreferee());

        agriculteur.setTypeCulture(request.getTypeCulture());
        agriculteur.setSurfaceTotale(request.getSurfaceTotale());
        agriculteur.setAnneesExperience(request.getAnneesExperience());
        agriculteur.setEstCooperative(request.getEstCooperative());
        agriculteur.setCooperativeNom(request.getCooperativeNom());
        agriculteur.setCertificationBio(request.getCertificationBio());
        agriculteur.setMethodeIrrigation(request.getMethodeIrrigation());
        agriculteur.setEquipements(request.getEquipements());
        agriculteur.setProductionAnnuelle(request.getProductionAnnuelle());
        agriculteur.setObjectifProduction(request.getObjectifProduction());
        agriculteur.setFormationAgricole(request.getFormationAgricole());
        agriculteur.setAccesCredit(request.getAccesCredit());
        agriculteur.setProblemesPrincipaux(request.getProblemesPrincipaux());

        User savedUser = userRepository.save(agriculteur);

        return generateAuthResponse(savedUser, "Inscription agriculteur réussie");
    }

    /**
     * Inscription d'un acheteur (avec tous les champs)
     */
    @Transactional
    public AuthResponse registerAcheteur(RegisterAcheteurRequest request) {
        log.info("Inscription d'un acheteur: {}", request.getTelephone());

        validateUniquePhone(request.getTelephone());

        Acheteur acheteur = new Acheteur();
        mapCommonFields(acheteur, request.getNom(), request.getTelephone(),
                request.getMotDePasse(), request.getCommune(), request.getRegion(),
                request.getLanguePreferee());

        // Mapping complet des champs acheteur
        acheteur.setEntreprise(request.getEntreprise());
        acheteur.setTypeAchat(request.getTypeAchat());
        acheteur.setProduitsRecherches(request.getProduitsRecherches());
        acheteur.setCapaciteStockage(request.getCapaciteStockage());
        acheteur.setZoneAchat(request.getZoneAchat());
        acheteur.setFrequenceAchat(request.getFrequenceAchat());
        acheteur.setModePaiement(request.getModePaiement());
        acheteur.setDelaiPaiement(request.getDelaiPaiement());
        acheteur.setExigencesQualite(request.getExigencesQualite());
        acheteur.setCertificationRequise(request.getCertificationRequise());
        acheteur.setVolumeMensuel(request.getVolumeMensuel());
        acheteur.setPrixMaximum(request.getPrixMaximum());
        acheteur.setTransportAssure(request.getTransportAssure());
        acheteur.setNumeroLicense(request.getNumeroLicense());
        acheteur.setSecteurActivite(request.getSecteurActivite());

        User savedUser = userRepository.save(acheteur);

        return generateAuthResponse(savedUser, "Inscription acheteur réussie");
    }

    /**
     * Inscription d'un fournisseur (avec tous les champs)
     */
    @Transactional
    public AuthResponse registerFournisseur(RegisterFournisseurRequest request) {
        log.info("Inscription d'un fournisseur: {}", request.getTelephone());

        validateUniquePhone(request.getTelephone());

        Fournisseur fournisseur = new Fournisseur();
        mapCommonFields(fournisseur, request.getNom(), request.getTelephone(),
                request.getMotDePasse(), request.getCommune(), request.getRegion(),
                request.getLanguePreferee());

        // Mapping complet des champs fournisseur
        fournisseur.setEntreprise(request.getEntreprise());
        fournisseur.setTypeProduits(request.getTypeProduits());
        fournisseur.setMarquesRepresentees(request.getMarquesRepresentees());
        fournisseur.setZoneLivraison(request.getZoneLivraison());
        fournisseur.setLicenceNumero(request.getLicenceNumero());
        fournisseur.setAgrementMinistere(request.getAgrementMinistere());
        fournisseur.setServiceConseil(request.getServiceConseil());
        fournisseur.setServiceApresVente(request.getServiceApresVente());
        fournisseur.setFormationTechnique(request.getFormationTechnique());
        fournisseur.setCreditClient(request.getCreditClient());
        fournisseur.setDelaiLivraison(request.getDelaiLivraison());
        fournisseur.setGarantieProduits(request.getGarantieProduits());
        fournisseur.setStockDisponible(request.getStockDisponible());
        fournisseur.setPrixCatalogue(request.getPrixCatalogue());
        fournisseur.setConditionsPaiement(request.getConditionsPaiement());
        fournisseur.setCertifications(request.getCertifications());
        fournisseur.setSiteWeb(request.getSiteWeb());
        fournisseur.setAdressePhysique(request.getAdressePhysique());
        fournisseur.setHorairesOuverture(request.getHorairesOuverture());

        User savedUser = userRepository.save(fournisseur);

        return generateAuthResponse(savedUser, "Inscription fournisseur réussie");
    }

    /**
     * Rafraîchir le token
     */
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new AuthenticationException("Token de rafraîchissement invalide");
        }

        String telephone = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByTelephoneAndStatut(telephone, StatutEnum.ACTIF)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        return generateAuthResponse(user, "Token rafraîchi avec succès");
    }

    /**
     * Obtenir le profil utilisateur avec données spécifiques
     */
    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        return UserProfileResponse.builder()
                .id(user.getId())
                .nom(user.getNom())
                .telephone(user.getTelephone())
                .commune(user.getCommune())
                .region(user.getRegion())
                .languePreferee(user.getLanguePreferee())
                .statut(user.getStatut())
                .typeUtilisateur(getTypeUtilisateur(user))
                .dateCreation(user.getDateCreation())
                .dateDerniereConnexion(user.getDateDerniereConnexion())
                .specificData(getSpecificUserData(user))
                .build();
    }

    // Méthodes utilitaires privées

    private void validateUniquePhone(String telephone) {
        if (userRepository.existsByTelephone(telephone)) {
            throw new DuplicateResourceException("Ce numéro de téléphone est déjà utilisé");
        }
    }

    private void mapCommonFields(User user, String nom, String telephone, String motDePasse,
                                 String commune, String region, LangueEnum languePreferee) {
        user.setNom(nom);
        user.setTelephone(telephone);
        user.setMotDePasseHash(passwordEncoder.encode(motDePasse));
        user.setCommune(commune);
        user.setRegion(region);
        user.setLanguePreferee(languePreferee != null ? languePreferee : LangueEnum.FR);
        user.setStatut(StatutEnum.ACTIF);
    }

    private AuthResponse generateAuthResponse(User user, String message) {
        String token = jwtTokenProvider.generateToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .nom(user.getNom())
                .telephone(user.getTelephone())
                .typeUtilisateur(getTypeUtilisateur(user))
                .languePreferee(user.getLanguePreferee())
                .commune(user.getCommune())
                .region(user.getRegion())
                .dateExpiration(jwtTokenProvider.getExpirationDateFromToken(token))
                .message(message)
                .build();
    }

    private TypeUtilisateurEnum getTypeUtilisateur(User user) {
        if (user instanceof Agriculteur) return TypeUtilisateurEnum.AGRICULTEUR;
        if (user instanceof Acheteur) return TypeUtilisateurEnum.ACHETEUR;
        if (user instanceof Fournisseur) return TypeUtilisateurEnum.FOURNISSEUR;
        return TypeUtilisateurEnum.ADMIN;
    }

    private Object getSpecificUserData(User user) {
        if (user instanceof Agriculteur agriculteur) {
            return AgriculteurData.builder()
                    .typeCulture(agriculteur.getTypeCulture())
                    .surfaceTotale(agriculteur.getSurfaceTotale())
                    .anneesExperience(agriculteur.getAnneesExperience())
                    .estCooperative(agriculteur.getEstCooperative())
                    .cooperativeNom(agriculteur.getCooperativeNom())
                    .certificationBio(agriculteur.getCertificationBio())
                    .methodeIrrigation(agriculteur.getMethodeIrrigation())
                    .equipements(agriculteur.getEquipements())
                    .productionAnnuelle(agriculteur.getProductionAnnuelle())
                    .objectifProduction(agriculteur.getObjectifProduction())
                    .formationAgricole(agriculteur.getFormationAgricole())
                    .accesCredit(agriculteur.getAccesCredit())
                    .problemesPrincipaux(agriculteur.getProblemesPrincipaux())
                    .build();
        }

        if (user instanceof Acheteur acheteur) {
            return AcheteurData.builder()
                    .entreprise(acheteur.getEntreprise())
                    .typeAchat(acheteur.getTypeAchat())
                    .produitsRecherches(acheteur.getProduitsRecherches())
                    .capaciteStockage(acheteur.getCapaciteStockage())
                    .zoneAchat(acheteur.getZoneAchat())
                    .frequenceAchat(acheteur.getFrequenceAchat())
                    .modePaiement(acheteur.getModePaiement())
                    .delaiPaiement(acheteur.getDelaiPaiement())
                    .exigencesQualite(acheteur.getExigencesQualite())
                    .certificationRequise(acheteur.getCertificationRequise())
                    .volumeMensuel(acheteur.getVolumeMensuel())
                    .prixMaximum(acheteur.getPrixMaximum())
                    .transportAssure(acheteur.getTransportAssure())
                    .numeroLicense(acheteur.getNumeroLicense())
                    .secteurActivite(acheteur.getSecteurActivite())
                    .build();
        }

        if (user instanceof Fournisseur fournisseur) {
            return FournisseurData.builder()
                    .entreprise(fournisseur.getEntreprise())
                    .typeProduits(fournisseur.getTypeProduits())
                    .marquesRepresentees(fournisseur.getMarquesRepresentees())
                    .zoneLivraison(fournisseur.getZoneLivraison())
                    .licenceNumero(fournisseur.getLicenceNumero())
                    .agrementMinistere(fournisseur.getAgrementMinistere())
                    .serviceConseil(fournisseur.getServiceConseil())
                    .serviceApresVente(fournisseur.getServiceApresVente())
                    .formationTechnique(fournisseur.getFormationTechnique())
                    .creditClient(fournisseur.getCreditClient())
                    .delaiLivraison(fournisseur.getDelaiLivraison())
                    .garantieProduits(fournisseur.getGarantieProduits())
                    .stockDisponible(fournisseur.getStockDisponible())
                    .prixCatalogue(fournisseur.getPrixCatalogue())
                    .conditionsPaiement(fournisseur.getConditionsPaiement())
                    .certifications(fournisseur.getCertifications())
                    .siteWeb(fournisseur.getSiteWeb())
                    .adressePhysique(fournisseur.getAdressePhysique())
                    .horairesOuverture(fournisseur.getHorairesOuverture())
                    .build();
        }

        return null;
    }

    // Classes internes pour les données spécifiques

    @lombok.Data
    @lombok.Builder
    public static class AgriculteurData {
        private String typeCulture;
        private Double surfaceTotale;
        private Integer anneesExperience;
        private Boolean estCooperative;
        private String cooperativeNom;
        private Boolean certificationBio;
        private String methodeIrrigation;
        private String equipements;
        private Double productionAnnuelle;
        private String objectifProduction;
        private Boolean formationAgricole;
        private Boolean accesCredit;
        private String problemesPrincipaux;
    }

    @lombok.Data
    @lombok.Builder
    public static class AcheteurData {
        private String entreprise;
        private String typeAchat;
        private String produitsRecherches;
        private Double capaciteStockage;
        private String zoneAchat;
        private String frequenceAchat;
        private String modePaiement;
        private Integer delaiPaiement;
        private String exigencesQualite;
        private Boolean certificationRequise;
        private Double volumeMensuel;
        private Double prixMaximum;
        private Boolean transportAssure;
        private String numeroLicense;
        private String secteurActivite;
    }

    @lombok.Data
    @lombok.Builder
    public static class FournisseurData {
        private String entreprise;
        private String typeProduits;
        private String marquesRepresentees;
        private String zoneLivraison;
        private String licenceNumero;
        private Boolean agrementMinistere;
        private Boolean serviceConseil;
        private Boolean serviceApresVente;
        private Boolean formationTechnique;
        private Boolean creditClient;
        private Integer delaiLivraison;
        private Integer garantieProduits;
        private Boolean stockDisponible;
        private String prixCatalogue;
        private String conditionsPaiement;
        private String certifications;
        private String siteWeb;
        private String adressePhysique;
        private String horairesOuverture;
    }
}