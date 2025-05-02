package backend.academy.apigateway.service;


import backend.academy.apigateway.dto.security.RoleDto;
import backend.academy.apigateway.dto.security.UserDto;

import java.util.List;

public interface RoleService {


    List<RoleDto> getAllRoles();
    List<UserDto> findUsersByRole(RoleDto role);
}
