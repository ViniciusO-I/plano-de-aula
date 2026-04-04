package br.com.fiap.skill_hub.mapper;

import br.com.fiap.skill_hub.controller.dto.GroupDto;
import br.com.fiap.skill_hub.controller.dto.ProfileEnum;
import br.com.fiap.skill_hub.controller.dto.SkillDto;
import br.com.fiap.skill_hub.controller.dto.UserDto;
import br.com.fiap.skill_hub.repository.entities.GroupEntity;
import br.com.fiap.skill_hub.repository.entities.SkillEntity;
import br.com.fiap.skill_hub.repository.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MapperImplTest {

    private final UserMapperImpl userMapper = new UserMapperImpl();
    private final SkillMapperImpl skillMapper = new SkillMapperImpl();

    @Test
    void userMapperShouldMapEntityAndDto() {
        UserDto dto = new UserDto(1, "Alice", "alice@email.com", "123", ProfileEnum.STUDENT);
        UserEntity entity = userMapper.toEntity(dto);

        assertEquals(1, entity.getId());
        assertEquals("Alice", entity.getName());

        UserDto mappedBack = userMapper.toDto(entity);
        assertEquals("alice@email.com", mappedBack.email());
        assertEquals(ProfileEnum.STUDENT, mappedBack.profile());

        assertEquals(null, userMapper.toEntity(null));
        assertEquals(null, userMapper.toDto(null));
        assertEquals(null, userMapper.toListDto(null));
        assertEquals(1, userMapper.toListDto(List.of(entity)).size());
    }

    @Test
    void skillMapperShouldMapList() {
        SkillEntity s1 = new SkillEntity();
        s1.setId(1);
        s1.setDescription("Java");
        SkillEntity s2 = new SkillEntity();
        s2.setId(2);
        s2.setDescription("Docker");

        List<SkillDto> result = skillMapper.toListDto(List.of(s1, s2));

        assertEquals(2, result.size());
        assertEquals("Docker", result.get(1).description());
    }

    @Test
    void groupMapperShouldMapBothDirections() {
        GroupMapperImpl groupMapper = new GroupMapperImpl();
        ReflectionTestUtils.setField(groupMapper, "userMapper", userMapper);
        ReflectionTestUtils.setField(groupMapper, "skillMapper", skillMapper);

        UserEntity owner = new UserEntity();
        owner.setId(9);
        owner.setName("Owner");
        owner.setEmail("owner@email.com");
        owner.setProfile(ProfileEnum.ADMINISTRATOR);

        SkillEntity requiredSkill = new SkillEntity();
        requiredSkill.setId(3);
        requiredSkill.setDescription("Spring");

        GroupEntity entity = new GroupEntity();
        entity.setId(5);
        entity.setDescription("Grupo A");
        entity.setMaxMembers(10);
        entity.setOwner(owner);
        entity.setUsers(Set.of(owner));
        entity.setSkills(Set.of(requiredSkill));

        GroupDto dto = groupMapper.toDto(entity);
        assertEquals(5, dto.id());
        assertEquals(9, dto.ownerId());
        assertEquals(1, dto.skills().size());

        GroupEntity mappedBack = groupMapper.toEntity(dto);
        assertNotNull(mappedBack.getOwner());
        assertEquals(9, mappedBack.getOwner().getId());
        assertEquals("Grupo A", mappedBack.getDescription());

        assertEquals(null, groupMapper.toDto(null));
        assertEquals(null, groupMapper.toEntity(null));
        assertEquals(1, groupMapper.toListDto(List.of(entity)).size());
        assertEquals(null, groupMapper.toListDto(null));

        GroupEntity withoutOwner = new GroupEntity();
        withoutOwner.setId(8);
        withoutOwner.setDescription("Sem owner");
        withoutOwner.setMaxMembers(2);
        withoutOwner.setUsers(null);
        withoutOwner.setSkills(null);

        GroupDto dtoWithoutOwner = groupMapper.toDto(withoutOwner);
        assertEquals(null, dtoWithoutOwner.ownerId());

        GroupDto dtoWithoutCollections = new GroupDto(2, "Sem colecoes", 3, 9, null, null);
        GroupEntity entityWithoutCollections = groupMapper.toEntity(dtoWithoutCollections);
        assertNotNull(entityWithoutCollections.getOwner());
        assertEquals(9, entityWithoutCollections.getOwner().getId());
        assertEquals(null, entityWithoutCollections.getUsers());
        assertEquals(null, entityWithoutCollections.getSkills());
    }
}


