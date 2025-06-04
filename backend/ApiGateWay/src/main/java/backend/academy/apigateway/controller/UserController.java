package backend.academy.apigateway.controller;


import backend.academy.apigateway.client.UserClient;
import backend.academy.apigateway.dto.EmailDto;
import backend.academy.apigateway.dto.UserDtoWithoutPassword;
import backend.academy.apigateway.dto.security.AddRoleDto;
import backend.academy.apigateway.dto.security.ChangingPasswordDto;
import backend.academy.apigateway.dto.security.RoleDto;
import backend.academy.apigateway.dto.security.UserDto;
import backend.academy.apigateway.exception.*;
import backend.academy.apigateway.service.UserService;
import backend.academy.apigateway.utils.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Slf4j
@Controller
@RestController
@RequiredArgsConstructor
@Tag(name = "Пользователь", description = "Взаимодействие с пользователями")
public class UserController {

    private final UserService userService;
    private final UserClient userClient;

    @PostMapping((ApiPaths.BASE_API + "/register"))
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        try {
            log.info("Registering user: {}", userDto);
            userService.registerUser(userDto);
            return ResponseEntity.ok("Successfully registered");
        } catch (UsernameAlreadyTakenException e) {
            log.error("Registration error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to register user with username " + userDto.getUsername());
        }  catch (EmailAlreadyTakenException e) {
            log.error("Registration error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Failed to register user with email " + userDto.getEmail());
        } catch (Exception e) {
            log.error(e.getMessage() + userDto.toString());
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @Operation(summary = "Логин")
    @PostMapping(ApiPaths.BASE_API + "/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        try {
            log.info("Logining user: {}", userDto);
            return ResponseEntity.ok(userService.verify(userDto));
        } catch (Exception e) {
            log.error(e.getMessage() + userDto.toString());
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @Operation(summary = "Логин в админ панель")
    @PostMapping(ApiPaths.BASE_API + "/loginadmin")
    public ResponseEntity<String> loginToAdminPanel(@RequestBody UserDto userDto) {
        try {
            log.info("Logining user: {}", userDto);
            return ResponseEntity.ok(userService.verifyAdmin(userDto));
        } catch (Exception e) {
            log.error(e.getMessage() + userDto.toString());
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PostMapping(ApiPaths.BASE_API + "/registerAndLogin")
    public ResponseEntity<String> registerAndLogin(@RequestBody UserDto userDto) {
        try {
            log.info("Registrating and logining user: {}", userDto);
            return ResponseEntity.ok(userService.registerAndVerifyUser(userDto));
        } catch (Exception e) {
            log.error(e.getMessage() + userDto.toString());
            return ResponseEntity.unprocessableEntity().build();
        }
    }


    @PostMapping((ApiPaths.ADMIN_API + "/registerWithRole"))
    public ResponseEntity<String> registerWithRole(@RequestBody UserDto userDto) {
        try {
            userService.registerWithRole(userDto);
            return ResponseEntity.ok().build();
        } catch (RoleDoesntExist e) {
            return ResponseEntity.unprocessableEntity().body("Не существует роли " + e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage() + userDto.toString());
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @Operation(summary = "Удаление пользователя")
    @PostMapping(ApiPaths.ADMIN_API + "/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestBody UserDto userDto) {
        try {
            userService.deleteUser(userDto);
            return ResponseEntity.ok().build();
        } catch (UserNotFound e) {
            return ResponseEntity.unprocessableEntity().body("User not found " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @Operation(summary = "Смена пароля")
    @PostMapping(ApiPaths.USER_API + "/changePassword")
    public ResponseEntity<String> updatePassword(@RequestBody ChangingPasswordDto changingPasswordDto, @AuthenticationPrincipal UserDetails userDetails) {
        try {

            userService.updatePassword(
                changingPasswordDto.getNewPassword(),
                changingPasswordDto.getOldPassword(),
                userDetails.getUsername());

            return ResponseEntity.ok().build();

        } catch (UserNotFound e) {
            return ResponseEntity.unprocessableEntity().body("User not found " + e.getMessage());
        } catch (WrongPasswordException e) {
            return ResponseEntity.unprocessableEntity().body("Wrong password " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @Operation(summary = "Получить роль пользователя")
    @PostMapping(ApiPaths.ADMIN_API + "/getRoles")
    public ResponseEntity<RoleDto> getRoles(@RequestBody UserDto userDto) {
        try {
            RoleDto res = userService.getRoles(userDto.getUsername());
            return ResponseEntity.ok(res);
        } catch (UserNotFound e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }


    @Operation(summary = "Добавить роль пользователю")
    @PostMapping(ApiPaths.ADMIN_API + "/addRoleToUser")
    public ResponseEntity<String> addRole(@RequestBody AddRoleDto addRoleDto) {
        try {
            userService.addRole(addRoleDto.getRole(), addRoleDto.getUsername());
            return ResponseEntity.ok().build();
        } catch (UserNotFound e) {
            return ResponseEntity.unprocessableEntity().body("User not found " + e.getMessage());
        } catch (RoleDoesntExist e) {
            return ResponseEntity.unprocessableEntity().body("Role not found " + e.getMessage());
        }

    }

    @Operation(summary = "Убрать роль у пользователя")
    @PostMapping(ApiPaths.ADMIN_API + "/deleteRoleFromUser")
    public ResponseEntity<String> deleteRoleFromUser(@RequestBody AddRoleDto addRoleDto) {
        try {
            userService.deleteRole(addRoleDto.getRole(), addRoleDto.getUsername());
            return ResponseEntity.ok().build();
        } catch (UserNotFound e) {
            log.error(e.getMessage());
            return ResponseEntity.unprocessableEntity().body("User not found " + e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @Operation(summary = "Получить список пользователей")
    @GetMapping(ApiPaths.ADMIN_API + "/getAllUsers")
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(required = false) String limit) {
        try {
            if (limit != null) {
                int limitInt = Integer.parseInt(limit);
                return ResponseEntity.ok(userService.getAllUsers(limitInt));
            }
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Получить конкретного пользователя")
    @PostMapping(ApiPaths.BASE_API + "/getUser")
    public ResponseEntity<UserDtoWithoutPassword> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            UserDto userDto = userService.getUserByEmail(userDetails.getUsername());
            log.info(userDto.toString());
            UserDtoWithoutPassword userDtoWithoutPassword = UserDtoWithoutPassword
                .builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .role(userDto.getRole())
                .isGameMaster(userDto.isGameMaster())
                .build();
            return ResponseEntity.ok(userDtoWithoutPassword);
        } catch (UserNotFound e) {
            log.error(e.getMessage() + userDetails.getUsername());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error(e.getMessage() + userDetails.getUsername());
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @Operation(summary = "Восстановление пароля")
    @PostMapping(ApiPaths.BASE_API + "/repairPassword")
    public ResponseEntity<Void> getUser(@RequestBody EmailDto email) {
        try {
            String tempPassword = userService.repairPassword(email.getEmail());
            userClient.repairPasswordByEmail(email.getEmail(), tempPassword);
            return ResponseEntity.ok().build();
        } catch (UserNotFound e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @Operation(summary = "Регистрация с подтверждением через почту")
    @PostMapping(ApiPaths.BASE_API + "/createUser")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        try{
            return ResponseEntity.ok().body(userService.requestToCreateUser(userDto));
        } catch (UsernameAlreadyTakenException e) {
            log.error("Registration error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDto);
        } catch (EmailAlreadyTakenException e) {
            log.error("Registration error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(userDto);
        }
    }

    @Operation(summary = "Отправка кода подтверждения почты")
    @PostMapping(ApiPaths.BASE_API + "/confirmationUser")
    public ResponseEntity<UserDto> confirmationUser(@RequestBody UserDto userDto, @RequestParam(name = "code") int code) {
        userService.userConfirmation(userDto, String.valueOf(code));
        return ResponseEntity.ok().build();
    }

}
