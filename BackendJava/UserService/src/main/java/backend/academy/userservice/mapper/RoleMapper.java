package backend.academy.userservice.mapper;

import backend.academy.userservice.dto.RoleDto;
import backend.academy.userservice.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {


    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDto roleToRoleDTO(Role role);

    Role roleDTOToRole(RoleDto roleDTO);

}
