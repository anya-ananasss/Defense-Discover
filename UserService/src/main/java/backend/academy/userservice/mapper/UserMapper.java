package backend.academy.userservice.mapper;

import backend.academy.userservice.dto.UserDto;
import backend.academy.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User entity);

    User toEntity(UserDto dto);
}
