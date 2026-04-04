package br.com.fiap.skill_hub.controller;

import br.com.fiap.skill_hub.api.GroupApi;
import br.com.fiap.skill_hub.controller.dto.GroupDto;
import br.com.fiap.skill_hub.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController implements GroupApi {

	private final GroupService groupService;

	public GroupController(GroupService groupService) {
		this.groupService = groupService;
	}

	@Override
	public ResponseEntity<GroupDto> create(@RequestBody @Valid GroupDto groupDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(groupService.create(groupDto));
	}

	@Override
	public ResponseEntity<List<GroupDto>> list() {
		return ResponseEntity.ok(groupService.list());
	}

	@Override
	public ResponseEntity<GroupDto> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(groupService.findById(id));
	}

	@Override
	public ResponseEntity<GroupDto> update(@PathVariable Integer id, @RequestBody @Valid GroupDto groupDto) {
		return ResponseEntity.ok(groupService.update(id, groupDto));
	}

	@Override
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		groupService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<GroupDto> join(@PathVariable Integer groupId, @PathVariable Integer userId) {
		return ResponseEntity.ok(groupService.join(groupId, userId));
	}
}
