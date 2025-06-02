package backend.academy.userservice.service;

import backend.academy.userservice.TestContainersConfig;
import backend.academy.userservice.dto.RoleDto;
import backend.academy.userservice.dto.UserDto;
import backend.academy.userservice.mapper.UserMapper;
import backend.academy.userservice.model.Role;
import backend.academy.userservice.model.User;
import backend.academy.userservice.repository.RoleRepository;
import backend.academy.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class UserServiceTest extends TestContainersConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    private Role userRole;
    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        userRole = roleRepository.save(Role.builder()
            .name("TEST_ROLE")
            .build());

        testUser = User.builder()
            .username("testUser")
            .email("test@test.com")
            .password(new BCryptPasswordEncoder(12).encode("password"))
            .role(userRole)
            .build();

        testUser = userRepository.save(testUser);

        testUserDto = UserDto.builder()
            .username("newTestUser")
            .email("newtest@test.com")
            .password("newpassword")
            .role(RoleDto.builder()
                .id(userRole.getId())
                .name(userRole.getName())
                .build())
            .build();
    }

    @Test
    void getUserByIdShouldReturnUserWhenExists() {
        UserDto foundUser = userService.getUserById(testUser.getId());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(foundUser.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void getUserByIdShouldThrowExceptionWhenNotFound() {
        assertThrows(RuntimeException.class,
            () -> userService.getUserById(999L));
    }

    @Test
    void getUserByUsernameShouldReturnUserWhenExists() {
        UserDto foundUser = userService.getUserByUsername(testUser.getUsername());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(foundUser.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void createUserShouldCreateNewUser() {
        UserDto createdUser = userService.createUser(testUserDto);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo(testUserDto.getUsername());
        assertThat(createdUser.getEmail()).isEqualTo(testUserDto.getEmail());

        User savedUser = userRepository.findById(createdUser.getId()).orElseThrow();
        assertThat(savedUser.getUsername()).isEqualTo(testUserDto.getUsername());
        assertThat(savedUser.getEmail()).isEqualTo(testUserDto.getEmail());
    }

    @Test
    void updateUserShouldUpdateExistingUser() {
        String newUsername = "updatedUsername";
        String newEmail = "updated@test.com";
        testUserDto.setUsername(newUsername);
        testUserDto.setEmail(newEmail);

        UserDto updatedUser = userService.updateUser(testUser.getId(), testUserDto);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getUsername()).isEqualTo(newUsername);
        assertThat(updatedUser.getEmail()).isEqualTo(newEmail);

        User savedUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(savedUser.getUsername()).isEqualTo(newUsername);
        assertThat(savedUser.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void deleteUserShouldRemoveUser() {
        userService.deleteUser(testUser.getId());

        assertThat(userRepository.findById(testUser.getId())).isEmpty();
    }
}