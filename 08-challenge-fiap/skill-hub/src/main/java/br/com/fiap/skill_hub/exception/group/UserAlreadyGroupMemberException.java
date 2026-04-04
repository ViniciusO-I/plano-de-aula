package br.com.fiap.skill_hub.exception.group;

import br.com.fiap.skill_hub.exception.BusinessException;

public class UserAlreadyGroupMemberException extends BusinessException {
    
    public UserAlreadyGroupMemberException(String message) {
        super(message, "USER_ALREADY_GROUP_MEMBER");
    }
}

