package com.agriculture.mauritanie.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotBlank(message = "Le refresh token est obligatoire")
    private String refreshToken;
}


