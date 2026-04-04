package br.com.fiap.skill_hub.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

public record GroupDto(@NotBlank String name, String description, @Min(2) @Max(10) Integer maxMembers) {


}
