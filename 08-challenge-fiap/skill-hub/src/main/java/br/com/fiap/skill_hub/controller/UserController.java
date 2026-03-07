package br.com.fiap.skill_hub.controller;

import br.com.fiap.skill_hub.controller.dto.LoginDto;
import br.com.fiap.skill_hub.controller.dto.UserDto;
import br.com.fiap.skill_hub.service.UserService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        UserDto userDtoResponse = userService.create(userDto);
        return ResponseEntity.ok().body(userDtoResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginDto loginDto) {
        UserDto userDtoResponse = userService.login(loginDto);
        return ResponseEntity.ok().body(userDtoResponse);
    }

    @PutMapping(value="/{idUser}/skills", consumes = "application/json")
    public ResponseEntity<UserDto> addSkill(@PathVariable Integer idUser, @RequestBody List<Integer> idSkills) {
        UserDto userDtoResponse = userService.addSkill(idUser, idSkills);
        return ResponseEntity.ok().body(userDtoResponse);

    }

}
