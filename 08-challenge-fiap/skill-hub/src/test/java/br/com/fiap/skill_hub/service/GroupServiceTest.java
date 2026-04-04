package br.com.fiap.skill_hub.service;

import br.com.fiap.skill_hub.controller.dto.GroupDto;
import br.com.fiap.skill_hub.controller.dto.SkillDto;
import br.com.fiap.skill_hub.exception.group.GroupNoAvailableSeatsException;
import br.com.fiap.skill_hub.exception.group.GroupNotFoundException;
import br.com.fiap.skill_hub.exception.group.GroupOwnerNotFoundException;
import br.com.fiap.skill_hub.exception.group.UserAlreadyGroupMemberException;
import br.com.fiap.skill_hub.exception.group.UserLacksRequiredSkillsException;
import br.com.fiap.skill_hub.exception.skill.SkillNotFoundException;
import br.com.fiap.skill_hub.exception.user.UserNotFoundException;
import br.com.fiap.skill_hub.mapper.GroupMapper;
import br.com.fiap.skill_hub.repository.GroupRepository;
import br.com.fiap.skill_hub.repository.SkillRepository;
import br.com.fiap.skill_hub.repository.UserRepository;
import br.com.fiap.skill_hub.repository.entities.GroupEntity;
import br.com.fiap.skill_hub.repository.entities.SkillEntity;
import br.com.fiap.skill_hub.repository.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private GroupMapper groupMapper;

    @InjectMocks
    private GroupService groupService;

    @Test
    void joinShouldThrowWhenUserAlreadyMember() {
        UserEntity user = new UserEntity();
        user.setId(5);

        GroupEntity group = new GroupEntity();
        group.setId(1);
        group.setMaxMembers(10);
        group.setUsers(Set.of(user));

        when(groupRepository.findById(1)).thenReturn(Optional.of(group));
        when(userRepository.findById(5)).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyGroupMemberException.class, () -> groupService.join(1, 5));
    }

    @Test
    void joinShouldThrowWhenNoAvailableSeats() {
        UserEntity owner = new UserEntity();
        owner.setId(1);

        UserEntity user = new UserEntity();
        user.setId(5);

        GroupEntity group = new GroupEntity();
        group.setId(1);
        group.setMaxMembers(1);
        group.setUsers(Set.of(owner));

        when(groupRepository.findById(1)).thenReturn(Optional.of(group));
        when(userRepository.findById(5)).thenReturn(Optional.of(user));

        assertThrows(GroupNoAvailableSeatsException.class, () -> groupService.join(1, 5));
    }

    @Test
    void joinShouldThrowWhenUserLacksRequiredSkills() {
        UserEntity user = new UserEntity();
        user.setId(5);
        user.setSkills(Set.of());

        SkillEntity requiredSkill = new SkillEntity();
        requiredSkill.setId(77);

        GroupEntity group = new GroupEntity();
        group.setId(1);
        group.setMaxMembers(3);
        group.setUsers(Set.of());
        group.setSkills(Set.of(requiredSkill));

        when(groupRepository.findById(1)).thenReturn(Optional.of(group));
        when(userRepository.findById(5)).thenReturn(Optional.of(user));

        assertThrows(UserLacksRequiredSkillsException.class, () -> groupService.join(1, 5));
    }

    @Test
    void createShouldPersistGroupAndReturnMappedDto() {
        UserEntity owner = new UserEntity();
        owner.setId(1);

        SkillEntity requiredSkill = new SkillEntity();
        requiredSkill.setId(2);

        GroupEntity saved = new GroupEntity();
        saved.setId(99);
        saved.setOwner(owner);

        GroupDto response = new GroupDto(99, "Grupo Java", 5, 1, List.of(), List.of(new SkillDto(2, "Java")));
        GroupDto request = new GroupDto(null, "Grupo Java", 5, 1, List.of(), List.of(new SkillDto(2, "Java")));

        when(userRepository.findById(1)).thenReturn(Optional.of(owner));
        when(skillRepository.findAllById(List.of(2))).thenReturn(List.of(requiredSkill));
        when(groupRepository.save(org.mockito.ArgumentMatchers.any(GroupEntity.class))).thenReturn(saved);
        when(groupMapper.toDto(saved)).thenReturn(response);

        GroupDto result = groupService.create(request);

        assertEquals(99, result.id());
        assertEquals("Grupo Java", result.description());
    }

    @Test
    void listShouldMapAllGroups() {
        GroupEntity g1 = new GroupEntity();
        g1.setId(1);
        GroupEntity g2 = new GroupEntity();
        g2.setId(2);

        when(groupRepository.findAll()).thenReturn(List.of(g1, g2));
        when(groupMapper.toDto(g1)).thenReturn(new GroupDto(1, "G1", 2, 1, List.of(), List.of()));
        when(groupMapper.toDto(g2)).thenReturn(new GroupDto(2, "G2", 2, 1, List.of(), List.of()));

        List<GroupDto> result = groupService.list();

        assertEquals(2, result.size());
    }

    @Test
    void findByIdShouldThrowWhenGroupDoesNotExist() {
        when(groupRepository.findById(7)).thenReturn(Optional.empty());

        assertThrows(GroupNotFoundException.class, () -> groupService.findById(7));
    }

    @Test
    void updateShouldThrowWhenGroupDoesNotExist() {
        GroupDto request = new GroupDto(null, "Grupo", 5, 1, List.of(), List.of(new SkillDto(1, "Java")));
        when(groupRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(GroupNotFoundException.class, () -> groupService.update(1, request));
    }

    @Test
    void updateShouldThrowWhenOwnerDoesNotExist() {
        GroupEntity existing = new GroupEntity();
        existing.setId(1);

        GroupDto request = new GroupDto(null, "Grupo", 5, 99, List.of(), List.of(new SkillDto(1, "Java")));

        when(groupRepository.findById(1)).thenReturn(Optional.of(existing));
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(GroupOwnerNotFoundException.class, () -> groupService.update(1, request));
    }

    @Test
    void updateShouldThrowWhenAnyRequiredSkillIsMissing() {
        GroupEntity existing = new GroupEntity();
        existing.setId(1);

        UserEntity owner = new UserEntity();
        owner.setId(1);

        SkillEntity only = new SkillEntity();
        only.setId(1);

        GroupDto request = new GroupDto(null, "Grupo", 5, 1, List.of(), List.of(new SkillDto(1, "Java"), new SkillDto(2, "Docker")));

        when(groupRepository.findById(1)).thenReturn(Optional.of(existing));
        when(userRepository.findById(1)).thenReturn(Optional.of(owner));
        when(skillRepository.findAllById(List.of(1, 2))).thenReturn(List.of(only));

        assertThrows(SkillNotFoundException.class, () -> groupService.update(1, request));
    }

    @Test
    void deleteShouldThrowWhenGroupDoesNotExist() {
        when(groupRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(GroupNotFoundException.class, () -> groupService.delete(10));
    }

    @Test
    void joinShouldThrowWhenUserDoesNotExist() {
        GroupEntity group = new GroupEntity();
        group.setId(1);
        group.setMaxMembers(10);
        group.setUsers(Set.of());

        when(groupRepository.findById(1)).thenReturn(Optional.of(group));
        when(userRepository.findById(50)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> groupService.join(1, 50));
    }

    @Test
    void joinShouldReturnMappedGroupWhenSuccessful() {
        SkillEntity required = new SkillEntity();
        required.setId(10);

        UserEntity user = new UserEntity();
        user.setId(5);
        user.setSkills(Set.of(required));

        GroupEntity group = new GroupEntity();
        group.setId(1);
        group.setMaxMembers(3);
        group.setUsers(new java.util.HashSet<>());
        group.setSkills(Set.of(required));

        GroupEntity saved = new GroupEntity();
        saved.setId(1);

        GroupDto mapped = new GroupDto(1, "Grupo", 3, 1, List.of(), List.of(new SkillDto(10, "Java")));

        when(groupRepository.findById(1)).thenReturn(Optional.of(group));
        when(userRepository.findById(5)).thenReturn(Optional.of(user));
        when(groupRepository.save(group)).thenReturn(saved);
        when(groupMapper.toDto(saved)).thenReturn(mapped);

        GroupDto result = groupService.join(1, 5);

        assertEquals(1, result.id());
    }
}

