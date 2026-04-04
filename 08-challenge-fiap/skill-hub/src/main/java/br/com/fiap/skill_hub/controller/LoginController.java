package br.com.fiap.skill_hub.controller;

import br.com.fiap.skill_hub.api.LoginApi;
import br.com.fiap.skill_hub.controller.dto.LoginDto;
import br.com.fiap.skill_hub.controller.dto.RefreshTokenRequestDto;
import br.com.fiap.skill_hub.controller.dto.TokenResponseDto;
import br.com.fiap.skill_hub.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController implements LoginApi {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    @PostMapping
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginDto loginDto) {
        return ResponseEntity.ok(loginService.login(loginDto));
    }

    @Override
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@RequestBody @Valid RefreshTokenRequestDto request) {
        return ResponseEntity.ok(loginService.refresh(request.refreshToken()));
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenRequestDto request) {
        loginService.logout(request.refreshToken());
        return ResponseEntity.noContent().build();
    }
}
