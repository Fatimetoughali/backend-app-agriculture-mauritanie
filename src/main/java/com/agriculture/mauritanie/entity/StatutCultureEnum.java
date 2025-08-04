// StatutCultureEnum.java
package com.agriculture.mauritanie.entity;

public enum StatutCultureEnum {
    EN_PREPARATION("En préparation"),
    PLANTE("Planté"),
    EN_CROISSANCE("En croissance"),
    EN_FLORAISON("En floraison"),
    MATURATION("En maturation"),
    PRET_A_RECOLTER("Prêt à récolter"),
    RECOLTE("Récolté"),
    EN_REPOS("En repos");

    private final String libelle;

    StatutCultureEnum(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}