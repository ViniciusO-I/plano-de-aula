package br.com.fiap.skill_hub.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TokenResponseDto(
        @NotBlank(message = "validation.token_response.access_token.required")
        String accessToken,

        @NotBlank(message = "validation.token_response.refresh_token.required")
        String refreshToken,

        @NotBlank(message = "validation.token_response.token_type.required")
        String tokenType,

        @NotNull(message = "validation.token_response.expires_in.required")
        @Positive(message = "validation.token_response.expires_in.positive")
        Long expiresIn
) {
}
