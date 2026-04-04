package br.com.fiap.skill_hub.controller.dto;

import br.com.fiap.skill_hub.repository.entities.UserEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private ProfileEnum profile;


    // sem expor dadso sensiveis  no caso password



}