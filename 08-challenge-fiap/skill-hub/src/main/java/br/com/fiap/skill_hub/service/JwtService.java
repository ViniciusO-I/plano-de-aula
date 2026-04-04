package br.com.fiap.skill_hub.service;

import br.com.fiap.skill_hub.repository.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${security.jwt.access-token-expiration-ms}")
    private long accessExpirationMs;

    @Value("${security.jwt.refresh-token-expiration-ms}")
    private long refreshExpirationMs;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(UserEntity user) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(user.getEmail())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(accessExpirationMs)))
                .claim("type", "ACCESS")
                .claim("profile", user.getProfile().name())
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(UserEntity user, String jti) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(user.getEmail())
                .id(jti)
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(refreshExpirationMs)))
                .claim("type", "REFRESH")
                .signWith(getSigningKey())
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractType(String token) {
        return extractAllClaims(token).get("type", String.class);
    }

    public String extractJti(String token) {
        return extractAllClaims(token).getId();
    }

    public boolean isAccessTokenValid(String token, UserEntity user) {
        Claims claims = extractAllClaims(token);
        return "ACCESS".equals(claims.get("type", String.class))
                && claims.getSubject().equals(user.getEmail())
                && claims.getExpiration().after(new Date());
    }

    public boolean isRefreshTokenValid(String token, UserEntity user) {
        Claims claims = extractAllClaims(token);
        return "REFRESH".equals(claims.get("type", String.class))
                && claims.getSubject().equals(user.getEmail())
                && claims.getExpiration().after(new Date());
    }
}


