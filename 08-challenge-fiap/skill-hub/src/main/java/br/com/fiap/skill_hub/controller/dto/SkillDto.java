package br.com.fiap.skill_hub.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SkillDto(
        @Positive(message = "validation.skill.id.positive")
        Integer id,

        @NotBlank(message = "validation.skill.description.required")
        @Size(min = 2, max = 80, message = "validation.skill.description.size")
        String description
) {
}
