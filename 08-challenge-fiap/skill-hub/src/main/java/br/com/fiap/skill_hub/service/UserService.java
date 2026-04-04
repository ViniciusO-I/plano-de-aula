package br.com.fiap.skill_hub.service;

import br.com.fiap.skill_hub.controller.dto.UserDto;
import br.com.fiap.skill_hub.mapper.UserMapper;
import br.com.fiap.skill_hub.repository.SkillRepository;
import br.com.fiap.skill_hub.repository.UserRepository;
import br.com.fiap.skill_hub.repository.entities.SkillEntity;
import br.com.fiap.skill_hub.repository.entities.UserEntity;
import br.com.fiap.skill_hub.response.UserResponse;
import br.com.fiap.skill_hub.response.SkillResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       SkillRepository skillRepository,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.userMapper = userMapper;
    }

    // Cria usuário — usado antes do AuthService existir, mantido por compatibilidade
    public UserDto create(UserDto userDtoRequest) {
        UserEntity userEntity = userMapper.toEntity(userDtoRequest);
        UserEntity saveEntity = userRepository.save(userEntity);
        return userMapper.toDto(saveEntity);
    }

    // Lista todos os usuários
    public List<UserDto> list() {
        return userMapper.toListDto(userRepository.findAll());
    }

    // Busca perfil do usuário pelo email (via JWT)
    public UserResponse getProfile(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return toUserResponse(user);
    }

    // Busca usuário por ID
    public UserResponse getById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
        return toUserResponse(user);
    }

    // Adiciona uma skill ao perfil do usuário
    public UserResponse addSkill(String email, Long skillId) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        SkillEntity skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill não encontrada: " + skillId));

        // Verifica se o usuário já tem essa skill
        if (user.getSkills().contains(skill)) {
            throw new RuntimeException("Usuário já possui essa skill");
        }

        user.getSkills().add(skill);
        userRepository.save(user);
        return toUserResponse(user);
    }

    // Remove uma skill do perfil do usuário
    public UserResponse removeSkill(String email, Long skillId) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        SkillEntity skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill não encontrada: " + skillId));

        user.getSkills().remove(skill);
        userRepository.save(user);
        return toUserResponse(user);
    }

    // Busca usuários que possuem uma skill específica
    public List<UserResponse> findBySkill(Long skillId) {
        skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill não encontrada: " + skillId));

        return userRepository.findAll()
                .stream()
                .filter(u -> u.getSkills().stream()
                        .anyMatch(s -> s.getId().equals(skillId)))
                .map(this::toUserResponse)
                .toList();
    }

    // Converte UserEntity para UserResponse
    private UserResponse toUserResponse(UserEntity user) {
        List<SkillResponse> skills = user.getSkills()
                .stream()
                .map(s -> new SkillResponse(s.getId(), s.getName(), s.getCategory()))
                .toList();

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                skills,
                user.getDtCreated() != null ?
                        user.getDtCreated().toInstant()
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDateTime() : null
        );
    }
}