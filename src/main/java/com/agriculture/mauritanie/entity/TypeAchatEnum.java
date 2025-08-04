package com.agriculture.mauritanie.entity;

public enum TypeAchatEnum {
    GROS("Achat en gros"),
    DETAIL("Achat au détail"),
    EXPORT("Export"),
    TRANSFORMATION("Transformation"),
    DISTRIBUTION("Distribution");

    private final String libelle;

    TypeAchatEnum(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
