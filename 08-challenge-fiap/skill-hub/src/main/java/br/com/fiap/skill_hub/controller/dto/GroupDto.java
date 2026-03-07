package br.com.fiap.skill_hub.controller.dto;

import lombok.Data;

import java.util.List;
@Data
public class GroupDto {
    private Integer id;
    private String description;
    private List<UserDto> users;
    private List<SkillDto> skills;

}
