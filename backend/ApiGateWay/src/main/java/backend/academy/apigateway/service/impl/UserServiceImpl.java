package backend.academy.apigateway.service.impl;


import backend.academy.apigateway.dto.PasswordRepair;
import backend.academy.apigateway.dto.UserConfirmation;
import backend.academy.apigateway.dto.security.RoleDto;
import backend.academy.apigateway.dto.security.UserDto;
import backend.academy.apigateway.exception.*;
import backend.academy.apigateway.client.RoleClient;
import backend.academy.apigateway.client.UserClient;
import backend.academy.apigateway.service.*;
import backend.academy.apigateway.utils.RandomUtils;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;

@Slf4j
@Service

public class UserServiceImpl implements UserService {

    private final JWTService jwtService;
    private final AuthenticationManager authManager;
    private final UserClient repo;
    private final RoleClient roleClient;
    private final UserClient userClient;
    private final KafkaClientService kafkaClientService;
    private final RedisService redisService;
    private final RoleService roleService;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public UserServiceImpl(JWTService jwtService, AuthenticationManager authManager, UserClient repo, RoleClient roleClient, UserClient userClient, RoleService roleService, KafkaClientService kafkaClientService, RedisService redisService) {
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.repo = repo;
        this.roleClient = roleClient;
        this.userClient = userClient;
        this.roleService = roleService;
        this.kafkaClientService = kafkaClientService;
        this.redisService = redisService;
    }

    public UserDto registerUser(UserDto user) {
        user.setPassword(encoder.encode(user.getPassword()));
        try {
            user.setRole(roleClient.getRoleByName("USER"));
            repo.createUser(user);
            return user;
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("Не удалось сохранить сущность User");
            return null;
        }
    }

    public String verify(UserDto user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getEmail());
        } else {
            return "fail";
        }
    }

    public String verifyAdmin(UserDto user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if (authentication.isAuthenticated() && authentication.getAuthorities().stream().anyMatch(e -> e.getAuthority().equals("ADMIN"))) {
            return jwtService.generateToken(user.getEmail());
        } else {
            return "fail";
        }
    }

    public String registerAndVerifyUser(UserDto user) {
        user.setPassword(encoder.encode(user.getPassword()));
        try {
            user.setRole(roleClient.getRoleByName("USER"));
            repo.createUser(user);
            Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(user.getEmail());
            }
        } catch (Exception e) {
            log.error("Не удалось сохранить или валидикровать сущность User");
            return null;
        }
        return null;
    }

    @Override
    public UserDto registerWithRole(UserDto user) {
        user.setPassword(encoder.encode(user.getPassword()));
        try {
            RoleDto roleEntity = roleClient.getRoleByName(user.getRole().getName());
            if (roleEntity != null) {
                user.setRole(roleEntity);
            } else {
                throw new RoleDoesntExist(user.getRole().getName());
            }
            repo.createUser(user);
            return user;
        } catch (Exception e) {
            log.error("Не удалось сохранить сущность User");
            return null;
        }
    }

    @Override
    public boolean deleteUser(UserDto user) {

        UserDto userEntity = userClient.getUserByUsername(user.getUsername());
        if (userEntity == null) {
            throw new UserNotFound(user.getUsername());
        }

        try {
            userClient.deleteUser(userEntity.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("User " + user.getUsername() + " не был удален");
            return false;
        }

        log.info("User " + user.getUsername() + " был удален");
        return true;
    }

    @Override
    public UserDto updatePassword(String newPassword, String oldPassword, String username) {
        UserDto userEntity = userClient.getUserByEmail(username);
        if (userEntity == null) {
            throw new UserNotFound(username);
        }
        if (encoder.matches(oldPassword, userEntity.getPassword())) {
            userEntity.setPassword(encoder.encode(newPassword));
            userClient.updateUser(userEntity.getId(), userEntity);
            return UserDto.builder().username(username).password(newPassword).build();
        }

        throw new WrongPasswordException(oldPassword);
    }

    @Override
    public RoleDto getRoles(String name) {

        UserDto userEntity = userClient.getUserByUsername(name);
        if (userEntity == null) {
            throw new UserNotFound(name);
        }

        return userEntity.getRole();
    }

    @Override
    public void addRole(RoleDto role, String username) {

        RoleDto roleEntity = roleClient.getRoleByName(role.getName());

        if (roleEntity == null) {
            throw new RoleDoesntExist(role.getName());
        }

        UserDto userEntity = userClient.getUserByUsername(username);

        if (userEntity == null) {
            throw new UserNotFound(username);
        }
        userEntity.setRole(roleEntity);
        repo.createUser(userEntity);
    }

    @Override
    public void deleteRole(RoleDto role, String username) {


        UserDto userEntity = userClient.getUserByUsername(username);

        if (userEntity == null) {
            throw new UserNotFound(username);
        }
        userEntity.setRole(null);
        userClient.updateUser(userEntity.getId(), userEntity);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userClient
                .getAllUsers();
    }

    @Override
    public List<UserDto> getAllUsers(int limit) {
        return userClient
                .getAllUsers()
                .stream()
                .limit(limit)
                .toList();
    }

    @Override
    public UserDto getUser(String username) {
        UserDto user = userClient.getUserByUsername(username);
        if (user == null) {
            throw new UserNotFound(username);
        }
        return user;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserDto user = userClient.getUserByEmail(email);
        if (user == null) {
            throw new UserNotFound(email);
        }
        return user;
    }

    @Override
    public String repairPassword(String email) {
        try {
            String tmpPassword = "tempPassword" + RandomUtils.generateSixDigitCode();
            kafkaClientService.sendPasswordRepair(
                    PasswordRepair.builder().password(tmpPassword).email(email).build());
            return tmpPassword;
        }catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
    }


    public UserDto requestToCreateUser(UserDto userDto) {
        checkUsernameAvailability(userDto.getUsername());
        checkEmailAvailability(userDto.getEmail());
        try {
            String code = RandomUtils.generateSixDigitCode();
            kafkaClientService.sendNotificationStackOverflow(
                    UserConfirmation
                            .builder()
                            .email(userDto.getEmail())
                            .username(userDto.getUsername())
                            .code(code)
                            .build());
            redisService.setValue(userDto.getEmail(), code);
            return userDto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void userConfirmation(UserDto userDto, String code) {
        try {
            String cachesCode = redisService.getValue(userDto.getEmail());

            if (Objects.equals(cachesCode, code)) {
                registerUser(userDto);
                return;
            }
            log.error(cachesCode + " " + code);

            throw new WrongConfirmationCode(userDto.getEmail());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void checkUsernameAvailability(String username) {
        try {
            userClient.getUserByUsername(username);
            throw new UsernameAlreadyTakenException("Username is taken");
        } catch (FeignException e) {
            log.info("username {} is available", username);
        }
    }

    public void checkEmailAvailability(String email) {
        try {
            userClient.getUserByEmail(email);
            throw new EmailAlreadyTakenException("Email is taken");
        } catch (FeignException e) {
            log.info("email {} is available", email);
        }
    }

}
