package com.agriculture.mauritanie.entity;

public enum FrequenceAchatEnum {
    QUOTIDIEN("Quotidien"),
    HEBDOMADAIRE("Hebdomadaire"),
    MENSUEL("Mensuel"),
    TRIMESTRIEL("Trimestriel"),
    SAISONNIER("Saisonnier"),
    ANNUEL("Annuel");

    private final String libelle;

    FrequenceAchatEnum(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}