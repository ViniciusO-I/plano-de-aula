package br.com.fiap.skill_hub.controller;

import br.com.fiap.skill_hub.response.UserResponse;
import br.com.fiap.skill_hub.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Retorna o perfil do usuário autenticado
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                userService.getProfile(userDetails.getUsername())
        );
    }

    // Busca usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    // Adiciona skill ao perfil do usuário autenticado
    @PostMapping("/me/skills/{skillId}")
    public ResponseEntity<UserResponse> addSkill(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long skillId) {
        return ResponseEntity.ok(
                userService.addSkill(userDetails.getUsername(), skillId)
        );
    }

    // Remove skill do perfil do usuário autenticado
    @DeleteMapping("/me/skills/{skillId}")
    public ResponseEntity<UserResponse> removeSkill(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long skillId) {
        return ResponseEntity.ok(
                userService.removeSkill(userDetails.getUsername(), skillId)
        );
    }

    // Busca usuários por skill
    @GetMapping("/by-skill/{skillId}")
    public ResponseEntity<List<UserResponse>> findBySkill(
            @PathVariable Long skillId) {
        return ResponseEntity.ok(userService.findBySkill(skillId));
    }
}