package br.com.fiap.skill_hub.mapper;

import br.com.fiap.skill_hub.controller.dto.GroupDto;
import br.com.fiap.skill_hub.repository.entities.GroupEntity;
import br.com.fiap.skill_hub.repository.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, SkillMapper.class})
public interface GroupMapper {

    @Mapping(target = "ownerId", source = "owner.id")
    GroupDto toDto(GroupEntity groupEntity);

    @Mapping(target = "owner", source = "ownerId")
    GroupEntity toEntity(GroupDto groupDto);

    List<GroupDto> toListDto(List<GroupEntity> entityList);

    default UserEntity map(Integer ownerId) {
        if (ownerId == null) {
            return null;
        }
        UserEntity owner = new UserEntity();
        owner.setId(ownerId);
        return owner;
    }
}

