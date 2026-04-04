package br.com.fiap.skill_hub.service;

import br.com.fiap.skill_hub.controller.dto.ProfileEnum;
import br.com.fiap.skill_hub.controller.dto.UserDto;
import br.com.fiap.skill_hub.exception.skill.SkillNotFoundException;
import br.com.fiap.skill_hub.exception.user.EmailAlreadyRegisteredException;
import br.com.fiap.skill_hub.exception.user.UserNotFoundException;
import br.com.fiap.skill_hub.mapper.UserMapper;
import br.com.fiap.skill_hub.repository.SkillRepository;
import br.com.fiap.skill_hub.repository.UserRepository;
import br.com.fiap.skill_hub.repository.entities.SkillEntity;
import br.com.fiap.skill_hub.repository.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createShouldEncodePasswordAndReturnSanitizedDto() {
        UserDto request = new UserDto(null, "Alice", "alice@email.com", "senha123", ProfileEnum.STUDENT);

        UserEntity mappedEntity = new UserEntity();
        mappedEntity.setEmail(request.email());
        mappedEntity.setName(request.name());
        mappedEntity.setProfile(request.profile());

        UserEntity savedEntity = new UserEntity();
        savedEntity.setId(10);
        savedEntity.setEmail(request.email());
        savedEntity.setName(request.name());
        savedEntity.setProfile(request.profile());
        savedEntity.setPassword("encoded");

        UserDto mappedDto = new UserDto(10, "Alice", "alice@email.com", "encoded", ProfileEnum.STUDENT);

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userMapper.toEntity(request)).thenReturn(mappedEntity);
        when(passwordEncoder.encode("senha123")).thenReturn("encoded");
        when(userRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(userMapper.toDto(savedEntity)).thenReturn(mappedDto);

        UserDto response = userService.create(request);

        assertEquals(10, response.id());
        assertEquals("Alice", response.name());
        assertEquals("alice@email.com", response.email());
        assertNull(response.password());
        assertEquals(ProfileEnum.STUDENT, response.profile());
    }

    @Test
    void createShouldThrowWhenEmailAlreadyExists() {
        UserDto request = new UserDto(null, "Bob", "bob@email.com", "senha123", ProfileEnum.STUDENT);
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(EmailAlreadyRegisteredException.class, () -> userService.create(request));
    }

    @Test
    void addSkillsShouldThrowWhenAnySkillDoesNotExist() {
        UserEntity user = new UserEntity();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        SkillEntity onlySkill = new SkillEntity();
        onlySkill.setId(99);
        when(skillRepository.findAllById(List.of(1, 2))).thenReturn(List.of(onlySkill));

        assertThrows(SkillNotFoundException.class, () -> userService.addSkills(1, List.of(1, 2)));
    }

    @Test
    void listSkillIdsShouldReturnAllSkillIds() {
        SkillEntity skill1 = new SkillEntity();
        skill1.setId(1);
        SkillEntity skill2 = new SkillEntity();
        skill2.setId(2);

        UserEntity user = new UserEntity();
        user.setId(1);
        user.setSkills(Set.of(skill1, skill2));

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        List<Integer> ids = userService.listSkillIds(1);

        assertEquals(2, ids.size());
        assertEquals(Set.of(1, 2), Set.copyOf(ids));
    }

    @Test
    void addSkillsShouldSaveAndReturnSanitizedDtoWhenAllSkillsExist() {
        UserEntity user = new UserEntity();
        user.setId(1);

        SkillEntity skill1 = new SkillEntity();
        skill1.setId(1);
        SkillEntity skill2 = new SkillEntity();
        skill2.setId(2);

        UserEntity saved = new UserEntity();
        saved.setId(1);
        saved.setName("Alice");
        saved.setEmail("alice@email.com");
        saved.setProfile(ProfileEnum.STUDENT);

        UserDto mappedDto = new UserDto(1, "Alice", "alice@email.com", "encoded", ProfileEnum.STUDENT);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(skillRepository.findAllById(List.of(1, 2))).thenReturn(List.of(skill1, skill2));
        when(userRepository.save(user)).thenReturn(saved);
        when(userMapper.toDto(saved)).thenReturn(mappedDto);

        UserDto response = userService.addSkills(1, List.of(1, 2));

        assertEquals(1, response.id());
        assertNull(response.password());
    }

    @Test
    void listSkillIdsShouldThrowWhenUserDoesNotExist() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.listSkillIds(99));
    }

    @Test
    void listShouldReturnSanitizedUsers() {
        UserEntity entity = new UserEntity();
        entity.setId(1);

        UserDto mappedDto = new UserDto(1, "Alice", "alice@email.com", "encoded", ProfileEnum.STUDENT);

        when(userRepository.findAll()).thenReturn(List.of(entity));
        when(userMapper.toListDto(List.of(entity))).thenReturn(List.of(mappedDto));

        List<UserDto> response = userService.list();

        assertEquals(1, response.size());
        assertNull(response.get(0).password());
    }

    @Test
    void addSkillsShouldThrowWhenUserDoesNotExist() {
        when(userRepository.findById(404)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.addSkills(404, List.of(1, 2)));
    }
}

