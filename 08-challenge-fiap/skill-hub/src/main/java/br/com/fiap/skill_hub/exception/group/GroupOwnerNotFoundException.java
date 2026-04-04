package br.com.fiap.skill_hub.exception.group;

import br.com.fiap.skill_hub.exception.BusinessException;

public class GroupOwnerNotFoundException extends BusinessException {
    
    public GroupOwnerNotFoundException(String message) {
        super(message, "GROUP_OWNER_NOT_FOUND");
    }
}

