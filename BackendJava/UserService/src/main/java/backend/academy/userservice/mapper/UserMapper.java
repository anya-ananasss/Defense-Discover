package backend.academy.userservice.mapper;

import backend.academy.userservice.dto.RoleDto;
import backend.academy.userservice.dto.UserDto;
import backend.academy.userservice.model.Role;
import backend.academy.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDTO(User user);

    User userDTOToUser(UserDto userDTO);

}
