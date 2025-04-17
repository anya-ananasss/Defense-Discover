package backend.academy.userservice.servcie.impl;

import backend.academy.common.dto.CreateDefaultUserRequestDto;
import backend.academy.userservice.dto.UserDto;
import backend.academy.userservice.model.User;
import backend.academy.userservice.repository.UserRepository;
import backend.academy.userservice.servcie.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;


    @Override
    public UserDto createDefaultUser(CreateDefaultUserRequestDto createDefaultUserRequestDto) {
        return null;
    }

    @Override
    public UserDto changeUserName(String newName) {
        return null;
    }

    @Override
    public String getPassword(String username) {
        return "";
    }

    @Override
    public String removeUser(String username) {
        return "";
    }

    @Override
    public UserDto createAdminUser(CreateDefaultUserRequestDto createDefaultUserRequestDto) {
        return null;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return List.of();
    }
}

