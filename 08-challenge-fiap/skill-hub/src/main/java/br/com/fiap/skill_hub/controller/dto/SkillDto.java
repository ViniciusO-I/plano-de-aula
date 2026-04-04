package br.com.fiap.skill_hub.controller.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public record SkillDto(@NotBlank String name, String category) {



}
