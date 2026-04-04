package br.com.fiap.skill_hub.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record GroupDto(
        @Positive(message = "validation.group.id.positive")
        Integer id,

        @NotBlank(message = "validation.group.description.required")
        @Size(min = 3, max = 120, message = "validation.group.description.size")
        String description,

        @Positive(message = "validation.group.max_members.positive")
        Integer maxMembers,

        @Positive(message = "validation.group.owner_id.positive")
        Integer ownerId,

        @Valid
        List<UserDto> users,

        @NotEmpty(message = "validation.group.skills.required")
        @Valid
        List<SkillDto> skills
) {
}
