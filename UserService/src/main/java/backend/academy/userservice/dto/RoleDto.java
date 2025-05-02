package backend.academy.userservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Data
@Builder
public class RoleDto {
    private Long id;
    private String name;
}
