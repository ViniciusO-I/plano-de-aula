package br.com.fiap.skill_hub.controller.dto;

import lombok.Data;

import java.util.List;
@Data
public class UserDto {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private ProfileDto profile;


}
