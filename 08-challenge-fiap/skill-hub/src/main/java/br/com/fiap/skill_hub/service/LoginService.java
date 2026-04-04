package br.com.fiap.skill_hub.service;

import br.com.fiap.skill_hub.controller.dto.LoginDto;
import br.com.fiap.skill_hub.controller.dto.TokenResponseDto;
import br.com.fiap.skill_hub.exception.auth.InvalidRefreshTokenException;
import br.com.fiap.skill_hub.exception.auth.RefreshTokenMismatchException;
import br.com.fiap.skill_hub.exception.auth.RefreshTokenNotFoundException;
import br.com.fiap.skill_hub.exception.auth.TokenHashGenerationException;
import br.com.fiap.skill_hub.exception.user.UserNotFoundException;
import br.com.fiap.skill_hub.repository.RefreshTokenRepository;
import br.com.fiap.skill_hub.repository.UserRepository;
import br.com.fiap.skill_hub.repository.entities.RefreshTokenEntity;
import br.com.fiap.skill_hub.repository.entities.UserEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public LoginService(AuthenticationManager authenticationManager,
                        UserRepository userRepository,
                        RefreshTokenRepository refreshTokenRepository,
                        JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    public TokenResponseDto login(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password())
        );

        UserEntity user = userRepository.findByEmail(loginDto.email())
                .orElseThrow(() -> new UserNotFoundException("User not found during login"));

        revokeAllActiveRefreshTokens(user);

        String accessToken = jwtService.generateAccessToken(user);
        String jti = UUID.randomUUID().toString();
        String refreshToken = jwtService.generateRefreshToken(user, jti);

        saveRefreshToken(user, refreshToken, jti);

        return new TokenResponseDto(accessToken, refreshToken, "Bearer", 900L);
    }

    public TokenResponseDto refresh(String refreshToken) {
        try {
            String email = jwtService.extractUsername(refreshToken);
            String jti = jwtService.extractJti(refreshToken);

            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User not found during refresh"));

            if (!jwtService.isRefreshTokenValid(refreshToken, user)) {
                throw new InvalidRefreshTokenException("Refresh token is invalid or expired");
            }

            RefreshTokenEntity storedToken = refreshTokenRepository.findByJtiAndRevokedFalse(jti)
                    .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found or revoked"));

            if (!storedToken.getTokenHash().equals(hashToken(refreshToken))) {
                throw new RefreshTokenMismatchException("Refresh token hash mismatch");
            }

            storedToken.setRevoked(true);

            String newAccessToken = jwtService.generateAccessToken(user);
            String newJti = UUID.randomUUID().toString();
            String newRefreshToken = jwtService.generateRefreshToken(user, newJti);

            saveRefreshToken(user, newRefreshToken, newJti);

            return new TokenResponseDto(newAccessToken, newRefreshToken, "Bearer", 900L);
        } catch (InvalidRefreshTokenException | RefreshTokenNotFoundException |
                 RefreshTokenMismatchException | UserNotFoundException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            throw new InvalidRefreshTokenException("Refresh token is invalid or malformed");
        }
    }

    public void logout(String refreshToken) {
        try {
            String jti = jwtService.extractJti(refreshToken);
            RefreshTokenEntity storedToken = refreshTokenRepository.findByJtiAndRevokedFalse(jti)
                    .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found or revoked"));
            storedToken.setRevoked(true);
        } catch (RefreshTokenNotFoundException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            throw new InvalidRefreshTokenException("Refresh token is invalid or malformed");
        }
    }

    private void saveRefreshToken(UserEntity user, String refreshToken, String jti) {
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setUser(user);
        entity.setJti(jti);
        entity.setTokenHash(hashToken(refreshToken));
        entity.setExpiresAt(jwtService.extractAllClaims(refreshToken).getExpiration().toInstant());
        entity.setRevoked(false);
        refreshTokenRepository.save(entity);
    }

    private void revokeAllActiveRefreshTokens(UserEntity user) {
        List<RefreshTokenEntity> activeTokens = refreshTokenRepository.findAllByUserIdAndRevokedFalse(user.getId());
        activeTokens.forEach(token -> token.setRevoked(true));
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new TokenHashGenerationException("Error generating token hash", ex);
        }
    }
}

