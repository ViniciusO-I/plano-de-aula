package br.com.fiap.skill_hub.service;

import br.com.fiap.skill_hub.controller.dto.LoginDto;
import br.com.fiap.skill_hub.controller.dto.ProfileEnum;
import br.com.fiap.skill_hub.controller.dto.TokenResponseDto;
import br.com.fiap.skill_hub.exception.auth.InvalidRefreshTokenException;
import br.com.fiap.skill_hub.exception.auth.RefreshTokenNotFoundException;
import br.com.fiap.skill_hub.exception.auth.RefreshTokenMismatchException;
import br.com.fiap.skill_hub.exception.user.UserNotFoundException;
import br.com.fiap.skill_hub.repository.RefreshTokenRepository;
import br.com.fiap.skill_hub.repository.UserRepository;
import br.com.fiap.skill_hub.repository.entities.RefreshTokenEntity;
import br.com.fiap.skill_hub.repository.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.Date;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private LoginService loginService;

    @Test
    void loginShouldReturnTokensWhenCredentialsAreValid() {
        LoginDto loginDto = new LoginDto("john@email.com", "abc12345");

        UserEntity user = new UserEntity();
        user.setId(1);
        user.setEmail("john@email.com");
        user.setProfile(ProfileEnum.STUDENT);

        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userRepository.findByEmail("john@email.com")).thenReturn(Optional.of(user));
        when(refreshTokenRepository.findAllByUserIdAndRevokedFalse(1)).thenReturn(List.of());
        when(jwtService.generateAccessToken(user)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(any(UserEntity.class), any(String.class))).thenReturn("refresh-token");
        when(jwtService.extractAllClaims("refresh-token")).thenReturn(io.jsonwebtoken.Jwts.claims()
                .subject("john@email.com")
                .expiration(Date.from(Instant.now().plusSeconds(3600)))
                .build());
        when(refreshTokenRepository.save(any(RefreshTokenEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        TokenResponseDto response = loginService.login(loginDto);

        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
        assertEquals("Bearer", response.tokenType());
    }

    @Test
    void refreshShouldThrowInvalidRefreshTokenWhenJwtIsMalformed() {
        when(jwtService.extractUsername("bad-token")).thenThrow(new RuntimeException("invalid token"));

        assertThrows(InvalidRefreshTokenException.class, () -> loginService.refresh("bad-token"));
    }

    @Test
    void logoutShouldThrowWhenRefreshTokenNotFound() {
        when(jwtService.extractJti("token-1")).thenReturn("jti-1");
        when(refreshTokenRepository.findByJtiAndRevokedFalse("jti-1")).thenReturn(Optional.empty());

        assertThrows(RefreshTokenNotFoundException.class, () -> loginService.logout("token-1"));
    }

    @Test
    void refreshShouldReturnNewTokensWhenRefreshIsValid() throws Exception {
        String oldRefresh = "old-refresh-token";
        String oldJti = "old-jti";
        String newJti = "new-jti";

        UserEntity user = new UserEntity();
        user.setId(1);
        user.setEmail("john@email.com");
        user.setProfile(ProfileEnum.STUDENT);

        RefreshTokenEntity stored = new RefreshTokenEntity();
        stored.setJti(oldJti);
        stored.setRevoked(false);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String oldHash = HexFormat.of().formatHex(digest.digest(oldRefresh.getBytes(StandardCharsets.UTF_8)));
        stored.setTokenHash(oldHash);

        when(jwtService.extractUsername(oldRefresh)).thenReturn("john@email.com");
        when(jwtService.extractJti(oldRefresh)).thenReturn(oldJti);
        when(userRepository.findByEmail("john@email.com")).thenReturn(Optional.of(user));
        when(jwtService.isRefreshTokenValid(oldRefresh, user)).thenReturn(true);
        when(refreshTokenRepository.findByJtiAndRevokedFalse(oldJti)).thenReturn(Optional.of(stored));
        when(jwtService.generateAccessToken(user)).thenReturn("new-access-token");
        when(jwtService.generateRefreshToken(any(UserEntity.class), any(String.class))).thenReturn("new-refresh-token");
        when(jwtService.extractAllClaims("new-refresh-token")).thenReturn(io.jsonwebtoken.Jwts.claims()
                .subject("john@email.com")
                .expiration(Date.from(Instant.now().plusSeconds(3600)))
                .build());
        when(refreshTokenRepository.save(any(RefreshTokenEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        TokenResponseDto response = loginService.refresh(oldRefresh);

        assertEquals("new-access-token", response.accessToken());
        assertEquals("new-refresh-token", response.refreshToken());
    }

    @Test
    void refreshShouldThrowMismatchWhenHashIsDifferent() {
        String token = "old-refresh-token";

        UserEntity user = new UserEntity();
        user.setEmail("john@email.com");

        RefreshTokenEntity stored = new RefreshTokenEntity();
        stored.setTokenHash("different-hash");

        when(jwtService.extractUsername(token)).thenReturn("john@email.com");
        when(jwtService.extractJti(token)).thenReturn("old-jti");
        when(userRepository.findByEmail("john@email.com")).thenReturn(Optional.of(user));
        when(jwtService.isRefreshTokenValid(token, user)).thenReturn(true);
        when(refreshTokenRepository.findByJtiAndRevokedFalse("old-jti")).thenReturn(Optional.of(stored));

        assertThrows(RefreshTokenMismatchException.class, () -> loginService.refresh(token));
    }

    @Test
    void logoutShouldThrowInvalidRefreshTokenWhenMalformed() {
        when(jwtService.extractJti("malformed")).thenThrow(new RuntimeException("bad token"));

        assertThrows(InvalidRefreshTokenException.class, () -> loginService.logout("malformed"));
    }

    @Test
    void refreshShouldThrowWhenUserFromTokenDoesNotExist() {
        when(jwtService.extractUsername("refresh-token")).thenReturn("ghost@email.com");
        when(jwtService.extractJti("refresh-token")).thenReturn("jti-1");
        when(userRepository.findByEmail("ghost@email.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> loginService.refresh("refresh-token"));
    }

    @Test
    void refreshShouldThrowWhenTokenIsNotValid() {
        UserEntity user = new UserEntity();
        user.setEmail("john@email.com");

        when(jwtService.extractUsername("refresh-token")).thenReturn("john@email.com");
        when(jwtService.extractJti("refresh-token")).thenReturn("jti-1");
        when(userRepository.findByEmail("john@email.com")).thenReturn(Optional.of(user));
        when(jwtService.isRefreshTokenValid("refresh-token", user)).thenReturn(false);

        assertThrows(InvalidRefreshTokenException.class, () -> loginService.refresh("refresh-token"));
    }

    @Test
    void refreshShouldThrowWhenStoredRefreshTokenIsMissing() {
        UserEntity user = new UserEntity();
        user.setEmail("john@email.com");

        when(jwtService.extractUsername("refresh-token")).thenReturn("john@email.com");
        when(jwtService.extractJti("refresh-token")).thenReturn("jti-1");
        when(userRepository.findByEmail("john@email.com")).thenReturn(Optional.of(user));
        when(jwtService.isRefreshTokenValid("refresh-token", user)).thenReturn(true);
        when(refreshTokenRepository.findByJtiAndRevokedFalse("jti-1")).thenReturn(Optional.empty());

        assertThrows(RefreshTokenNotFoundException.class, () -> loginService.refresh("refresh-token"));
    }

    @Test
    void logoutShouldRevokeTokenWhenFound() {
        RefreshTokenEntity token = new RefreshTokenEntity();
        token.setRevoked(false);

        when(jwtService.extractJti("token-ok")).thenReturn("jti-ok");
        when(refreshTokenRepository.findByJtiAndRevokedFalse("jti-ok")).thenReturn(Optional.of(token));

        loginService.logout("token-ok");

        assertEquals(true, token.getRevoked());
    }
}

