package br.com.fiap.skill_hub.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UserDto(
        @Positive(message = "validation.user.id.positive")
        Integer id,

        @NotBlank(message = "validation.user.name.required")
        @Size(min = 2, max = 120, message = "validation.user.name.size")
        String name,

        @NotBlank(message = "validation.user.email.required")
        @Email(message = "validation.user.email.invalid")
        @Size(max = 255, message = "validation.user.email.max")
        String email,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank(message = "validation.user.password.required")
        @Size(min = 8, max = 64, message = "validation.user.password.size")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
                message = "validation.user.password.pattern"
        )
        String password,

        @NotNull(message = "validation.user.profile.required")
        ProfileEnum profile
) {
}
