package br.com.fiap.skill_hub.controller;

import br.com.fiap.skill_hub.controller.dto.SkillDto;
import br.com.fiap.skill_hub.response.SkillResponse;
import br.com.fiap.skill_hub.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    // Cria nova skill — apenas ADMINISTRATOR
    @PostMapping
    public ResponseEntity<SkillResponse> create(
            @Valid @RequestBody SkillDto dto) {
        SkillResponse response = skillService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Lista todas as skills — qualquer usuário autenticado
    @GetMapping
    public ResponseEntity<List<SkillResponse>> listAll() {
        return ResponseEntity.ok(skillService.listAll());
    }

    // Busca skill por ID
    @GetMapping("/{id}")
    public ResponseEntity<SkillResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.findById(id));
    }
}