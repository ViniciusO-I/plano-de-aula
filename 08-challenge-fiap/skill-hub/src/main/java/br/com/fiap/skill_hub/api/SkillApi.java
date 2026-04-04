package br.com.fiap.skill_hub.api;

import br.com.fiap.skill_hub.controller.dto.SkillDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

@Tag(name = "Skills", description = "Endpoints para gerenciar skills")
public interface SkillApi {

    @PostMapping
    @Operation(summary = "Criar skill", description = "Cria uma nova skill no catálogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skill criada com sucesso", content = @Content(schema = @Schema(implementation = SkillDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Skill já cadastrada")
    })
    ResponseEntity<SkillDto> create(@RequestBody @Valid SkillDto skillDto);

    @GetMapping
    @Operation(summary = "Listar skills", description = "Lista todas as skills cadastradas")
    ResponseEntity<List<SkillDto>> list();

    @GetMapping("/{id}")
    @Operation(summary = "Buscar skill por ID")
    ResponseEntity<SkillDto> findById(@PathVariable Integer id);

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar skill", description = "Atualiza descrição da skill")
    ResponseEntity<SkillDto> update(@PathVariable Integer id, @RequestBody @Valid SkillDto skillDto);

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir skill")
    ResponseEntity<Void> delete(@PathVariable Integer id);
}

