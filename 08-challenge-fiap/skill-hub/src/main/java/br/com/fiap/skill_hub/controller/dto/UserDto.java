package br.com.fiap.skill_hub.controller.dto;

import br.com.fiap.skill_hub.repository.entities.UserEntity;
import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
@Data
public class UserDto {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private ProfileEnum profile;

    public UserDto(UserEntity user){
        BeanUtils.copyProperties(user,this);



    }


}
