package br.com.fiap.skill_hub.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDto(
        @NotBlank(message = "validation.login.email.required")
        @Email(message = "validation.login.email.invalid")
        @Size(max = 255, message = "validation.login.email.max")
        String email,

        @NotBlank(message = "validation.login.password.required")
        @Size(min = 8, max = 64, message = "validation.login.password.size")
        String password
) {
}
