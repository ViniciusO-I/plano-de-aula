package br.com.fiap.skill_hub.exception.auth;

import br.com.fiap.skill_hub.exception.BusinessException;

public class RefreshTokenMismatchException extends BusinessException {
    
    public RefreshTokenMismatchException(String message) {
        super(message, "REFRESH_TOKEN_MISMATCH");
    }
}

