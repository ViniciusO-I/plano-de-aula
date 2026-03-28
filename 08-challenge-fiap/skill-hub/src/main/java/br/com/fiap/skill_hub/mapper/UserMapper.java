package br.com.fiap.skill_hub.mapper;

import br.com.fiap.skill_hub.controller.dto.UserDto;
import br.com.fiap.skill_hub.repository.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(UserDto userDto);
    UserDto toDto(UserEntity userEntity);
    List<UserDto> toListDto(List<UserEntity> entityList);

}
