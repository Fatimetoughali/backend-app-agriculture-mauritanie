package com.agriculture.mauritanie.entity;

public enum LangueEnum {AR("Arabe"),
    FR("Français"),
    HASSANIYA("Hassaniya"),
    PULAAR("Pulaar");

    private final String libelle;

    LangueEnum(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}