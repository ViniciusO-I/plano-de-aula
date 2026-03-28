package br.com.fiap.skill_hub.service;

import br.com.fiap.skill_hub.controller.dto.LoginDto;
import br.com.fiap.skill_hub.controller.dto.UserDto;
import br.com.fiap.skill_hub.repository.UserRepository;
import br.com.fiap.skill_hub.repository.entities.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto create(UserDto userDtoRequest) {
        //1 passo transformar request em entidade
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userDtoRequest.getName());
        userEntity.setPassword(userDtoRequest.getPassword());
        userEntity.setEmail(userDtoRequest.getEmail());

        //TODO  completar os parametros que faltam

        //2 passo salvar a entidade no banco
        UserEntity saveEntity = userRepository.save(userEntity);

        // 3 passo transformar  em dto para a controller
        UserDto userDtoResponse = new UserDto();
        userDtoResponse.setId(saveEntity.getId());
        userDtoResponse.setName(saveEntity.getName());
        userDtoResponse.setEmail(saveEntity.getEmail());

        return userDtoResponse;
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

    //TODO TRANSFORMAR DTO EM ENTITY
}
