package backend.academy.userservice.servcie.impl;

import backend.academy.common.dto.CreateDefaultUserRequestDto;
import backend.academy.userservice.dto.UserDto;
import backend.academy.userservice.exception.UserNotFoundException;
import backend.academy.userservice.mapper.UserMapper;
import backend.academy.userservice.model.Role;
import backend.academy.userservice.model.User;
import backend.academy.userservice.repository.RoleRepository;
import backend.academy.userservice.repository.UserRepository;
import backend.academy.userservice.servcie.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createDefaultUser(CreateDefaultUserRequestDto createDefaultUserRequestDto) {
        return createUser(createDefaultUserRequestDto, Set.of(roleRepository.findByRolename("user")));
    }

    @Override
    public UserDto changeUserName(String oldName, String newName) throws UserNotFoundException {
        User userEntity = getUserByUsername(oldName);
        userEntity.setUsername(newName);
        return userMapper.userToUserDTO(userRepository.save(userEntity));
    }

    @Override
    public String getPassword(String username) throws UserNotFoundException {
        User userEntity = getUserByUsername(username);
        return userEntity.getPassword();
    }

    @Override
    public String removeUser(String username) throws UserNotFoundException {
        User userEntity = getUserByUsername(username);
        userRepository.delete(userEntity);
    }

    @Override
    public UserDto createAdminUser(CreateDefaultUserRequestDto createDefaultUserRequestDto) {
       return createUser(createDefaultUserRequestDto, Set.of(roleRepository.findByRolename("admin")));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::userToUserDTO)
                .toList();
    }

    private UserDto createUser(CreateDefaultUserRequestDto createDefaultUserRequestDto, Set<Role> roles) {
        User userEntity = User
                .builder()
                .roles(
                        roles
                )
                .username(createDefaultUserRequestDto.username())
                .email(createDefaultUserRequestDto.mail())
                .password(createDefaultUserRequestDto.password())
                .keyWords(Set.of())
                .stats(Set.of())
                .build();

        return userMapper.userToUserDTO(userRepository.save(userEntity));
    }

    private User getUserByUsername(String username) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        return optionalUser.get();
    }
}

