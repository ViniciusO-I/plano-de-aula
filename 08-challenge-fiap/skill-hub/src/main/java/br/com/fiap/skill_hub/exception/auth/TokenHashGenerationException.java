package br.com.fiap.skill_hub.exception.auth;

import br.com.fiap.skill_hub.exception.BusinessException;

public class TokenHashGenerationException extends BusinessException {

    public TokenHashGenerationException(String message, Throwable cause) {
        super(message, "TOKEN_HASH_GENERATION_ERROR", cause);
    }
}

