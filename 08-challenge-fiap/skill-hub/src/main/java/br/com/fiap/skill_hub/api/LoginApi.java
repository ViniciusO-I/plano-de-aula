package br.com.fiap.skill_hub.api;

import br.com.fiap.skill_hub.controller.dto.LoginDto;
import br.com.fiap.skill_hub.controller.dto.RefreshTokenRequestDto;
import br.com.fiap.skill_hub.controller.dto.TokenResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "Endpoints de autenticação com JWT")
public interface LoginApi {

    @PostMapping
    @Operation(summary = "Autenticar usuário", description = "Retorna access token e refresh token")
    ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginDto loginDto);

    @PostMapping("/refresh")
    @Operation(summary = "Renovar tokens", description = "Gera novo access token e refresh token")
    ResponseEntity<TokenResponseDto> refresh(@RequestBody @Valid RefreshTokenRequestDto request);

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Revoga o refresh token")
    ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenRequestDto request);
}

