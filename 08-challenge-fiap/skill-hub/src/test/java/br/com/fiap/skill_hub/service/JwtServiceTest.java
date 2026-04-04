package br.com.fiap.skill_hub.service;

import br.com.fiap.skill_hub.controller.dto.ProfileEnum;
import br.com.fiap.skill_hub.repository.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "MDEyMzQ1Njc4OUFCQ0RFRjAxMjM0NTY3ODlBQkNERUY=");
        ReflectionTestUtils.setField(jwtService, "issuer", "skill-hub-api");
        ReflectionTestUtils.setField(jwtService, "accessExpirationMs", 900000L);
        ReflectionTestUtils.setField(jwtService, "refreshExpirationMs", 604800000L);
    }

    @Test
    void shouldGenerateAndValidateAccessToken() {
        UserEntity user = new UserEntity();
        user.setEmail("john@email.com");
        user.setProfile(ProfileEnum.STUDENT);

        String accessToken = jwtService.generateAccessToken(user);

        assertNotNull(accessToken);
        assertEquals("john@email.com", jwtService.extractUsername(accessToken));
        assertEquals("ACCESS", jwtService.extractType(accessToken));
        assertTrue(jwtService.isAccessTokenValid(accessToken, user));
        assertFalse(jwtService.isRefreshTokenValid(accessToken, user));
    }

    @Test
    void shouldGenerateAndValidateRefreshTokenWithJti() {
        UserEntity user = new UserEntity();
        user.setEmail("john@email.com");
        user.setProfile(ProfileEnum.STUDENT);

        String refreshToken = jwtService.generateRefreshToken(user, "jti-123");

        assertNotNull(refreshToken);
        assertEquals("john@email.com", jwtService.extractUsername(refreshToken));
        assertEquals("REFRESH", jwtService.extractType(refreshToken));
        assertEquals("jti-123", jwtService.extractJti(refreshToken));
        assertTrue(jwtService.isRefreshTokenValid(refreshToken, user));
        assertFalse(jwtService.isAccessTokenValid(refreshToken, user));
    }
}

