package br.com.fiap.skill_hub.controller;

import br.com.fiap.skill_hub.controller.dto.SkillDto;
import br.com.fiap.skill_hub.service.SkillService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillControllerTest {

    @Mock
    private SkillService skillService;

    @InjectMocks
    private SkillController skillController;

    @Test
    void shouldCreateSkill() {
        SkillDto dto = new SkillDto(1, "Java");
        when(skillService.create(dto)).thenReturn(dto);

        var response = skillController.create(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Java", response.getBody().description());
    }

    @Test
    void shouldListSkills() {
        when(skillService.list()).thenReturn(List.of(new SkillDto(1, "Java")));

        var response = skillController.list();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldFindSkillById() {
        when(skillService.findById(1)).thenReturn(new SkillDto(1, "Java"));

        var response = skillController.findById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().id());
    }

    @Test
    void shouldUpdateSkill() {
        when(skillService.update(1, new SkillDto(null, "Spring"))).thenReturn(new SkillDto(1, "Spring"));

        var response = skillController.update(1, new SkillDto(null, "Spring"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Spring", response.getBody().description());
    }

    @Test
    void shouldDeleteSkill() {
        doNothing().when(skillService).delete(1);

        var response = skillController.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}

