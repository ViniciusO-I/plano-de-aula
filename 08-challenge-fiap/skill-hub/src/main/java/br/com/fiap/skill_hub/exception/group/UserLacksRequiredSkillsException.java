package br.com.fiap.skill_hub.exception.group;

import br.com.fiap.skill_hub.exception.BusinessException;

public class UserLacksRequiredSkillsException extends BusinessException {
    
    public UserLacksRequiredSkillsException(String message) {
        super(message, "USER_LACKS_REQUIRED_SKILLS");
    }
}

