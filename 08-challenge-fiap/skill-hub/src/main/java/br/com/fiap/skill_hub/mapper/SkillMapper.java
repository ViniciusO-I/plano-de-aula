package br.com.fiap.skill_hub.mapper;

import br.com.fiap.skill_hub.controller.dto.SkillDto;
import br.com.fiap.skill_hub.repository.entities.SkillEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    SkillEntity toEntity(SkillDto skillDto);

    SkillDto toDto(SkillEntity skillEntity);

    List<SkillDto> toListDto(List<SkillEntity> entityList);
}

