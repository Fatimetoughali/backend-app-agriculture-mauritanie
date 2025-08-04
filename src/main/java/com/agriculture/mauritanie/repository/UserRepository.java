package com.agriculture.mauritanie.repository;

import com.agriculture.mauritanie.entity.User;
import com.agriculture.mauritanie.entity.StatutEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Trouve un utilisateur par son numéro de téléphone
     */
    Optional<User> findByTelephone(String telephone);

    /**
     * Vérifie si un numéro de téléphone existe déjà
     */
    boolean existsByTelephone(String telephone);

    /**
     * Trouve un utilisateur actif par téléphone
     */
    Optional<User> findByTelephoneAndStatut(String telephone, StatutEnum statut);

    /**
     * Trouve tous les utilisateurs par région
     */
    List<User> findByRegion(String region);

    /**
     * Trouve tous les utilisateurs par commune
     */
    List<User> findByCommune(String commune);

    /**
     * Trouve tous les utilisateurs par statut
     */
    List<User> findByStatut(StatutEnum statut);

    /**
     * Met à jour la date de dernière connexion
     */
    @Query("UPDATE User u SET u.dateDerniereConnexion = :date WHERE u.id = :userId")
    void updateDerniereConnexion(@Param("userId") Long userId, @Param("date") LocalDateTime date);

    /**
     * Trouve les utilisateurs créés après une certaine date
     */
    List<User> findByDateCreationAfter(LocalDateTime date);

    /**
     * Compte le nombre d'utilisateurs par type
     */
    @Query("SELECT TYPE(u), COUNT(u) FROM User u GROUP BY TYPE(u)")
    List<Object[]> countByType();

    /**
     * Trouve les utilisateurs inactifs depuis une certaine période
     */
    @Query("SELECT u FROM User u WHERE u.dateDerniereConnexion < :date OR u.dateDerniereConnexion IS NULL")
    List<User> findInactiveUsers(@Param("date") LocalDateTime date);
}