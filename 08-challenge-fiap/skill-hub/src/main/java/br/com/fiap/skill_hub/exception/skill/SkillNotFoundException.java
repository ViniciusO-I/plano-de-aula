package br.com.fiap.skill_hub.exception.skill;

import br.com.fiap.skill_hub.exception.BusinessException;

public class SkillNotFoundException extends BusinessException {
    
    public SkillNotFoundException(String message) {
        super(message, "SKILL_NOT_FOUND");
    }
}

