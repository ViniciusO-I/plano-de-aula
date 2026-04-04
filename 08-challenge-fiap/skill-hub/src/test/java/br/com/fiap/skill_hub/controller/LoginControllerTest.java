package br.com.fiap.skill_hub.controller;

import br.com.fiap.skill_hub.controller.dto.LoginDto;
import br.com.fiap.skill_hub.controller.dto.RefreshTokenRequestDto;
import br.com.fiap.skill_hub.controller.dto.TokenResponseDto;
import br.com.fiap.skill_hub.service.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    @Test
    void shouldLogin() {
        LoginDto loginDto = new LoginDto("john@email.com", "abc12345");
        TokenResponseDto token = new TokenResponseDto("access", "refresh", "Bearer", 900L);
        when(loginService.login(loginDto)).thenReturn(token);

        var response = loginController.login(loginDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("access", response.getBody().accessToken());
    }

    @Test
    void shouldRefreshToken() {
        RefreshTokenRequestDto request = new RefreshTokenRequestDto("refresh-token-1234567890");
        TokenResponseDto token = new TokenResponseDto("new-access", "new-refresh", "Bearer", 900L);
        when(loginService.refresh(request.refreshToken())).thenReturn(token);

        var response = loginController.refresh(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("new-access", response.getBody().accessToken());
    }

    @Test
    void shouldLogout() {
        RefreshTokenRequestDto request = new RefreshTokenRequestDto("refresh-token-1234567890");
        doNothing().when(loginService).logout(request.refreshToken());

        var response = loginController.logout(request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}

