package br.com.fiap.skill_hub.service;

import br.com.fiap.skill_hub.controller.dto.UserDto;
import br.com.fiap.skill_hub.exception.user.EmailAlreadyRegisteredException;
import br.com.fiap.skill_hub.exception.user.UserNotFoundException;
import br.com.fiap.skill_hub.exception.skill.SkillNotFoundException;
import br.com.fiap.skill_hub.mapper.UserMapper;
import br.com.fiap.skill_hub.repository.SkillRepository;
import br.com.fiap.skill_hub.repository.UserRepository;
import br.com.fiap.skill_hub.repository.entities.SkillEntity;
import br.com.fiap.skill_hub.repository.entities.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SkillRepository skillRepository;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder,
                       SkillRepository skillRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.skillRepository = skillRepository;
    }

    public UserDto create(UserDto userDtoRequest) {

        if (userRepository.existsByEmail(userDtoRequest.email())) {
            throw new EmailAlreadyRegisteredException("E-mail já cadastrado");
        }

        //1 passo transformar request em entidade
        UserEntity userEntity = userMapper.toEntity(userDtoRequest);
        userEntity.setPassword(passwordEncoder.encode(userDtoRequest.password()));

        //2 passo salvar a entidade no banco
        UserEntity saveEntity = userRepository.save(userEntity);

        // 3 passo transformar  em dto para a controller
        return sanitizePassword(userMapper.toDto(saveEntity));
    }

    public List<UserDto> list() {
        List<UserEntity> usersList = userRepository.findAll();
        List<UserDto> dtoList = userMapper.toListDto(usersList);
        List<UserDto> sanitized = new ArrayList<>();
        for (UserDto userDto : dtoList) {
            sanitized.add(sanitizePassword(userDto));
        }
        return sanitized;
    }

    public UserDto addSkills(Integer userId, List<Integer> skillIds) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        List<SkillEntity> skills = skillRepository.findAllById(skillIds);
        if (skills.size() != skillIds.size()) {
            throw new SkillNotFoundException("Uma ou mais skills não foram encontradas");
        }

        user.setSkills(Set.copyOf(skills));
        UserEntity savedUser = userRepository.save(user);
        return sanitizePassword(userMapper.toDto(savedUser));
    }

    public List<Integer> listSkillIds(Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        List<Integer> skillIds = new ArrayList<>();
        for (SkillEntity skill : user.getSkills()) {
            skillIds.add(skill.getId());
        }
        return skillIds;
    }

    private UserDto sanitizePassword(UserDto userDto) {
        return new UserDto(userDto.id(), userDto.name(), userDto.email(), null, userDto.profile());
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



    //TODO TRANSFORMAR DTO EM ENTITY
}
