package br.com.fiap.skill_hub.exception.group;

import br.com.fiap.skill_hub.exception.BusinessException;

public class GroupNoAvailableSeatsException extends BusinessException {
    
    public GroupNoAvailableSeatsException(String message) {
        super(message, "GROUP_NO_AVAILABLE_SEATS");
    }
}

