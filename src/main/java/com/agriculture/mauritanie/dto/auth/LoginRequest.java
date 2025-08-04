package com.agriculture.mauritanie.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^\\+222[0-9]{8}$|^[0-9]{8}$", message = "Format téléphone invalide")
    private String telephone;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;
}