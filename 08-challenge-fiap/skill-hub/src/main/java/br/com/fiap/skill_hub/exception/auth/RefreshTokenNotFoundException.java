package br.com.fiap.skill_hub.exception.auth;

import br.com.fiap.skill_hub.exception.BusinessException;

public class RefreshTokenNotFoundException extends BusinessException {
    
    public RefreshTokenNotFoundException(String message) {
        super(message, "REFRESH_TOKEN_NOT_FOUND");
    }
}

