package backend.academy.userservice.service;

import backend.academy.userservice.dto.RoleDto;
import backend.academy.userservice.dto.UserDto;
import backend.academy.userservice.mapper.UserMapper;
import backend.academy.userservice.model.Role;
import backend.academy.userservice.model.User;
import backend.academy.userservice.repository.UserRepository;
import backend.academy.userservice.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @PostConstruct
    @Transactional
    void init() {
        log.info("UserService initializating");
        if (roleRepository.findFirstByName("ADMIN").isEmpty()) {
            roleRepository.save(Role.builder().name("ADMIN").build());
        }

        if (roleRepository.findFirstByName("USER").isEmpty()) {
            roleRepository.save(Role.builder().name("USER").build());
        }

        if (userRepository.findByUsername("ADMIN").isEmpty()) {
            userRepository.save(
                    User.builder()
                            .username("ADMIN")
                            .password(encoder.encode("0000"))
                            .email("admin@admin.com")
                            .role(roleRepository.findByName("ADMIN").orElseThrow())
                            .isGameMaster(true)
                            .build()
            );
        }

        if (userRepository.findByUsername("gameMaster1").isEmpty()) {
            userRepository.save(
                    User.builder()
                            .username("gameMaster1")
                            .password(encoder.encode("gameMaster0000"))
                            .email("placeholder1@admin.com")
                            .role(roleRepository.findByName("USER").orElseThrow())
                            .isGameMaster(true)
                            .build()
            );
        }

        if (userRepository.findByUsername("gameMaster2").isEmpty()) {
            userRepository.save(
                    User.builder()
                            .username("gameMaster2")
                            .password(encoder.encode("gameMaster0000"))
                            .email("placeholder2@admin.com")
                            .role(roleRepository.findByName("USER").orElseThrow())
                            .isGameMaster(true)
                            .build()
            );
        }
        log.info("UserService inited");
    }

    public List<User> getAllUsers() {
        return userRepository.findAll().stream().peek(user -> user.setPassword("")).collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto getUserByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto getUserByEmail(String email) {

        log.info("UserService getUserByEmail: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        log.info(user.toString());
        UserDto userDto = userMapper.toDto(user);
        log.info(userDto.toString());
        return userDto;
    }



    public UserDto createUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        Role role = roleRepository.findById(userDto.getRole().getId())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);

        return userMapper.toDto(userRepository.save(user));
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));;
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        Role role = roleRepository.findById(userDto.getRole().getId())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);

        return userMapper.toDto(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
