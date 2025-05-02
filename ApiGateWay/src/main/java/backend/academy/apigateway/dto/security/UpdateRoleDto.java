package backend.academy.apigateway.dto.security;

import lombok.Data;

@Data
public class UpdateRoleDto {
    private String oldRoleName;
    private RoleDto roleDto;
}
