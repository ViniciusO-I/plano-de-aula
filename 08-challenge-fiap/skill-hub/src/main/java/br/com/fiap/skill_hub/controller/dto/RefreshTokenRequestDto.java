package br.com.fiap.skill_hub.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RefreshTokenRequestDto(
		@NotBlank(message = "validation.refresh_token.required")
		@Size(min = 20, message = "validation.refresh_token.size")
		String refreshToken
) {
}
