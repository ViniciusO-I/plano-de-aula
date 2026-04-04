package br.com.fiap.skill_hub.api;

import br.com.fiap.skill_hub.controller.dto.GroupDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Groups", description = "Endpoints para gerenciar grupos e entrada por skill")
public interface GroupApi {

    @PostMapping
    @Operation(summary = "Criar grupo", description = "Cria grupo com skills obrigatórias e owner")
    ResponseEntity<GroupDto> create(@RequestBody @Valid GroupDto groupDto);

    @GetMapping
    @Operation(summary = "Listar grupos")
    ResponseEntity<List<GroupDto>> list();

    @GetMapping("/{id}")
    @Operation(summary = "Buscar grupo por ID")
    ResponseEntity<GroupDto> findById(@PathVariable Integer id);

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar grupo")
    ResponseEntity<GroupDto> update(@PathVariable Integer id, @RequestBody @Valid GroupDto groupDto);

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir grupo")
    ResponseEntity<Void> delete(@PathVariable Integer id);

    @PostMapping("/{groupId}/join/{userId}")
    @Operation(summary = "Entrar no grupo", description = "Permite entrada apenas para usuários com as skills mínimas exigidas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário entrou no grupo"),
            @ApiResponse(responseCode = "400", description = "Sem skill mínima ou sem vagas")
    })
    ResponseEntity<GroupDto> join(@PathVariable Integer groupId, @PathVariable Integer userId);
}

