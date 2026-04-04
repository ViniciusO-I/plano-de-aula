package br.com.fiap.skill_hub.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@NotBlank String name, @NotBlank @Email String email,
                              @NotBlank @Size(min = 3) String password, @NotNull ProfileEnum profile ) {
}
