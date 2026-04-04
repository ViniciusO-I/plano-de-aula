package br.com.fiap.skill_hub.response;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponse(Long id, String name, String email,
                           List<SkillResponse> skills, LocalDateTime createdAt) {
}


