package com.agriculture.mauritanie.entity;

public enum CertificationEnum {
    BIO("Bio"),
    TRADITIONNEL("Traditionnel"),
    COMMERCE_EQUITABLE("Commerce équitable");

    private final String libelle;

    CertificationEnum(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}