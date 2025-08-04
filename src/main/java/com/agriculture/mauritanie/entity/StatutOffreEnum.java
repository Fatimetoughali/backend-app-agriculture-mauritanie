package com.agriculture.mauritanie.entity;

public enum StatutOffreEnum {
    DISPONIBLE("Disponible"),
    RESERVE("Réservé"),
    VENDU("Vendu"),
    EXPIRE("Expiré"),
    SUSPENDU("Suspendu");

    private final String libelle;

    StatutOffreEnum(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}