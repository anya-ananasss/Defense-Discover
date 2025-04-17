package backend.academy.userservice.servcie;

import backend.academy.common.dto.CreateDefaultUserRequestDto;
import backend.academy.userservice.dto.UserDto;
import backend.academy.userservice.exception.UserNotFoundException;

import java.util.List;

public interface UserService {

    UserDto createDefaultUser(CreateDefaultUserRequestDto createDefaultUserRequestDto);
    UserDto changeUserName(String oldName, String newName) throws UserNotFoundException;
    String getPassword(String username) throws UserNotFoundException;
    String removeUser(String username) throws UserNotFoundException;
    UserDto createAdminUser(CreateDefaultUserRequestDto createDefaultUserRequestDto);
    List<UserDto> getAllUsers();
}
