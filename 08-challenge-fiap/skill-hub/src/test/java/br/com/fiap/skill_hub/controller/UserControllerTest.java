package br.com.fiap.skill_hub.controller;

import br.com.fiap.skill_hub.controller.dto.ProfileEnum;
import br.com.fiap.skill_hub.controller.dto.UserDto;
import br.com.fiap.skill_hub.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void shouldCreateUser() {
        UserDto dto = new UserDto(1, "Alice", "alice@email.com", null, ProfileEnum.STUDENT);
        when(userService.create(dto)).thenReturn(dto);

        var response = userController.create(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, response.getBody().id());
    }

    @Test
    void shouldListUsers() {
        when(userService.list()).thenReturn(List.of(new UserDto(1, "Alice", "alice@email.com", null, ProfileEnum.STUDENT)));

        var response = userController.list();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldAddSkills() {
        UserDto dto = new UserDto(1, "Alice", "alice@email.com", null, ProfileEnum.STUDENT);
        when(userService.addSkills(1, List.of(1, 2))).thenReturn(dto);

        var response = userController.addSkills(1, List.of(1, 2));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().id());
    }

    @Test
    void shouldListUserSkills() {
        when(userService.listSkillIds(1)).thenReturn(List.of(10, 20));

        var response = userController.listUserSkills(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }
}


