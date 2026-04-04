package br.com.fiap.skill_hub.controller;

import br.com.fiap.skill_hub.api.UserApi;
import br.com.fiap.skill_hub.controller.dto.UserDto;
import br.com.fiap.skill_hub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController implements UserApi {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserDto userDto) {
        UserDto userDtoResponse = userService.create(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDtoResponse);
    }

    @Override
    public ResponseEntity<List<UserDto>> list() {
        List<UserDto> allUsers = userService.list();
        return ResponseEntity.ok().body(allUsers);
    }

    @Override
    public ResponseEntity<UserDto> addSkills(@PathVariable Integer idUser, @RequestBody @Valid List<Integer> skillIds) {
        return ResponseEntity.ok(userService.addSkills(idUser, skillIds));
    }

    @Override
    public ResponseEntity<List<Integer>> listUserSkills(@PathVariable Integer idUser) {
        return ResponseEntity.ok(userService.listSkillIds(idUser));
    }

}
