package br.com.fiap.skill_hub.controller.dto;

import lombok.Data;

import java.util.List;
@Data
public class ProfileDto {
private Integer id;

    private ProfileEnum type;
    private List<SkillDto> skills;

}

