package br.com.fiap.skill_hub.service;

import br.com.fiap.skill_hub.controller.dto.LoginDto;
import br.com.fiap.skill_hub.controller.dto.UserDto;
import br.com.fiap.skill_hub.mapper.UserMapper;
import br.com.fiap.skill_hub.repository.UserRepository;
import br.com.fiap.skill_hub.repository.entities.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDto create(UserDto userDtoRequest) {
        //1 passo transformar request em entidade
        UserEntity userEntity = userMapper.toEntity(userDtoRequest);

        //2 passo salvar a entidade no banco
        UserEntity saveEntity = userRepository.save(userEntity);

        // 3 passo transformar  em dto para a controller
        UserDto userDtoResponse = userMapper.toDto(saveEntity);
        return userDtoResponse;
    }


//    public UserDto login(LoginDto loginDto) {
//        UserDto userDto = new UserDto();
//        userDto.setEmail("viniciusemail.com");
//        return userDto;
//    }

//    public  UserDto addSkill(Integer idUser, List<Integer> idSkills){
//        UserDto userDto = new UserDto();
//        userDto.setName("test");
//        return userDto;
//
//    }

    public List<UserDto> list() {
        List<UserEntity> usersList = userRepository.findAll();
        return userMapper.toListDto(usersList);

    }

    //TODO TRANSFORMAR DTO EM ENTITY
}
