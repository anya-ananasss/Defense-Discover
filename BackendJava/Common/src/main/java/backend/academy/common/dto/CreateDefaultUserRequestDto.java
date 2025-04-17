package backend.academy.common.dto;

public record CreateDefaultUserRequestDto(
        String username,
        String mail,
        String password
) {
}
