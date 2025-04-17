package backend.academy.userservice.servcie;

import backend.academy.common.dto.CreateDefaultUserRequestDto;
import backend.academy.userservice.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createDefaultUser(CreateDefaultUserRequestDto createDefaultUserRequestDto);
    UserDto changeUserName(String newName);
    String getPassword(String username);
    String removeUser(String username);
    UserDto createAdminUser(CreateDefaultUserRequestDto createDefaultUserRequestDto);
    List<UserDto> getAllUsers();
}
