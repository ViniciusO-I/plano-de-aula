package br.com.fiap.skill_hub.controller;

import br.com.fiap.skill_hub.controller.dto.GroupDto;
import br.com.fiap.skill_hub.response.GroupResponse;
import br.com.fiap.skill_hub.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // Cria novo grupo
    @PostMapping
    public ResponseEntity<GroupResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody GroupDto dto) {
        GroupResponse response = groupService.create(
                userDetails.getUsername(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Lista todos os grupos
    @GetMapping
    public ResponseEntity<List<GroupResponse>> listAll() {
        return ResponseEntity.ok(groupService.listAll());
    }

    // Busca grupo por ID
    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.findById(id));
    }

    // Lista meus grupos
    @GetMapping("/my")
    public ResponseEntity<List<GroupResponse>> myGroups(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                groupService.myGroups(userDetails.getUsername()));
    }

    // Entra em um grupo
    @PostMapping("/{id}/join")
    public ResponseEntity<GroupResponse> join(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(
                groupService.join(userDetails.getUsername(), id));
    }

    // Sai de um grupo
    @DeleteMapping("/{id}/leave")
    public ResponseEntity<Void> leave(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        groupService.leave(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }

    // Exclui o grupo — apenas OWNER
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        groupService.delete(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}