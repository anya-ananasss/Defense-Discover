package backend.academy.userservice.dto;

public record UserDto(Long userId,String userName, RoleDto roleDto) {}