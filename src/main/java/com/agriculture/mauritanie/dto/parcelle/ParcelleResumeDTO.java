package com.agriculture.mauritanie.dto.parcelle;

import com.agriculture.mauritanie.entity.StatutCultureEnum;
import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ParcelleResumeDTO {
    private Long id;
    private String nomParcelle;
    private BigDecimal surfaceHectares;
    private String typeCulture;
    private String commune;
    private StatutCultureEnum statutCulture;
    private String statutCultureLibelle;
    private LocalDate datePlantation;
    private LocalDate dateRecoltePrevue;
    private Boolean irrigation;
    private Integer joursAvantRecolte;
}