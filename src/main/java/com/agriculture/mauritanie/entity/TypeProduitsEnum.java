package com.agriculture.mauritanie.entity;

public enum TypeProduitsEnum {
    SEMENCES("Semences"),
    ENGRAIS("Engrais"),
    PESTICIDES("Pesticides"),
    OUTILS("Outils agricoles"),
    EQUIPEMENTS("Équipements"),
    IRRIGATION("Matériel d'irrigation"),
    VETEMENTS("Vêtements de protection"),
    FOURRAGE("Fourrage animal"),
    MEDICAMENTS_VETERINAIRES("Médicaments vétérinaires");

    private final String libelle;

    TypeProduitsEnum(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
