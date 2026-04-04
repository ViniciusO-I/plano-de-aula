package br.com.fiap.skill_hub.service;

import br.com.fiap.skill_hub.controller.dto.SkillDto;
import br.com.fiap.skill_hub.exception.skill.SkillAlreadyRegisteredException;
import br.com.fiap.skill_hub.exception.skill.SkillDescriptionAlreadyInUseException;
import br.com.fiap.skill_hub.exception.skill.SkillNotFoundException;
import br.com.fiap.skill_hub.mapper.SkillMapper;
import br.com.fiap.skill_hub.repository.SkillRepository;
import br.com.fiap.skill_hub.repository.entities.SkillEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private SkillMapper skillMapper;

    @InjectMocks
    private SkillService skillService;

    @Test
    void createShouldThrowWhenDescriptionAlreadyExists() {
        SkillDto request = new SkillDto(null, "Java");

        SkillEntity existing = new SkillEntity();
        existing.setId(1);
        existing.setDescription("Java");

        when(skillRepository.findByDescriptionIgnoreCase("Java")).thenReturn(Optional.of(existing));

        assertThrows(SkillAlreadyRegisteredException.class, () -> skillService.create(request));
    }

    @Test
    void updateShouldThrowWhenTargetSkillDoesNotExist() {
        SkillDto request = new SkillDto(null, "Docker");
        when(skillRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(SkillNotFoundException.class, () -> skillService.update(10, request));
    }

    @Test
    void updateShouldThrowWhenDescriptionInUseByAnotherSkill() {
        SkillEntity target = new SkillEntity();
        target.setId(10);
        target.setDescription("Kotlin");

        SkillEntity other = new SkillEntity();
        other.setId(11);
        other.setDescription("Spring");

        when(skillRepository.findById(10)).thenReturn(Optional.of(target));
        when(skillRepository.findByDescriptionIgnoreCase("Spring")).thenReturn(Optional.of(other));

        SkillDto request = new SkillDto(null, "Spring");
        assertThrows(SkillDescriptionAlreadyInUseException.class, () -> skillService.update(10, request));
    }

    @Test
    void findByIdShouldMapFoundEntity() {
        SkillEntity skill = new SkillEntity();
        skill.setId(7);
        skill.setDescription("Kafka");

        SkillDto dto = new SkillDto(7, "Kafka");

        when(skillRepository.findById(7)).thenReturn(Optional.of(skill));
        when(skillMapper.toDto(skill)).thenReturn(dto);

        SkillDto result = skillService.findById(7);

        assertEquals(7, result.id());
        assertEquals("Kafka", result.description());
    }

    @Test
    void createShouldPersistAndReturnMappedDto() {
        SkillDto request = new SkillDto(null, "  Java  ");

        SkillEntity saved = new SkillEntity();
        saved.setId(4);
        saved.setDescription("Java");

        SkillDto response = new SkillDto(4, "Java");

        when(skillRepository.findByDescriptionIgnoreCase("  Java  ")).thenReturn(Optional.empty());
        when(skillRepository.save(org.mockito.ArgumentMatchers.any(SkillEntity.class))).thenReturn(saved);
        when(skillMapper.toDto(saved)).thenReturn(response);

        SkillDto result = skillService.create(request);

        assertEquals(4, result.id());
        assertEquals("Java", result.description());
    }

    @Test
    void listShouldMapAllEntities() {
        SkillEntity s1 = new SkillEntity();
        s1.setId(1);
        s1.setDescription("Java");
        SkillEntity s2 = new SkillEntity();
        s2.setId(2);
        s2.setDescription("Docker");

        when(skillRepository.findAll()).thenReturn(java.util.List.of(s1, s2));
        when(skillMapper.toDto(s1)).thenReturn(new SkillDto(1, "Java"));
        when(skillMapper.toDto(s2)).thenReturn(new SkillDto(2, "Docker"));

        java.util.List<SkillDto> result = skillService.list();

        assertEquals(2, result.size());
    }

    @Test
    void deleteShouldRemoveWhenSkillExists() {
        SkillEntity skill = new SkillEntity();
        skill.setId(1);
        when(skillRepository.findById(1)).thenReturn(Optional.of(skill));
        doNothing().when(skillRepository).delete(skill);

        skillService.delete(1);
    }

    @Test
    void updateShouldPersistWhenDescriptionIsUnique() {
        SkillEntity existing = new SkillEntity();
        existing.setId(10);
        existing.setDescription("Old");

        SkillEntity saved = new SkillEntity();
        saved.setId(10);
        saved.setDescription("New");

        SkillDto response = new SkillDto(10, "New");

        when(skillRepository.findById(10)).thenReturn(Optional.of(existing));
        when(skillRepository.findByDescriptionIgnoreCase("  New  ")).thenReturn(Optional.empty());
        when(skillRepository.save(existing)).thenReturn(saved);
        when(skillMapper.toDto(saved)).thenReturn(response);

        SkillDto result = skillService.update(10, new SkillDto(null, "  New  "));

        assertEquals("New", result.description());
    }

    @Test
    void deleteShouldThrowWhenSkillDoesNotExist() {
        when(skillRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(SkillNotFoundException.class, () -> skillService.delete(999));
    }
}

