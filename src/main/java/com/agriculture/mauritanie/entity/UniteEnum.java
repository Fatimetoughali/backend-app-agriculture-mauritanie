// UniteEnum.java
package com.agriculture.mauritanie.entity;

public enum UniteEnum {
    KG("Kilogramme"),
    TONNE("Tonne"),
    SAC("Sac"),
    PIECE("Pièce"),
    LITRE("Litre"),
    CAISSE("Caisse");

    private final String libelle;

    UniteEnum(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}

