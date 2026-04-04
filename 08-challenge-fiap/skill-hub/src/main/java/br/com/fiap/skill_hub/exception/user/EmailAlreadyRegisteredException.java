package br.com.fiap.skill_hub.exception.user;

import br.com.fiap.skill_hub.exception.BusinessException;

public class EmailAlreadyRegisteredException extends BusinessException {
    
    public EmailAlreadyRegisteredException(String message) {
        super(message, "EMAIL_ALREADY_REGISTERED");
    }
}

