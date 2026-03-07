package br.com.fiap.skill_hub.controller;

import br.com.fiap.skill_hub.controller.dto.LoginDto;
import br.com.fiap.skill_hub.controller.dto.UserDto;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequestMapping("/api/users")
public class UserController {

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        return ResponseEntity.ok().body(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok().build();

    }

    @PutMapping(value="/{idUser}/skills", consumes = "application/json")
    public ResponseEntity<UserDto> addSkill(@PathVariable Integer idUser, @RequestBody List<Integer> idSkills) {
        return ResponseEntity.ok().body(null);

    }

}
