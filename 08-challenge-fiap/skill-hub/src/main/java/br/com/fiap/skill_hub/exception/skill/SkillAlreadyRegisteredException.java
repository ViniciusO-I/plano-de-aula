package br.com.fiap.skill_hub.exception.skill;

import br.com.fiap.skill_hub.exception.BusinessException;

public class SkillAlreadyRegisteredException extends BusinessException {
    
    public SkillAlreadyRegisteredException(String message) {
        super(message, "SKILL_ALREADY_REGISTERED");
    }
}

