package com.agriculture.mauritanie.entity;

public enum QualiteEnum {
    PREMIUM("Premium"),
    STANDARD("Standard"),
    ECONOMIQUE("Économique");

    private final String libelle;

    QualiteEnum(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}