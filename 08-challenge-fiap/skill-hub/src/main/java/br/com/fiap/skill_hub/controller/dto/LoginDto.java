package br.com.fiap.skill_hub.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(@NotBlank String email, @NotBlank String password) {

}
