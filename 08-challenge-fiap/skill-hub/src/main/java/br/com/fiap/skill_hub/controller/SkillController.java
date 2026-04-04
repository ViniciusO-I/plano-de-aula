package br.com.fiap.skill_hub.controller;

import br.com.fiap.skill_hub.api.SkillApi;
import br.com.fiap.skill_hub.controller.dto.SkillDto;
import br.com.fiap.skill_hub.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController implements SkillApi {

	private final SkillService skillService;

	public SkillController(SkillService skillService) {
		this.skillService = skillService;
	}

	@Override
	public ResponseEntity<SkillDto> create(@RequestBody @Valid SkillDto skillDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(skillService.create(skillDto));
	}

	@Override
	public ResponseEntity<List<SkillDto>> list() {
		return ResponseEntity.ok(skillService.list());
	}

	@Override
	public ResponseEntity<SkillDto> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(skillService.findById(id));
	}

	@Override
	public ResponseEntity<SkillDto> update(@PathVariable Integer id, @RequestBody @Valid SkillDto skillDto) {
		return ResponseEntity.ok(skillService.update(id, skillDto));
	}

	@Override
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		skillService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
