package backend.academy.apigateway.service;


import backend.academy.apigateway.dto.security.RoleDto;
import backend.academy.apigateway.dto.security.UserDto;

import java.util.List;

public interface UserService {

    UserDto registerUser(UserDto user);
    String verify(UserDto user);
    String registerAndVerifyUser(UserDto user);
    UserDto registerWithRole(UserDto user);
    boolean deleteUser(UserDto user);
    UserDto updatePassword(String newPassword, String oldPassword, String username);
    RoleDto getRoles(String name);
    void addRole(RoleDto role, String username);
    void deleteRole(RoleDto role, String username);
    List<UserDto> getAllUsers();
    List<UserDto> getAllUsers(int limit);
    UserDto getUser(String username);
    UserDto requestToCreateUser(UserDto user);
    void userConfirmation(UserDto userDto, String code);
}
