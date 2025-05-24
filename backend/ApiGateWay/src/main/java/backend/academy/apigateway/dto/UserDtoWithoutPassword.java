package backend.academy.apigateway.dto;

import backend.academy.apigateway.dto.security.RoleDto;
import backend.academy.apigateway.dto.security.UserDto;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserDtoWithoutPassword {
    private Long id;
    private String username;
    private String email;
    private RoleDto role;
    private boolean isGameMaster;
}
