package backend.academy.apigateway.dto.security;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddRoleDto {

    private RoleDto role;
    private String username;
}
