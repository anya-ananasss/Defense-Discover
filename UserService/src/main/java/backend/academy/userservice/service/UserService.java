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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @PostConstruct
    @Transactional
    void init() {
        roleRepository.save(Role.builder().name("ADMIN").build());
        roleRepository.save(Role.builder().name("USER").build());

        userRepository.save(
                User
                        .builder()
                        .username("ADMIN")
                        .password(encoder.encode("0000"))
                        .email("admin@admin.com")
                        .role(roleRepository.findByName("ADMIN").orElseThrow(NullPointerException::new))
                        .build()
        );
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
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
