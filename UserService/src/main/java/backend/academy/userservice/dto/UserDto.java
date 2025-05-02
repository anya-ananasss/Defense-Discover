package backend.academy.userservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Data
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private RoleDto role;
}
