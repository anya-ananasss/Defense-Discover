package backend.academy.apigateway.service.impl;


import backend.academy.apigateway.dto.security.RoleDto;
import backend.academy.apigateway.dto.security.UserDto;
import backend.academy.apigateway.exception.RoleDoesntExist;
import backend.academy.apigateway.client.RoleClient;
import backend.academy.apigateway.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleClient roleClient;

    public RoleServiceImpl(RoleClient roleClient) {
        this.roleClient = roleClient;
    }

    @Override
    public List<RoleDto> getAllRoles() {
        return roleClient.getAllRoles();
    }

    @Override

    public List<UserDto> findUsersByRole(RoleDto role) {
        //todo
        return null;
    }
}
