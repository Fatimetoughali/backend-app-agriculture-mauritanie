package com.agriculture.mauritanie.entity;

public enum TypeCultureEnum {
    CEREALES("Céréales"),
    LEGUMINEUSES("Légumineuses"),
    MARAICHAGE("Maraîchage"),
    ARBORICULTURE("Arboriculture"),
    ELEVAGE("Élevage"),
    PECHE("Pêche"),
    APICULTURE("Apiculture"),
    CULTURES_FOURRAGERES("Cultures fourragères"),
    CULTURES_INDUSTRIELLES("Cultures industrielles");

    private final String libelle;

    TypeCultureEnum(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}