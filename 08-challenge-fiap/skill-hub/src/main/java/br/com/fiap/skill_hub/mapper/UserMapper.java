package br.com.fiap.skill_hub.mapper;

import br.com.fiap.skill_hub.controller.dto.UserDto;
import br.com.fiap.skill_hub.repository.entities.UserEntity;
import org.springframework.stereotype.Component;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;


// vou precisar mapear na mao!

import java.util.List;

@Component
public class UserMapper {

    public UserEntity toEntity(UserDto dto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(dto.getName());
        userEntity.setEmail(dto.getEmail());
        userEntity.setPassword(dto.getPassword());
        userEntity.setProfile(dto.getProfile());

        return userEntity;


    }

    public UserDto toDto(UserEntity entity){
        UserDto dtoEntity = new UserDto();
        dtoEntity.setId(entity.getId());
        dtoEntity.setName(entity.getName());
        dtoEntity.setEmail(entity.getEmail());
        dtoEntity.setProfile(entity.getProfile());

        return dtoEntity;

    }

    public  List<UserDto> toListDto(List<UserEntity> entities){
        return  entities.stream().map(this::toDto).toList();
    }
}


//@Mapper(componentModel = "spring")
//public interface UserMapper {
//
//    UserEntity toEntity(UserDto userDto);
//    UserDto toDto(UserEntity userEntity);
//    List<UserDto> toListDto(List<UserEntity> entityList);
//
//
//
//}
