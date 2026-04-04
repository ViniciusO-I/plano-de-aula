package br.com.fiap.skill_hub.api;

import br.com.fiap.skill_hub.controller.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(
    name = "Users",
    description = "Endpoints para gerenciar usuários da aplicação Skill Hub"
)
public interface UserApi {

    @PostMapping
    @Operation(
        summary = "Criar novo usuário",
        description = "Cria um novo usuário no sistema com os dados fornecidos (name, email, password, profile)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor"
        )
    })
    ResponseEntity<UserDto> create(
        @RequestBody @Valid UserDto userDto
    );

    @GetMapping
    @Operation(
        summary = "Listar todos os usuários",
        description = "Retorna uma lista com todos os usuários cadastrados no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuários obtida com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor"
        )
    })
    ResponseEntity<List<UserDto>> list();

    @PutMapping("/{idUser}/skills")
    @Operation(
        summary = "Associar skills ao usuário",
        description = "Substitui a lista de skills do usuário com os IDs enviados"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Skills associadas com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário ou skill não encontrada")
    })
    ResponseEntity<UserDto> addSkills(
        @PathVariable Integer idUser,
        @RequestBody @Valid List<Integer> skillIds
    );

    @GetMapping("/{idUser}/skills")
    @Operation(
        summary = "Listar skills do usuário",
        description = "Retorna os IDs das skills associadas ao usuário"
    )
    ResponseEntity<List<Integer>> listUserSkills(@PathVariable Integer idUser);
}

