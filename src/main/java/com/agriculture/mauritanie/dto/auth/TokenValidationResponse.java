// TokenValidationResponse.java
package com.agriculture.mauritanie.dto.auth;

import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
public class TokenValidationResponse {
    private boolean valid;
    private String username;
    private Long userId;
    private LocalDateTime expiresAt;
}