package br.com.fiap.skill_hub.exception.skill;

import br.com.fiap.skill_hub.exception.BusinessException;

public class SkillDescriptionAlreadyInUseException extends BusinessException {
    
    public SkillDescriptionAlreadyInUseException(String message) {
        super(message, "SKILL_DESCRIPTION_ALREADY_IN_USE");
    }
}

