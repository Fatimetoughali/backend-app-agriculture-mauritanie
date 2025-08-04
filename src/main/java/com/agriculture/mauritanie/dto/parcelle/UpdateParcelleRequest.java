package com.agriculture.mauritanie.dto.parcelle;

import com.agriculture.mauritanie.entity.StatutCultureEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateParcelleRequest {

    @NotBlank(message = "Le nom de la parcelle est obligatoire")
    private String nomParcelle;

    @NotNull(message = "La surface est obligatoire")
    @DecimalMin(value = "0.01", message = "La surface doit être positive")
    private BigDecimal surfaceHectares;

    @NotBlank(message = "Le type de culture est obligatoire")
    private String typeCulture;

    private String commune;
    private String region;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDate datePlantation;
    private LocalDate dateRecoltePrevue;
    private StatutCultureEnum statutCulture;
    private Boolean irrigation;
    private String notes;
}