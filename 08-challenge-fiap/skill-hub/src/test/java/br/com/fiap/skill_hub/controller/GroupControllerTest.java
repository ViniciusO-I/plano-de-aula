package br.com.fiap.skill_hub.controller;

import br.com.fiap.skill_hub.controller.dto.GroupDto;
import br.com.fiap.skill_hub.controller.dto.SkillDto;
import br.com.fiap.skill_hub.service.GroupService;
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
class GroupControllerTest {

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupController groupController;

    private GroupDto sampleGroup() {
        return new GroupDto(1, "Grupo A", 5, 1, List.of(), List.of(new SkillDto(1, "Java")));
    }

    @Test
    void shouldCreateGroup() {
        GroupDto dto = sampleGroup();
        when(groupService.create(dto)).thenReturn(dto);

        var response = groupController.create(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, response.getBody().id());
    }

    @Test
    void shouldListGroups() {
        when(groupService.list()).thenReturn(List.of(sampleGroup()));

        var response = groupController.list();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldFindGroupById() {
        when(groupService.findById(1)).thenReturn(sampleGroup());

        var response = groupController.findById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().id());
    }

    @Test
    void shouldUpdateGroup() {
        GroupDto dto = sampleGroup();
        when(groupService.update(1, dto)).thenReturn(dto);

        var response = groupController.update(1, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Grupo A", response.getBody().description());
    }

    @Test
    void shouldDeleteGroup() {
        doNothing().when(groupService).delete(1);

        var response = groupController.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldJoinGroup() {
        GroupDto dto = sampleGroup();
        when(groupService.join(1, 2)).thenReturn(dto);

        var response = groupController.join(1, 2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().id());
    }
}

