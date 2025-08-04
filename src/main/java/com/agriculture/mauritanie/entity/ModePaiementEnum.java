package com.agriculture.mauritanie.entity;

public enum ModePaiementEnum {
    ESPECES("Espèces"),
    VIREMENT("Virement bancaire"),
    MOBILE_MONEY("Mobile Money"),
    CHEQUE("Chèque"),
    CREDIT("Crédit"),
    TROC("Troc/Échange");

    private final String libelle;

    ModePaiementEnum(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
