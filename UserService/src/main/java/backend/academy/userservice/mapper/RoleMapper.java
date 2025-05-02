package backend.academy.userservice.mapper;

import backend.academy.userservice.dto.RoleDto;
import backend.academy.userservice.dto.UserDto;
import backend.academy.userservice.model.Role;
import backend.academy.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDto toDto(Role entity);

    Role toEntity(RoleDto dto);
}
