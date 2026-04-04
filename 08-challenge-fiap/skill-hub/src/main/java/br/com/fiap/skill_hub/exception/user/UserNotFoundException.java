package br.com.fiap.skill_hub.exception.user;

import br.com.fiap.skill_hub.exception.BusinessException;

public class UserNotFoundException extends BusinessException {
    
    public UserNotFoundException(String message) {
        super(message, "USER_NOT_FOUND");
    }
}

