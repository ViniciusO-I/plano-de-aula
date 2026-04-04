package br.com.fiap.skill_hub.exception.auth;

import br.com.fiap.skill_hub.exception.BusinessException;

public class InvalidRefreshTokenException extends BusinessException {
    
    public InvalidRefreshTokenException(String message) {
        super(message, "INVALID_REFRESH_TOKEN");
    }
}

