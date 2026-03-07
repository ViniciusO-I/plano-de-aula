package br.com.fiap.skill_hub.service;

import br.com.fiap.skill_hub.controller.dto.LoginDto;
import br.com.fiap.skill_hub.controller.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Service
public class UserService {

    public UserDto create(UserDto userDto) {
        userDto.setId(33);
        return userDto;
    }

    public UserDto login(LoginDto loginDto) {
        UserDto userDto = new UserDto();
        userDto.setEmail("viniciusemail.com");
        return userDto;
    }

    public  UserDto addSkill(Integer idUser, List<Integer> idSkills){
        UserDto userDto = new UserDto();
        userDto.setName("test");
        return userDto;

    }
}
