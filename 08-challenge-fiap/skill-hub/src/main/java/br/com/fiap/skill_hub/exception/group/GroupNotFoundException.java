package br.com.fiap.skill_hub.exception.group;

import br.com.fiap.skill_hub.exception.BusinessException;

public class GroupNotFoundException extends BusinessException {
    
    public GroupNotFoundException(String message) {
        super(message, "GROUP_NOT_FOUND");
    }
}

